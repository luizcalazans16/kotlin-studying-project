package com.mercadolivro.controller.mapper

import com.mercadolivro.controller.request.CreatePurchaseRequest
import com.mercadolivro.model.Purchase
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request: CreatePurchaseRequest): Purchase {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)

        return Purchase(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}