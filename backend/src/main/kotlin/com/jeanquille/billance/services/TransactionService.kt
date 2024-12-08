package com.jeanquille.billance.services

import com.jeanquille.billance.models.Transaction
import com.jeanquille.billance.repositories.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(val transactionRepository: TransactionRepository) {
    fun getTransactionsInParty(partyId: Long): MutableList<Transaction> {
        return transactionRepository.findByPartyId(partyId)
    }
}