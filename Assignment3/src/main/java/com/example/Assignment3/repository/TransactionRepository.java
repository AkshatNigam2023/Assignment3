package com.example.Assignment3.repository;

import com.example.Assignment3.model.Transaction;
import com.example.Assignment3.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     Optional<List<Transaction>> findByStatus(TransactionStatus transactionStatus);
}
