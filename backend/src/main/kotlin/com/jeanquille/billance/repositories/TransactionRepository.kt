package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Transaction
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    @Transactional
    fun deleteByPartyId(partyId: Long)
    fun getTransactionById(transactionId: Long): Optional<Transaction>
}
