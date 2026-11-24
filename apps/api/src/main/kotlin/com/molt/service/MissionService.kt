package com.molt.service

import com.molt.dto.MissionDto
import com.molt.dto.ProposalDto
import com.molt.dto.ProposalRequest
import com.molt.entity.MissionEntity
import com.molt.entity.MissionStatus
import com.molt.entity.ProposalEntity
import com.molt.exception.ApiException
import com.molt.mapper.MissionMapper
import com.molt.repository.MissionRepository
import com.molt.repository.ProposalRepository
import com.molt.repository.TalentRepository
import com.molt.util.MoneyUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class MissionService {

    @Autowired
    private lateinit var missionRepository: MissionRepository

    @Autowired
    private lateinit var proposalRepository: ProposalRepository

    @Autowired
    private lateinit var talentRepository: TalentRepository

    fun listMissions(status: MissionStatus?): List<MissionDto> {
        val missions = if (status != null) {
            missionRepository.findByStatusOrderByCreatedAtDescIdDesc(status)
        } else {
            missionRepository.findAllByOrderByCreatedAtDescIdDesc()
        }
        val result = ArrayList<MissionDto>()
        for (m in missions) {
            val dto = MissionDto()
            dto.id = m.id
            dto.title = m.title
            dto.client = m.client
            dto.skills = ArrayList(m.skills)
            dto.status = m.status
            dto.durationDays = m.durationDays
            dto.remote = m.remote
            dto.createdAt = m.createdAt
            result.add(dto)
        }
        return result
    }

    fun getMission(id: Long): MissionEntity {
        return missionRepository.findById(id)
            .orElseThrow { ApiException.notFound("Mission $id not found") }
    }

    @Transactional
    fun createProposal(missionId: Long, request: ProposalRequest): ResponseEntity<ProposalDto> {
        if (request.message == null || request.message!!.length < 10) {
            throw ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "message is too short")
        }
        if (request.dailyRateCents == null || request.dailyRateCents!! <= 0) {
            throw ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "dailyRateCents must be positive")
        }

        val mission = missionRepository.findById(missionId)
            .orElseThrow { ApiException.notFound("Mission $missionId not found") }

        if (!mission.canReceiveProposals()) {
            throw ApiException(
                HttpStatus.CONFLICT, "MISSION_NOT_OPEN",
                "Mission $missionId is not open for proposals"
            )
        }

        val talent = talentRepository.findById(request.talentId!!).orElse(null)
        if (talent == null) {
            throw ApiException(
                HttpStatus.BAD_REQUEST, "UNKNOWN_TALENT",
                "Talent ${request.talentId} does not exist"
            )
        }

        if (proposalRepository.existsByMissionIdAndTalentId(missionId, talent.id!!)) {
            throw ApiException(
                HttpStatus.CONFLICT, "DUPLICATE_PROPOSAL",
                "Talent ${talent.id} already proposed on mission $missionId"
            )
        }

        var p = ProposalEntity()
        p.mission = mission
        p.talent = talent
        p.dailyRateCents = request.dailyRateCents
        p.message = request.message
        p.createdAt = Instant.now()
        p = proposalRepository.save(p)

        try {
            log.info(
                "New proposal on mission {} by {} at {}/day (usual rate {})", missionId, talent.name,
                MoneyUtils.formatCents(p.dailyRateCents!!), talent.displayRate
            )
        } catch (e: Exception) {
            log.info("New proposal on mission {} by {}", missionId, talent.name)
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(MissionMapper.toProposalDto(p))
    }

    companion object {
        private val log = LoggerFactory.getLogger(MissionService::class.java)
    }
}
