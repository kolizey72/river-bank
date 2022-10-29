package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    public List<Operation> findAllByUserId(long userId) {
        return operationRepository.findAllByAccountUserId(userId);
    }

    public List<Operation> findAllByAccountNumber(long accountNumber) {
        return operationRepository.findAllByAccountNumber(accountNumber);
    }

    public Operation findById(long id) {
        return operationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void create(Operation operation) {
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
    }

    @Transactional
    public void delete(long id) {
        operationRepository.deleteById(id);
    }
}
