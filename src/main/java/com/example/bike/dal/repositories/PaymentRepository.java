package com.example.bike.dal.repositories;

import com.example.bike.dal.models.Payment;
import com.example.bike.dal.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
