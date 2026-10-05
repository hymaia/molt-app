package com.molt.repository;

import com.molt.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    ClientEntity findByName(String name);
}
