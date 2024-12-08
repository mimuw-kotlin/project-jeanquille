package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.models.Bill
import com.jeanquille.billance.models.Member
import com.jeanquille.billance.models.Party
import com.jeanquille.billance.repositories.PartyRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import kotlin.math.ceil

@Service
class PartyService(val partyRepository: PartyRepository) {
    fun getAllParties(): MutableList<Party> {
        return partyRepository.findAll()
    }

    fun getPartiesByAccountId(accountId: UUID): MutableList<Party> {
        return partyRepository.findByMembersAccountId(accountId)
    }

    fun getPartyById(partyId: Long): Party {
        return partyRepository.findById(partyId).orElseThrow()
    }

    fun createParty(party: Party, creator: Account): Party {
        val member = Member()
        member.account.id = creator.id
        party.members.add(member)

        return partyRepository.save(party)
    }

    fun updateParty(partyId: Long, newPartyName: String): Party {
        val party = partyRepository.findById(partyId).orElseThrow()
        party.name = newPartyName
        return partyRepository.save(party)
    }

    fun deleteParty(partyId: Long) {
        partyRepository.deleteById(partyId)
    }

    fun addMember(partyId: Long, accountId: UUID) : Party {
        val party = partyRepository.findById(partyId).orElseThrow()
        if (party.members.any { it.account.id == accountId }) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Account is already a member of this party")
        }
        val member = Member()
        member.account.id = accountId
        party.members.add(member)

        return partyRepository.save(party)
    }

    fun addBill(bill: Bill): Party {
        val n: Int = bill.participants.size
        val amountPerPerson: Long = ceil(bill.amount.toDouble() / n).toLong()
        val totalAmount: Long = amountPerPerson * n
        bill.amount = totalAmount

        val partyMembers: MutableList<Member> = memberRepository.findByPartyId(bill.party.id!!)
        return Party() //TODO: Implement further
    }
}