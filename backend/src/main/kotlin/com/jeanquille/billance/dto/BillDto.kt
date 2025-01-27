package com.jeanquille.billance.dto

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.models.Bill
import com.jeanquille.billance.models.Party
import java.util.*

data class BillDto(
    val name: String,
    val amount: Long,
    val payerId: UUID,
    val participantsIds: List<UUID>
) {
    fun toBill(partyId: Long): Bill {
        return Bill(
            name = name,
            amount = amount,
            party = Party(partyId),
            payer = Account(payerId),
            participants = participantsIds.map { Account(it) }.toMutableList()
        )
    }
}
