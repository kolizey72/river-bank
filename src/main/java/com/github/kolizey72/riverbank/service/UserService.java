package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Currency;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.entity.dto.RegistrationForm;
import com.github.kolizey72.riverbank.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final int DEFAULT_MAX_ACCOUNTS = 5;
    private final Currency DEFAULT_CURRENCY = Currency.RUB;

    private final UserRepository userRepository;

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    public Optional<User> findByNameIgnoreCase(String name) {
        return userRepository.findByNameIgnoreCase(name);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    public void register(RegistrationForm registrationForm) {
        User user = new User();

        user.setName(registrationForm.getName());
        user.setEmail(registrationForm.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setMaxAccounts(DEFAULT_MAX_ACCOUNTS);

        userRepository.save(user);

        if (user.getAccounts() == null || user.getAccounts().isEmpty()) {
            Account account = new Account(0L, DEFAULT_CURRENCY);
            accountService.create(account, user.getId());
        }
    }
}
