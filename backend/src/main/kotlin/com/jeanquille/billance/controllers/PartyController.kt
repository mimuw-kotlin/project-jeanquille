package com.jeanquille.billance.controllers

import com.jeanquille.billance.dto.BillDto
import com.jeanquille.billance.models.Party
import com.jeanquille.billance.models.Transaction
import com.jeanquille.billance.services.PartyService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class PartyController(private val partyService: PartyService) {

    @GetMapping("/parties/{accountId}")
    fun getParties(@PathVariable accountId: UUID): List<Party> = partyService.getPartiesByAccountId(accountId)

    @GetMapping("/party/{partyId}")
    fun getParty(@PathVariable partyId: Long): Party = partyService.getPartyById(partyId)

    @DeleteMapping("/party/{partyId}")
    fun deleteParty(@PathVariable partyId: Long) {
        partyService.deleteParty(partyId)
    }

    @PostMapping("/party/{creatorId}")
    fun createParty(@PathVariable creatorId: UUID, @RequestBody json: Map<String, String>) {
        val partyName = json["name"] ?: throw IllegalArgumentException("Name is required")
        val party = Party(name = partyName)

        partyService.createParty(party, creatorId)
    }

    @PostMapping("/party/{partyId}/member/{accountId}")
    fun addMember(@PathVariable partyId: Long, @PathVariable accountId: UUID): Party {
        return partyService.addMember(partyId, accountId)
    }

    @PostMapping("/party/{partyId}/bill")
    fun addBill(@PathVariable partyId: Long, @RequestBody billDto: BillDto): Party {
        return partyService.addBill(billDto.toBill(partyId))
    }

    @PostMapping("/party/{partyId}/sumup")
    fun sumUp(@PathVariable partyId: Long): MutableList<Transaction> {
        return partyService.sumUpParty(partyId)
    }
}
