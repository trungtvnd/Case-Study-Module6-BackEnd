package codegym.com.vn.controller.user;


import codegym.com.vn.model.*;
import codegym.com.vn.service.Account.IUserService;
import codegym.com.vn.service.interfaceService.IHashTagService;
import codegym.com.vn.service.interfaceService.IPostService;
import codegym.com.vn.service.interfaceService.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("user/post")
public class PostController {

    @Autowired
    private IPostService iPostService;
    @Autowired
    private IHashTagService iHashTagService;
    @Autowired
    private IStatusService iStatusService;
    @Autowired
    private IUserService iUserService;

    @GetMapping
    public ResponseEntity<Iterable<Post>> showAll() {
        Iterable<Post> posts = iPostService.findAll();
        if (!posts.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("displayByUser/{id}")
    public ResponseEntity<Iterable<Post>> showAllByUser(@PathVariable("id") Long idUser) {
        Iterable<Post> posts = iPostService.findPostByIdUser(idUser);
        if (!posts.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    //lấy 1 đối tượng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Post> showOne(@PathVariable("id") Long id) {
        Optional<Post> post = iPostService.findById(id);
        if (!post.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    //tạo mới 1 đội tượng
    @PostMapping
    public ResponseEntity<Post> createProduct(@RequestBody Post post) {
        Post postCreate = iPostService.save(post);
        return new ResponseEntity<>(postCreate, HttpStatus.CREATED);
    }

    //cập nhật 1 đối tượng có id
    @PutMapping("{id}")
    public ResponseEntity<Post> editProduct(@RequestBody Post postEdit, @PathVariable("id") Long id) {
        Optional<Post> post = iPostService.findById(id);
        if (!post.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        postEdit.setId(post.get().getId());
        postEdit = iPostService.save(postEdit);
        return new ResponseEntity<>(postEdit, HttpStatus.OK);
    }

    //xóa 1 đối tượng theo id
    @DeleteMapping("{id}")
    public ResponseEntity<Post> delete(@PathVariable("id") Long id) {
        Optional<Post> post = iPostService.findById(id);
        if (!post.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        iPostService.delete(id);
        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Iterable<Status>> showAllStatus() {
        Iterable<Status> status = iStatusService.findAll();
        if (!status.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> showAllUser() {
        Iterable<User> users  = iUserService.findAll();
        if (!users.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/hashtags")
    public ResponseEntity<Iterable<HashTags>> showAllHashtags() {
        Iterable<HashTags> hashTags = iHashTagService.findAll();
        if (!hashTags.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hashTags, HttpStatus.OK);
    }

    @GetMapping("users/{fullName}")
    public ResponseEntity<?> findUserByFullName(@PathVariable("fullName") String fullName) {
        Optional<User> user = iUserService.findByFullName(fullName);
        if (!user.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }


}
