package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.entity.OperationType;
import com.github.kolizey72.riverbank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;

    public AccountService(AccountRepository accountRepository, OperationService operationService) {
        this.accountRepository = accountRepository;
        this.operationService = operationService;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(long id) {
        return accountRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void create(Account account) {
        operationService.create(new Operation(account, 0L, OperationType.CREATION));
        accountRepository.save(account);
    }

    @Transactional
    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public void deposit(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        Account account = findById(id);

        account.setAmount(account.getAmount() + amount);
        operationService.create(new Operation(account, amount, OperationType.DEPOSIT));
    }

    @Transactional
    public void withdraw(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        Account account = findById(id);

        if (account.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough money");
        }

        account.setAmount(account.getAmount() - amount);
        operationService.create(new Operation(account, amount, OperationType.WITHDRAW));
    }

    @Transactional
    public void transfer(long senderId, long recipientId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less or equals 0");
        }

        Account sender = findById(senderId);
        Account recipient = findById(recipientId);

        if (sender.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough money");
        }

        sender.setAmount(sender.getAmount() - amount);
        operationService.create(new Operation(sender, amount, OperationType.TRANSFER));
        recipient.setAmount(recipient.getAmount() + amount);
        operationService.create(new Operation(recipient, amount, OperationType.DEPOSIT));
    }
}