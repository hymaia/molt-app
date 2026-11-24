package com.molt.mapper

import com.molt.dto.MissionDetailDto
import com.molt.dto.MissionDto
import com.molt.dto.ProposalDto
import com.molt.entity.MissionEntity
import com.molt.entity.ProposalEntity

object MissionMapper {

    fun toDto(m: MissionEntity): MissionDto {
        val dto = MissionDto()
        copy(m, dto)
        return dto
    }

    fun toDetailDto(m: MissionEntity): MissionDetailDto {
        val dto = MissionDetailDto()
        copy(m, dto)
        dto.description = m.description
        for (p in m.proposals) {
            dto.proposals.add(toProposalDto(p))
        }
        return dto
    }

    fun toProposalDto(p: ProposalEntity): ProposalDto {
        return ProposalDto(p.id, p.talent!!.id, p.talent!!.name, p.dailyRateCents, p.message, p.createdAt)
    }

    private fun copy(m: MissionEntity, dto: MissionDto) {
        dto.id = m.id
        dto.title = m.title
        dto.client = m.client
        dto.skills = ArrayList(m.skills)
        dto.status = m.status
        dto.durationDays = m.durationDays
        dto.remote = m.remote
        dto.createdAt = m.createdAt
    }
}
