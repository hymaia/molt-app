package com.molt.controller

import com.molt.dto.MissionDetailDto
import com.molt.dto.MissionDto
import com.molt.dto.ProposalDto
import com.molt.dto.ProposalRequest
import com.molt.entity.MissionStatus
import com.molt.exception.ApiException
import com.molt.service.MissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/missions")
class MissionController {

    @Autowired
    private lateinit var missionService: MissionService

    @GetMapping
    fun list(@RequestParam(required = false) status: MissionStatus?): List<MissionDto> {
        return missionService.listMissions(status)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): MissionDetailDto {
        val m = missionService.getMission(id)
        val dto = MissionDetailDto()
        dto.id = m.id
        dto.title = m.title
        dto.description = m.description
        dto.client = m.client
        dto.skills = ArrayList(m.skills)
        dto.status = m.status
        dto.durationDays = m.durationDays
        dto.remote = m.remote
        dto.createdAt = m.createdAt
        for (p in m.proposals) {
            dto.proposals.add(
                ProposalDto(
                    p.id, p.talent!!.id, p.talent!!.name,
                    p.dailyRateCents, p.message, p.createdAt
                )
            )
        }
        return dto
    }

    @PostMapping("/{id}/proposals")
    fun createProposal(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProposalRequest,
    ): ResponseEntity<ProposalDto> {
        if (request.message != null && request.message!!.length > MAX_MESSAGE_LENGTH) {
            throw ApiException(
                HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "message must be at most $MAX_MESSAGE_LENGTH characters"
            )
        }

        val mission = missionService.getMission(id)
        if (mission.status == MissionStatus.CONTRACTED || mission.status == MissionStatus.DONE) {
            throw ApiException(
                HttpStatus.CONFLICT, "MISSION_NOT_OPEN",
                "Mission $id is not open for proposals"
            )
        }
        for (existing in mission.proposals) {
            if (existing.talent!!.id == request.talentId) {
                throw ApiException(
                    HttpStatus.CONFLICT, "DUPLICATE_PROPOSAL",
                    "Talent ${request.talentId} already proposed on mission $id"
                )
            }
        }

        return missionService.createProposal(id, request)
    }

    companion object {
        private const val MAX_MESSAGE_LENGTH = 2000
    }
}
