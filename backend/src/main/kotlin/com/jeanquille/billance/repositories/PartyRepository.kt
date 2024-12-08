package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Party
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PartyRepository: JpaRepository<Party, Long> {
    fun findByMembersAccountId(accountId: UUID): MutableList<Party>
}