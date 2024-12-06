package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {
    fun findByPartyId(partyId: Long): MutableList<Transaction>
}