package com.mercadolivro.model

import com.mercadolivro.emuns.CustomerStatus
import javax.persistence.*

@Entity(name = "customer")
@Table(name = "customer")
data class Customer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus
)