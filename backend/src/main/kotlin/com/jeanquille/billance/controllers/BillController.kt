package com.jeanquille.billance.controllers

import com.jeanquille.billance.services.BillService
import org.springframework.web.bind.annotation.RestController

@RestController
class BillController(private val billService: BillService) {
}