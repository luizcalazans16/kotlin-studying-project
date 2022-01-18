package com.mercadolivro.extension

import com.mercadolivro.controller.request.CreateCustomerRequest
import com.mercadolivro.controller.request.UpdateCustomerRequest
import com.mercadolivro.model.Customer

fun CreateCustomerRequest.toCustomer(): Customer {
    return Customer(name = this.name, email = this.email)
}
fun UpdateCustomerRequest.toCustomer(id: Int): Customer {
    return Customer(id = id, name = this.name, email = this.email)
}