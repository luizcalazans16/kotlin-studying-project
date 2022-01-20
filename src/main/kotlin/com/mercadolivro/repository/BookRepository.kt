package com.mercadolivro.repository

import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.model.Book
import com.mercadolivro.model.Customer
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<Book, Int> {
    fun findByStatus(status: BookStatus): List<Book>
    fun findByCustomer(customer: Customer): List<Book>
}