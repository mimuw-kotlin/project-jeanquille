package com.jeanquille.billance.controllers

import com.jeanquille.billance.services.MemberService
import org.springframework.web.bind.annotation.*

@RestController
class MemberController(private val memberService: MemberService) {

    @DeleteMapping("/member/{memberId}")
    fun deleteMember(@PathVariable memberId: Long) {
        memberService.deleteMember(memberId)
    }
}
