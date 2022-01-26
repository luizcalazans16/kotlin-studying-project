package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.CreateCustomerRequest
import com.mercadolivro.controller.request.UpdateCustomerRequest
import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserCustomDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return all customers`() {
        val customer1 = customerRepository.save(buildCustomer())
        val customer2 = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status.name))
            .andExpect(jsonPath("$[1].id").value(customer2.id))
            .andExpect(jsonPath("$[1].name").value(customer2.name))
            .andExpect(jsonPath("$[1].email").value(customer2.email))
            .andExpect(jsonPath("$[1].status").value(customer2.status.name))
    }

    @Test
    fun `should return customers by name when get all`() {
        val customer = customerRepository.save(buildCustomer(name = "Luiz"))
        customerRepository.save(buildCustomer(name = "Gustavo"))

        mockMvc.perform(get("/customers?name=Lu"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(customer.id))
            .andExpect(jsonPath("$[0].name").value(customer.name))
            .andExpect(jsonPath("$[0].email").value(customer.email))
            .andExpect(jsonPath("$[0].status").value(customer.status.name))
    }

    @Nested
    inner class `create Customer` {

        @Test
        fun `should create customer`() {
            val request = CreateCustomerRequest(
                name = "Fake Name",
                email = "${UUID.randomUUID()}@email.com",
                password = "123456"
            )

            mockMvc.perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)

            val customers = customerRepository.findAll().toList()
            assertEquals(1, customers.size)
            assertEquals(request.name, customers[0].name)
            assertEquals(request.email, customers[0].email)
        }

        @Test
        fun `should throw error when create customer has invalid information`() {
            val request = CreateCustomerRequest(
                name = "",
                email = "${UUID.randomUUID()}@email.com",
                password = "123456"
            )

            mockMvc.perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isUnprocessableEntity)
                .andExpect(jsonPath("$.httpCode").value(422))
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andExpect(jsonPath("$.internalCode").value("ML-001"))
        }
    }

    @Nested
    inner class `Get Customer By Id` {

        @Test
        fun `should return customer when get user by id`() {
            val customer = customerRepository.save(buildCustomer())

            mockMvc.perform(
                get("/customers/${customer.id}")
                    .with(user(UserCustomDetails(customer)))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(customer.id))
                .andExpect(jsonPath("$.name").value(customer.name))
                .andExpect(jsonPath("$.email").value(customer.email))
                .andExpect(jsonPath("$.status").value(customer.status.name))
        }

        @Test
        fun `should return forbidden when user does not have the same id`() {
            val customer = customerRepository.save(buildCustomer())

            mockMvc.perform(get("/customers/0").with(user(UserCustomDetails(customer))))
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.httpCode").value(403))
                .andExpect(jsonPath("$.message").value("Access Denied"))
                .andExpect(jsonPath("$.internalCode").value("ML-000"))
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `should return customer when user is admin`() {
            val customer = customerRepository.save(buildCustomer())

            mockMvc.perform(
                get("/customers/${customer.id}")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(customer.id))
                .andExpect(jsonPath("$.name").value(customer.name))
                .andExpect(jsonPath("$.email").value(customer.email))
                .andExpect(jsonPath("$.status").value(customer.status.name))
        }
    }

    @Nested
    inner class `Update Customer` {

        @Test
        fun `should update customer`() {
            val customer = customerRepository.save(buildCustomer())
            val request = UpdateCustomerRequest(
                name = "Luiz",
                email = "emailUpdate@email.com"
            )

            mockMvc.perform(
                put("/customers/${customer.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isNoContent)

            val customers = customerRepository.findAll().toList()
            assertEquals(1, customers.size)
            assertEquals(request.name, customers[0].name)
            assertEquals(request.email, customers[0].email)
        }

        @Test
        fun `should throw error when update customer has invalid information`() {
            val request = UpdateCustomerRequest(
                name = "",
                email = "${UUID.randomUUID()}@email.com",
            )

            mockMvc.perform(
                put("/customers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isUnprocessableEntity)
                .andExpect(jsonPath("$.httpCode").value(422))
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andExpect(jsonPath("$.internalCode").value("ML-001"))
        }

        @Test
        fun `should return not found  update customer does not exist`() {
            val request = UpdateCustomerRequest(
                name = "Luiz",
                email = "${UUID.randomUUID()}@email.com",
            )

            mockMvc.perform(
                put("/customers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.httpCode").value(404))
                .andExpect(jsonPath("$.message").value("Customer [1] does not exist"))
                .andExpect(jsonPath("$.internalCode").value("ML-201"))
        }
    }

    @Nested
    inner class `Delete Customer` {

        @Test
        fun `should delete customer`() {
            val customer = customerRepository.save(buildCustomer())

            mockMvc.perform(
                delete("/customers/${customer.id}")
            )
                .andExpect(status().isNoContent)

            val deletedCustomer = customerRepository.findById(customer.id!!)
            assertEquals(CustomerStatus.INATIVO, deletedCustomer.get().status)
        }

        @Test
        fun `should return not found when delete customer does not exist`() {
            mockMvc.perform(
                delete("/customers/1")
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.httpCode").value(404))
                .andExpect(jsonPath("$.message").value("Customer [1] does not exist"))
                .andExpect(jsonPath("$.internalCode").value("ML-201"))

        }
    }

}