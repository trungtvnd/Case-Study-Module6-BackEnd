package codegym.com.vn.controller.auth;



import codegym.com.vn.dto.request.*;
import codegym.com.vn.dto.response.JwtResponse;
import codegym.com.vn.dto.response.ResponseMessage;
import codegym.com.vn.model.Role;
import codegym.com.vn.model.RoleName;
import codegym.com.vn.model.User;
import codegym.com.vn.security.jwt.JwtAuthTokenFilter;
import codegym.com.vn.security.jwt.JwtProvider;
import codegym.com.vn.security.service.UserPrinciple;
import codegym.com.vn.service.Account.IRoleService;
import codegym.com.vn.service.Account.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtAuthTokenFilter jwtAuthTokenFilter;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
            Optional<User> user = userService.findByUsername(loginRequest.getUsername());
        if (userService.existsByEmail(user.get().getEmail())){
            if (!userService.findByEmail(user.get().getEmail()).get().getStatusActive()){
                return new ResponseEntity<>(new ResponseMessage("Vui lòng kích hoạt tài khoản của bạn!"), HttpStatus.BAD_REQUEST);
            }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(),userDetails.getId(), userDetails.getFullName(), userDetails.getEmail(),
                userDetails.getPhone(), userDetails.getAddress(), userDetails.getAvatar(), userDetails.getAuthorities()
        ));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("User existed, please entry other user"),
                    HttpStatus.OK);
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Email existed, please entry other email"),
                    HttpStatus.OK);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFullName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userService.save(user);

        return new ResponseEntity<>(new ResponseMessage("Mail has been send. Please check to active your account!"), HttpStatus.OK);
    }

    @PutMapping("changeProfile")
    public ResponseEntity<?> changeProfile(HttpServletRequest request, @Valid @RequestBody ChangeProfileForm changeProfileForm) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user;
        try{
            if(userService.existsByEmail(changeProfileForm.getEmail())){
                return new ResponseEntity<>(new ResponseMessage("noemail"), HttpStatus.OK);
            }
            user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found" + username));
            user.setEmail(changeProfileForm.getEmail());
            user.setAddress(changeProfileForm.getAddress());
            user.setPhone(changeProfileForm.getPhone());
            user.setFullName(changeProfileForm.getFullName());
            userService.save(user);
            return new ResponseEntity<>(new ResponseMessage("change successfully"), HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/active/{token}")
    public ResponseEntity<?> activeUserByToken(@PathVariable String token){
        userService.activeUser(token);
        return new ResponseEntity<>("Active successfully!", HttpStatus.OK);
    }
    @GetMapping("/check-email/{mail}")
    public ResponseEntity<?> checkEmail(@PathVariable String mail){
        if (userService.existsByEmail(mail)){
            if (userService.findByEmail(mail).get().getStatusActive()){
                return new ResponseEntity<>(userService.findByEmail(mail), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("changePassword")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody ChangePasswordForm changePassword) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user;
        try{
            user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found" + username));
            if (!passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
//            Mã 600 là lỗi sai mật khẩu hiện tại
                return new ResponseEntity<>(new ResponseMessage("600"), HttpStatus.OK);
            } else if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword())) {
//            Mã 601 là lỗi xác nhận mật khẩu mới sai
                return new ResponseEntity<>(new ResponseMessage("601"), HttpStatus.OK);
            }
            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userService.save(user);
            return new ResponseEntity<>(new ResponseMessage("change password successfully"), HttpStatus.OK);

        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("changeAvatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @RequestBody ChangeAvatar changeAvatar) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user;
        try{
            if(changeAvatar.getAvatar()== null){
                return new ResponseEntity<>(new ResponseMessage("not found"), HttpStatus.OK);
            }else {
                user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found" + username));
                user.setAvatar(changeAvatar.getAvatar());
                userService.save(user);
            }
            return new ResponseEntity<>(new ResponseMessage("change avatar successfully"), HttpStatus.OK);

        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
