package codegym.com.vn.controller.auth;



import codegym.com.vn.message.request.ChangeProfileForm;
import codegym.com.vn.message.request.LoginForm;
import codegym.com.vn.message.request.SignUpForm;
import codegym.com.vn.message.response.JwtResponse;
import codegym.com.vn.message.response.ResponseMessage;
import codegym.com.vn.model.Role;
import codegym.com.vn.model.RoleName;
import codegym.com.vn.model.User;
import codegym.com.vn.security.jwt.JwtAuthTokenFilter;
import codegym.com.vn.security.jwt.JwtProvider;
import codegym.com.vn.security.service.UserPrinciple;
import codegym.com.vn.service.Account.IRoleService;
import codegym.com.vn.service.Account.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
}
