package com.mercadolivro.model

import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.emuns.RolesEnum
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
    var status: CustomerStatus,

    @Column
    var password: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "customer_roles",
        joinColumns = [JoinColumn(name = "customer_id")]
    )
    @ElementCollection(
        targetClass = RolesEnum::class,
        fetch = FetchType.EAGER
    )
    var roles: Set<RolesEnum> = setOf()
)