package com.github.kolizey72.riverbank.service;

import com.github.kolizey72.riverbank.entity.Operation;
import com.github.kolizey72.riverbank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    public Operation findById(long id) {
        return operationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void create(Operation operation) {
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
    }

    public void delete(long id) {
        operationRepository.deleteById(id);
    }
}
