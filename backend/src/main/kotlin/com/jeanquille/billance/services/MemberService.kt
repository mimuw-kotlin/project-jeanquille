package com.jeanquille.billance.services

import com.jeanquille.billance.models.Member
import com.jeanquille.billance.repositories.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(val memberRepository: MemberRepository) {
    fun getMembersInParty(partyId: Long): MutableList<Member> {
        return memberRepository.findAllByPartyId(partyId)
    }
}