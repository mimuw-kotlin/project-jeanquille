package com.jeanquille.billance.controllers

import com.jeanquille.billance.dto.AccountPostDto
import com.jeanquille.billance.models.Account
import com.jeanquille.billance.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping("/accounts")
    fun getAllAccounts(): MutableList<Account> = accountService.getAllAccounts()

//    @PostMapping("/account/{username}")
//    fun createAccount(@PathVariable username: String): Account {
//        return accountService.createAccount(username)
//    }

    @GetMapping("/account/{accountId}")
    fun getAccount(@PathVariable accountId: UUID): Account = accountService.getAccount(accountId)

    @PostMapping("/account")
    fun createAccount(@RequestBody accountPostDto: AccountPostDto) {
        accountService.createAccount(accountPostDto.toAccount())
    }
}