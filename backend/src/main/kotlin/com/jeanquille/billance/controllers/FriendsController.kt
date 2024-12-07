package com.jeanquille.billance.controllers

import com.jeanquille.billance.services.FriendsService
import org.springframework.web.bind.annotation.RestController

@RestController
class FriendsController(private val friendsService: FriendsService) {
}