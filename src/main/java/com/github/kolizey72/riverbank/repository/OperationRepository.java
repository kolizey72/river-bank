package com.github.kolizey72.riverbank.repository;

import com.github.kolizey72.riverbank.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
