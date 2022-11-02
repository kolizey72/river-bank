package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.entity.OperationType;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.repository.AccountRepository;
import com.github.kolizey72.riverbank.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final OperationService operationService;


    private final EntityManager entityManager;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, OperationService operationService, EntityManager entityManager) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.operationService = operationService;
        this.entityManager = entityManager;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public List<Account> findAllByUserId(long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public Account findByNumber(long number) {
        return accountRepository.findById(number).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void create(Account account, long userId) {
        User user = entityManager.find(User.class, userId);

        user.addAccount(account);
        account.setUser(user);

        accountRepository.save(account);
        operationService.create(new Operation(account, 0L, OperationType.CREATION));
    }

    @Transactional
    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public void deposit(long userId, long number, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        if (!isAccountOwner(userId, number)) {
            throw new IllegalArgumentException(String.format("User %d has no access to account %d", userId, number));
        }

        Account account = findByNumber(number);

        account.setAmount(account.getAmount() + amount);
        operationService.create(new Operation(account, amount, OperationType.DEPOSIT));
    }

    @Transactional
    public void withdraw(long userId, long number, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        if (!isAccountOwner(userId, number)) {
            throw new IllegalArgumentException(String.format("User %d has no access to account %d", userId, number));
        }

        Account account = findByNumber(number);

        if (account.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough money");
        }

        account.setAmount(account.getAmount() - amount);
        operationService.create(new Operation(account, amount, OperationType.WITHDRAW));
    }

    @Transactional
    public void transfer(long userId, long senderNumber, long recipientNumber, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        if (!isAccountOwner(userId, senderNumber)) {
            throw new IllegalArgumentException(String.format("User %d has no access to account %d", userId, senderNumber));
        }

        Account sender = findByNumber(senderNumber);
        Account recipient = findByNumber(recipientNumber);

        if (sender.getCurrency() != recipient.getCurrency()) {
            throw new UnsupportedOperationException(String.format("Can't convert %s to %s", sender.getCurrency(), recipient.getCurrency()));
        }

        if (sender.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough money");
        }

        sender.setAmount(sender.getAmount() - amount);
        operationService.create(new Operation(sender, amount, OperationType.TRANSFER));
        recipient.setAmount(recipient.getAmount() + amount);
        operationService.create(new Operation(recipient, amount, OperationType.DEPOSIT));
    }

    private boolean isAccountOwner(long userId, long accountNumber) {
        return findAllByUserId(userId).contains(findByNumber(accountNumber));
    }
}
