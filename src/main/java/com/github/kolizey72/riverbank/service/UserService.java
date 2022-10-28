package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Currency;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final AccountService accountService;

    public UserService(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void create(User user) {
        userRepository.save(user);

        if (user.getAccounts() == null && user.getAccounts().isEmpty()) {
            Account account = new Account(user, 0L, Currency.RUB);
            accountService.create(account);
            user.addAccount(account);
        }
    }

    @Transactional
    public void update(long id, User updatedUser) {
        userRepository.findById(id).ifPresent(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
        });
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
