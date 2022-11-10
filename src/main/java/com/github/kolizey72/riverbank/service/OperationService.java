package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findAllByUserId(long userId) {
        return operationRepository.findAllByAccountUserIdOrderByDateTimeDesc(userId);
    }

    public List<Operation> findAllByAccountNumber(long accountNumber) {
        return operationRepository.findAllByAccountNumberOrderByDateTimeDesc(accountNumber);
    }

    @Transactional
    public void create(Operation operation) {
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
    }
}
