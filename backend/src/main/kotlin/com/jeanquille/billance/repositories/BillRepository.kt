package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Bill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BillRepository: JpaRepository<Bill, Long> {
    fun findByPartyId(partyId: Long): MutableList<Bill>
}