package com.github.kolizey72.riverbank.repository;

import com.github.kolizey72.riverbank.entity.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByAccountUserId(long userId, Pageable pageable);
    List<Operation> findAllByAccountNumber(long accountNumber, Pageable pageable);

    long countByAccountUserId(long userId);
    long countByAccountNumber(long accountNumber);
}
