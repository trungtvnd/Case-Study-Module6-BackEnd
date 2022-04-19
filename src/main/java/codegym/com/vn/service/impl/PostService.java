package codegym.com.vn.service.impl;

import codegym.com.vn.model.Post;
import codegym.com.vn.repository.IPostRepository;
import codegym.com.vn.service.interfaceService.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository iPostRepository;

    @Override
    public Iterable<Post> findAll() {
        return iPostRepository.findAll();
    }

    @Override
    public Post save(Post post) {
        return iPostRepository.saveAndFlush(post);
    }

    @Override
    public void delete(Long id) {
         iPostRepository.deleteById(id);

    }

    @Override
    public Page<Post> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return iPostRepository.findById(id);
    }

    @Override
    public Iterable<Post> findByName(String name) {
        return iPostRepository.findAllByTitleContaining(name);
    }

    @Override
    public Iterable<Post> findPostByIdUser(Long idUser) {
        return iPostRepository.findAllByUser_Id(idUser);
    }

    @Override
    public Optional<Post> findByHashTags(String hashTags) {
        return Optional.empty();
    }


    @Override
    public Iterable<Post> findPostByHashTag(Long idHashTag) {
        return iPostRepository.findAllByHashTags_Id(idHashTag);
    }


    @Override
    public Iterable<Post> findPostByIdStatus(Long idStatus) {
        return iPostRepository.findAllByStatus_Id(idStatus);
    }
}
