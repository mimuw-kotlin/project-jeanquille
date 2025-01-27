package com.jeanquille.billance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BillanceApplication

fun main(args: Array<String>) {
    runApplication<BillanceApplication>(*args)
}
