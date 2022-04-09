package codegym.com.vn.service.impl;

import codegym.com.vn.model.Comment;
import codegym.com.vn.repository.ICommentRepository;
import codegym.com.vn.service.interfaceService.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentRepository iCommentRepository;

    @Override
    public Iterable<Comment> findAll() {
        return iCommentRepository.findAll();
    }

    @Override
    public Comment save(Comment comment) {
        return iCommentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        iCommentRepository.deleteById(id);

    }

    @Override
    public Page<Comment> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return iCommentRepository.findById(id);
    }

    @Override
    public Iterable<Comment> findByName(String name) {
        return null;
    }
}
