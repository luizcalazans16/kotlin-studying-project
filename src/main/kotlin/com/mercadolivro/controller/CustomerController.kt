package com.mercadolivro.controller

import com.mercadolivro.controller.request.CreateCustomerRequest
import com.mercadolivro.controller.request.UpdateCustomerRequest
import com.mercadolivro.extension.toCustomerEntity
import com.mercadolivro.model.Customer
import com.mercadolivro.service.CustomerService
import lombok.extern.log4j.Log4j2
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")
@Log4j2
class CustomerController(
    val customerService: CustomerService
) {

    @GetMapping
    fun getAll(@RequestParam name: String?): List<Customer> {
        return customerService.getAll(name)
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): Customer {
        return customerService.findById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody customer: CreateCustomerRequest) {
        customerService.create(customer.toCustomerEntity())
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody customer: UpdateCustomerRequest) {
        val savedCustomer = getCustomer(id)
        customerService.update(customer.toCustomerEntity(savedCustomer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
    }
}