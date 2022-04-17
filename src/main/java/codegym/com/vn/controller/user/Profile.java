package codegym.com.vn.controller.user;


import codegym.com.vn.model.Post;
import codegym.com.vn.model.User;
import codegym.com.vn.repository.IUserRepository;
import codegym.com.vn.service.Account.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin("*")
@RequestMapping("user/profile")
public class Profile {

    @Autowired
    private IUserService iUserService;


    @GetMapping("/{id}")
    public ResponseEntity<User> showOne(@PathVariable("id") Long id) {
        Optional<User> user2 = iUserService.findById(id);
        if (!user2.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user2.get(), HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<User> editProfile(@RequestBody User userEdit, @PathVariable("id") Long id) {
        Optional<User> user = iUserService.findById(id);
        if (!user.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userEdit.setId(user.get().getId());
        userEdit = iUserService.save(userEdit);
        return new ResponseEntity<>(userEdit, HttpStatus.OK);
    }
    }





