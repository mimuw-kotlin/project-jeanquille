package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByPartyId(partyId: Long): MutableList<Member>
}