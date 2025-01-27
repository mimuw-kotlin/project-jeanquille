package com.jeanquille.billance.controllers

import com.jeanquille.billance.dto.AccountPostDto
import com.jeanquille.billance.models.Account
import com.jeanquille.billance.models.LoginRequest
import com.jeanquille.billance.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class AccountController(private val accountService: AccountService) {

    private val lock = Any()

    @GetMapping("/account/{accountId}")
    fun getAccount(@PathVariable accountId: UUID): Account = accountService.getAccount(accountId)

    @PostMapping("/register")
    fun createAccount(@RequestBody accountPostDto: AccountPostDto) {
        synchronized(lock) {
            accountService.createAccount(accountPostDto.toAccount())
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): UUID {
        return accountService.login(loginRequest.username, loginRequest.password)
    }
}
