package com.jeanquille.billance.services

import com.jeanquille.billance.models.Bill
import com.jeanquille.billance.repositories.BillRepository
import org.springframework.stereotype.Service

@Service
class BillService(val billRepository: BillRepository) {
    fun getAllBills(): MutableList<Bill> {
        return billRepository.findAll()
    }

    fun getBillsInParty(partyId: Long): MutableList<Bill> {
        return billRepository.findByPartyId(partyId)
    }
}