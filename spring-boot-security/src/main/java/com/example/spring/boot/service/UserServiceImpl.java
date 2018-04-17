package com.example.spring.boot.service;

import com.example.spring.boot.dto.UserCreateForm;
import com.example.spring.boot.model.Role;
import com.example.spring.boot.model.User;
import com.example.spring.boot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnotherUserService anotherUserService;

    public Optional<User> getByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        User findOne = userRepository.findOne(id);
        return findOne;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    /**
     * 事务是内置在SimpleJpaRepository上
     *
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#findAll()
     * @see UserService#findAll()
     */
    @Override
    public Collection<User> findAll() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    public User create(UserCreateForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }

    @Transactional
    public User createInNewTransaction(UserCreateForm form) {
        long currentTimeMillis = System.currentTimeMillis();
        UserCreateForm random = new UserCreateForm();
        random.setEmail(currentTimeMillis + "@gmail.com");
        random.setPassword("test" + currentTimeMillis);
        random.setRole(Role.ADMIN);
        random.setUsername("name" + currentTimeMillis);
        User user = create(random);

        try {
            // Participating in existing transaction
            // 调用bean内部方法，方法其实并没有被spring增强，仍然是在同一个事务之内的
            // test(form);

            // 会新建事务
            // Suspending current transaction, creating new transaction with name [com.example.service.AnotherUserService.test]
            anotherUserService.test(form);

            // 在方法里抛出 RuntimeException
            // nested exception is javax.persistence.RollbackException: Transaction marked as rollbackOnly
            // anotherUserService.test2(form);
        } catch (Exception e) {
            LOGGER.error("create user error", e);
        }
        return user;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test(UserCreateForm form) {
        LOGGER.info("===================================enter exception method : 1");
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setRole(form.getRole());
        user.setPasswordHash(form.getPassword());
        userRepository.save(user);
        LOGGER.info("===================================enter exception method : 2");
    }

}