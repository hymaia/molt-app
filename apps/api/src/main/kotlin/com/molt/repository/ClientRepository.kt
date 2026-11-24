package com.molt.repository

import com.molt.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository : JpaRepository<ClientEntity, Long> {

    fun findByName(name: String): ClientEntity?
}
