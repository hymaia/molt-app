package com.molt.repository

import com.molt.entity.ProposalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProposalRepository : JpaRepository<ProposalEntity, Long> {

    fun existsByMissionIdAndTalentId(missionId: Long, talentId: Long): Boolean
}
