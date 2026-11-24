package com.molt.repository

import com.molt.entity.MissionEntity
import com.molt.entity.MissionStatus
import org.springframework.data.jpa.repository.JpaRepository

interface MissionRepository : JpaRepository<MissionEntity, Long> {

    fun findByStatusOrderByCreatedAtDescIdDesc(status: MissionStatus): List<MissionEntity>

    fun findAllByOrderByCreatedAtDescIdDesc(): List<MissionEntity>
}
