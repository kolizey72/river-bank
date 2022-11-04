package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.entity.OperationType;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.exception.NotFoundException;
import com.github.kolizey72.riverbank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;


    private final EntityManager entityManager;

    public AccountService(AccountRepository accountRepository, OperationService operationService, EntityManager entityManager) {
        this.accountRepository = accountRepository;
        this.operationService = operationService;
        this.entityManager = entityManager;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public List<Account> findAllByUserId(long userId) {
        return accountRepository.findAllByUserIdOrderByNumber(userId);
    }

    public Account findByNumber(long number) {
        return accountRepository.findById(number).orElseThrow(() -> new NotFoundException(number, Account.class));
    }

    public Account findByNumber(long number, long userId) {
        return accountRepository.findAllByUserId(userId).stream()
                .filter(a -> a.getNumber().equals(number))
                .findAny().orElseThrow(() -> new NotFoundException(number, Account.class));
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

        Account account = findByNumber(number, userId);

        account.setAmount(account.getAmount() + amount);
        operationService.create(new Operation(account, amount, OperationType.DEPOSIT));
    }

    @Transactional
    public void withdraw(long userId, long number, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        Account account = findByNumber(number, userId);

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

        Account sender = findByNumber(senderNumber, userId);
        Account recipient;
        try {
            recipient = findByNumber(recipientNumber);
        } catch (NotFoundException e) {
            throw new IllegalArgumentException(String.format("Account %d doesn't exist", recipientNumber));
        }

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
}
