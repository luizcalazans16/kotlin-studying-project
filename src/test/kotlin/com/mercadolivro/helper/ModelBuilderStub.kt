package com.mercadolivro.helper

import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.emuns.RolesEnum
import com.mercadolivro.model.Book
import com.mercadolivro.model.Customer
import com.mercadolivro.model.Purchase
import java.math.BigDecimal
import java.util.*

fun buildCustomer(
    id: Int? = null,
    name: String = "Customer name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "password"

) = Customer(
    id = id,
    name = name,
    email = email,
    status = CustomerStatus.ATIVO,
    password = password,
    roles = setOf(RolesEnum.CUSTOMER)
)

fun buildPurchase(
    id: Int? = null,
    customer: Customer = buildCustomer(),
    books: MutableList<Book> = mutableListOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN
) = Purchase(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)