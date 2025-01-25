package com.jeanquille.billance.controllers

import com.jeanquille.billance.models.Transaction
import com.jeanquille.billance.repositories.TransactionRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionController(private val transactionRepository: TransactionRepository) {

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: Long): Transaction {
        return transactionRepository.getTransactionById(transactionId).orElseThrow()
    }
}