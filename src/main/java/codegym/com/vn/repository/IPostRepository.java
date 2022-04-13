package codegym.com.vn.repository;

import codegym.com.vn.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public interface IPostRepository extends JpaRepository<Post,Long> {

    Iterable<Post>findAllByTitleContaining(String title);

    Iterable<Post>findAllByContentContaining(String content);

    Iterable<Post>findAllByUser_Id(Long id);

//    Iterable<Post>findAllByHashTagsContaining(List<HashTag> hashTags);



}
