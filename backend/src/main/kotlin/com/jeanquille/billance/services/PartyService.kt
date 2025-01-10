package com.jeanquille.billance.services

import com.jeanquille.billance.models.*
import com.jeanquille.billance.repositories.BillRepository
import com.jeanquille.billance.repositories.MemberRepository
import com.jeanquille.billance.repositories.PartyRepository
import com.jeanquille.billance.repositories.TransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import kotlin.math.ceil

@Service
class PartyService(
    private val partyRepository: PartyRepository,
    private val memberRepository: MemberRepository,
    private val billRepository: BillRepository,
    private val transactionRepository: TransactionRepository
) {
    fun getAllParties(): MutableList<Party> {
        return partyRepository.findAll()
    }

    fun getPartiesByAccountId(accountId: UUID): MutableList<Party> {
        return partyRepository.findByMembersAccountId(accountId)
    }

    fun getPartyById(partyId: Long): Party {
        return partyRepository.findById(partyId).orElseThrow()
    }

    fun createParty(party: Party, creatorId: UUID): Party {
        val member = Member(party=party, account=Account(id=creatorId))
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
        val member = Member(party=party, account=Account(id=accountId))
        party.members.add(member)
        return partyRepository.save(party)
    }

    fun addBill(bill: Bill): Party {
        val n: Int = bill.participants.size
        val amountPerPerson: Long = ceil(bill.amount.toDouble() / n).toLong()
        val totalAmount: Long = amountPerPerson * n
        bill.amount = totalAmount

        val participants: MutableList<Member> = bill.participants.map {
            memberRepository.findByAccountId(it.id!!)
        }.toMutableList()

        participants.forEach {
            it.balance -= amountPerPerson
            memberRepository.save(it)
        }

        val payer: Member = memberRepository.findByAccountId(bill.payer.id!!)
        payer.balance += totalAmount
        memberRepository.save(payer)

        billRepository.save(bill)
        return partyRepository.findById(bill.party.id!!).orElseThrow()
    }

    fun sumUpParty(partyId: Long): MutableList<Transaction> {
        val party: Party = partyRepository.findById(partyId).orElseThrow()
        transactionRepository.deleteByPartyId(partyId)

        val members: MutableList<Member> = memberRepository.findAllByPartyId(partyId)
            .filter { it.balance != 0L }.toMutableList()

        val split = recursiveSplit(members)
        return makeTransactions(split, party)
    }

    private fun makeTransactions(split: MutableList<MutableList<Member>>, party: Party): MutableList<Transaction> {
        val transactions: MutableList<Transaction> = mutableListOf()
        for (group in split) {
            val payers = group.filter { it.balance < 0 }.sortedBy { it.balance }.reversed()
            val receivers = group.filter { it.balance > 0 }.sortedBy { it.balance }

            var payerIndex = payers.size - 1
            var receiverIndex = receivers.size - 1

            while (payerIndex >= 0 && receiverIndex >= 0) {
                val payer = payers[payerIndex]
                val receiver = receivers[receiverIndex]
                val amount = minOf(-payer.balance, receiver.balance)

                payer.balance += amount
                receiver.balance -= amount

                transactions.add(Transaction(party=party, payer=payer.account, receiver=receiver.account, amount=amount))

                if (payer.balance == 0L) {
                    payerIndex--
                }
                if (receiver.balance == 0L) {
                    receiverIndex--
                }
            }


        }
        return transactionRepository.saveAll(transactions)
    }

    //assumes that balances sum up to 0
    private fun recursiveSplit(members: MutableList<Member>): MutableList<MutableList<Member>> {
        val n = members.size
        if (n == 0) {
            return mutableListOf(mutableListOf())
        }
        val maxSum = members.filter { it.balance > 0 }.sumOf { it.balance.toInt() }
        val m = maxSum + 1

        if (n * m  > 10000000) {
            return mutableListOf(members)
        }

        members.sortBy { -it.balance }

        val dp = Array(n) { Array(m) { false } }

        dp[0][members[0].balance.toInt()] = true

        for (i in 1 until n) {
            for (j in 0 until m) {
                val k = j - members[i].balance.toInt()
                dp[i][j] = dp[i-1][j] || (k in 0..<m && dp[i-1][k])
            }
        }
        if (!dp[n-1][0]) {
            throw IllegalStateException("Balances do not sum up to 0")
        }

        val res = mutableListOf<MutableList<Member>>()
        val subRes = mutableListOf<Member>()
        var subNotAll = false
        var i = n - 1
        var j = 0

        while (i > 0) {
            if (dp[i-1][j]) {
                subNotAll = true
            } else {
                subRes.add(members[i])
                j -= members[i].balance.toInt()
                i -= 1
            }
            i--
        }

        subRes.add(members[0])

        if (subNotAll) {
            res.addAll(recursiveSplit(subRes))
            res.addAll(recursiveSplit(members.filter { it !in subRes }.toMutableList()))
            return res
        }

        return mutableListOf(subRes)
    }
}