package com.jeanquille.billance.controllers

import com.jeanquille.billance.models.Bill
import com.jeanquille.billance.services.BillService
import org.springframework.web.bind.annotation.*

@RestController
class BillController(private val billService: BillService) {

    @GetMapping("/bills")
    fun getAllBills(): MutableList<Bill> = billService.getAllBills()

    @GetMapping("/bill/{billId}")
    fun getBill(@PathVariable billId: Long): Bill = billService.getBill(billId)

    @DeleteMapping("/bill/{billId}")
    fun deleteBill(@PathVariable billId: Long) {
        billService.deleteBill(billId)
    }
}