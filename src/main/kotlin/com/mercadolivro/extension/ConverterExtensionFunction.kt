package com.mercadolivro.extension

import com.mercadolivro.controller.request.CreateBookRequest
import com.mercadolivro.controller.request.CreateCustomerRequest
import com.mercadolivro.controller.request.UpdateBookRequest
import com.mercadolivro.controller.request.UpdateCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.model.Book
import com.mercadolivro.model.Customer

fun CreateCustomerRequest.toCustomerEntity(): Customer {
    return Customer(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ATIVO)
}

fun UpdateCustomerRequest.toCustomerEntity(previousValue: Customer): Customer {
    return Customer(
        id = previousValue.id,
        name = this.name,
        email = this.email,
        status = previousValue.status)
}

fun CreateBookRequest.toBookEntity(customer: Customer): Book {
    return Book(
        name = this.name,
        price = this.price,
        status = BookStatus.ATIVO,
        customer = customer
    )
}

fun UpdateBookRequest.toBookEntity(previousValue: Book): Book {
    return Book(
        id = previousValue.id,
        name = this.name ?: previousValue.name,
        price = this.price ?: previousValue.price,
        status = previousValue.status,
        customer = previousValue.customer
    )
}

fun Customer.toResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun Book.toResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}