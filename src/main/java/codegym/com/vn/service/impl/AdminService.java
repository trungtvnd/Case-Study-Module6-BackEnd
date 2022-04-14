package codegym.com.vn.service.impl;

import codegym.com.vn.model.User;
import codegym.com.vn.repository.IAdminRepository;
import codegym.com.vn.service.interfaceService.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IAdminRepository iAdminRepository;

    @Override
    public Iterable<User> findAll() {
        return iAdminRepository.findAll();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Page<User> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<User> findByName(String name) {
        return null;
    }
}
