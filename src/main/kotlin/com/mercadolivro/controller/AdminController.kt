package com.mercadolivro.controller

import com.mercadolivro.service.CustomerService
import lombok.extern.log4j.Log4j2
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
@Log4j2
class AdminController() {

    @GetMapping("/report")
    fun report(): String {
        return "This is a report"
    }
}