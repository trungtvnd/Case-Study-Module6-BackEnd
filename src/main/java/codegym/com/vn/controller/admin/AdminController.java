package codegym.com.vn.controller.admin;

import codegym.com.vn.model.Post;
import codegym.com.vn.model.User;
import codegym.com.vn.service.interfaceService.IAdminService;
import codegym.com.vn.service.interfaceService.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService iAdminService;

    @Autowired
    private IPostService iPostService;


    @GetMapping
    public ResponseEntity<Iterable<User>> showAll() {
        Iterable<User> users = iAdminService.findAll();
        if (!users.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/findByName/{title}")
    public ResponseEntity<Iterable<Post>> findAllPostByTitle(@PathVariable("title") String title) {
        Iterable<Post> posts = iPostService.findByName(title);
        if (!posts.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


}
