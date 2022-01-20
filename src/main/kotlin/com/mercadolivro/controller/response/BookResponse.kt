package com.mercadolivro.controller.response

import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.model.Customer
import java.math.BigDecimal
import javax.persistence.*

data class BookResponse(
    var id: Int? = null,

    var name: String,

    var price: BigDecimal,

    var customer: Customer? = null,

    var status: BookStatus? = null
)
