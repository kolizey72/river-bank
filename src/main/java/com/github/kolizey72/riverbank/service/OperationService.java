package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.repository.OperationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OperationService {

    private final int OPERATIONS_PER_PAGE = 10;

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findAllByUserId(long userId, int page) {
        return operationRepository.findAllByAccountUserId(userId, PageRequest.of(page - 1, OPERATIONS_PER_PAGE, Sort.by("dateTime").descending()));
    }

    public List<Operation> findAllByAccountNumber(long accountNumber, int page) {
        return operationRepository.findAllByAccountNumber(accountNumber, PageRequest.of(page - 1, OPERATIONS_PER_PAGE, Sort.by("dateTime").descending()));
    }

    public long countPagesByUserId(long userId) {
        return (operationRepository.countByAccountUserId(userId) / OPERATIONS_PER_PAGE) + 1;
    }

    public long countPagesByAccountNumber(long accountNumber) {
        return (operationRepository.countByAccountNumber(accountNumber) / OPERATIONS_PER_PAGE) + 1;
    }

    @Transactional
    public void create(Operation operation) {
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
    }
}
