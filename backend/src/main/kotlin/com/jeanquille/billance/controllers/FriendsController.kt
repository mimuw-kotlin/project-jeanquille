package com.jeanquille.billance.controllers

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.services.FriendsService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class FriendsController(private val friendsService: FriendsService) {
    @GetMapping("/friends/{accountId}")
    fun getFriends(@PathVariable accountId: UUID): MutableList<Account> {
        return friendsService.getFriends(accountId)
    }

    @PostMapping("/friends/{accountId}/add")
    fun addFriend(@PathVariable accountId: UUID, @RequestBody json: Map<String, String>) {
        friendsService.addFriend(accountId, json["friendUsername"]!!)
    }

    @DeleteMapping("/friends/{accountId}/unfriend/{friendId}")
    fun unfriend(@PathVariable accountId: UUID, @PathVariable friendId: UUID) {
        friendsService.unfriend(accountId, friendId)
    }
}