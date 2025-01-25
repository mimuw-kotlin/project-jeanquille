package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findAllByPartyId(partyId: Long): MutableList<Member>
    fun findByAccountIdAndPartyId(accountId: UUID, partyId: Long): Member
}