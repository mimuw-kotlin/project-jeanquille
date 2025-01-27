package com.jeanquille.billance.services

import com.jeanquille.billance.models.Bill
import com.jeanquille.billance.models.Member
import com.jeanquille.billance.repositories.BillRepository
import com.jeanquille.billance.repositories.MemberRepository
import org.springframework.stereotype.Service

@Service
class BillService(
    private val billRepository: BillRepository,
    private val memberRepository: MemberRepository
) {
    fun getAllBills(): MutableList<Bill> {
        return billRepository.findAll()
    }

    fun deleteBill(billId: Long) {
        val bill: Bill = billRepository.findById(billId).orElseThrow()
        val n: Int = bill.participants.size
        val amountPerPerson: Long = bill.amount / n

        val participants: MutableList<Member> = bill.participants.map {
            memberRepository.findByAccountIdAndPartyId(it.id!!, bill.party.id!!)
        }.toMutableList()

        participants.forEach {
            it.balance += amountPerPerson
            memberRepository.save(it)
        }

        val payer: Member = memberRepository.findByAccountIdAndPartyId(bill.payer.id!!, bill.party.id!!)
        payer.balance -= bill.amount
        memberRepository.save(payer)

        billRepository.deleteById(billId)
    }

    fun getBill(billId: Long): Bill = billRepository.findById(billId).orElseThrow()
}
