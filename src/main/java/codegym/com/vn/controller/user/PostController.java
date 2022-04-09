package codegym.com.vn.controller.user;


import codegym.com.vn.model.Comment;
import codegym.com.vn.model.Post;
import codegym.com.vn.service.interfaceService.ICommentService;
import codegym.com.vn.service.interfaceService.IPostService;
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

    @GetMapping
    public ResponseEntity<Iterable<Post>> showAll() {
        Iterable<Post> posts = iPostService.findAll();
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


}
