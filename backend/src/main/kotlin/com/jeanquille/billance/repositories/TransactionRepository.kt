package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Transaction
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {
    fun findAllByPartyId(partyId: Long): MutableList<Transaction>
    @Transactional
    fun deleteByPartyId(partyId: Long)
}