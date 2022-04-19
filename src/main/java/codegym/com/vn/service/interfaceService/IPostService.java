package codegym.com.vn.service.interfaceService;

import codegym.com.vn.model.Post;
import codegym.com.vn.model.User;
import codegym.com.vn.service.InterfaceGeneral;

import java.awt.*;
import java.util.Optional;

public interface IPostService extends InterfaceGeneral<Post> {
    Iterable<Post> findPostByIdUser(Long idUser);
    Optional<Post> findByHashTags(String hashTags);
    Iterable<Post> findPostByIdHashTags(Long idHashTags);

}
