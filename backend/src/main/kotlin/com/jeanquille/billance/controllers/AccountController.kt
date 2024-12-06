package com.jeanquille.billance.controllers

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping("/accounts")
    fun getAllAccounts(): MutableList<Account> = accountService.getAllAccounts()

    //TEST
    @PostMapping("/account/{username}")
    fun createAccount(@PathVariable username: String): Account {
        return accountService.createAccount(username)
    }
}