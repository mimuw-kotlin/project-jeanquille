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

    fun deleteBill(billId: Long) {
        billRepository.deleteById(billId)
    }

    fun updateBill(billId: Long, newBill: Bill) {
        val billToUpdate: Bill = billRepository.findById(billId).orElseThrow()
        billToUpdate.amount = newBill.amount
        billToUpdate.date = newBill.date
        billToUpdate.name = newBill.name
        billRepository.save(billToUpdate)
    }

    fun getBill(billId: Long): Bill = billRepository.findById(billId).orElseThrow()
}