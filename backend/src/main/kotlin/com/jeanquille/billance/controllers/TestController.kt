package com.jeanquille.billance.controllers

import com.jeanquille.billance.models.TestModel
import com.jeanquille.billance.services.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(val service: TestService) {
    @GetMapping
    fun xddd(): MutableList<TestModel> {
        return service.xddd()
    }
}