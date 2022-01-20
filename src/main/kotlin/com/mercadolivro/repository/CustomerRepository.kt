package com.mercadolivro.repository

import com.mercadolivro.model.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<Customer, Int> {

    fun findByName(name: String): List<Customer>
}