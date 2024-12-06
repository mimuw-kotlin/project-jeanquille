package com.jeanquille.billance.services

import com.jeanquille.billance.models.Party
import com.jeanquille.billance.repositories.PartyRepository
import org.springframework.stereotype.Service

@Service
class PartyService(val partyRepository: PartyRepository) {
    fun getAllParties(): MutableList<Party> {
        return partyRepository.findAll()
    }
}