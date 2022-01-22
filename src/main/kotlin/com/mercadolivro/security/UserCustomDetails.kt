package com.mercadolivro.security

import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.model.Customer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    val customer: Customer
) : UserDetails {

    val id: Int = customer.id!!

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return customer.roles.map {
            SimpleGrantedAuthority(it.description)
        }.toMutableList()
    }

    override fun getPassword(): String = customer.password

    override fun getUsername(): String = customer.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = customer.status == CustomerStatus.ATIVO
}