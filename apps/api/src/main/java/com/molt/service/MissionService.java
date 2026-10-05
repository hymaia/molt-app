package com.molt.service;

import com.molt.dto.MissionDto;
import com.molt.dto.ProposalDto;
import com.molt.dto.ProposalRequest;
import com.molt.entity.MissionEntity;
import com.molt.entity.MissionStatus;
import com.molt.entity.ProposalEntity;
import com.molt.entity.TalentEntity;
import com.molt.exception.ApiException;
import com.molt.mapper.MissionMapper;
import com.molt.repository.MissionRepository;
import com.molt.repository.ProposalRepository;
import com.molt.repository.TalentRepository;
import com.molt.util.MoneyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private TalentRepository talentRepository;

    public List<MissionDto> listMissions(MissionStatus status) {
        List<MissionEntity> missions;
        if (status != null) {
            missions = missionRepository.findByStatusOrderByCreatedAtDescIdDesc(status);
        } else {
            missions = missionRepository.findAllByOrderByCreatedAtDescIdDesc();
        }
        List<MissionDto> result = new ArrayList<>();
        for (MissionEntity m : missions) {
            MissionDto dto = new MissionDto();
            dto.setId(m.getId());
            dto.setTitle(m.getTitle());
            dto.setClient(m.getClient());
            dto.setSkills(new ArrayList<>(m.getSkills()));
            dto.setStatus(m.getStatus());
            dto.setDurationDays(m.getDurationDays());
            dto.setRemote(m.getRemote());
            dto.setCreatedAt(m.getCreatedAt());
            result.add(dto);
        }
        return result;
    }

    public MissionEntity getMission(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Mission " + id + " not found"));
    }

    @Transactional
    public ResponseEntity<ProposalDto> createProposal(Long missionId, ProposalRequest request) {
        if (request.getMessage() == null || request.getMessage().length() < 10) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "message is too short");
        }
        if (request.getDailyRateCents() == null || request.getDailyRateCents() <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "dailyRateCents must be positive");
        }

        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> ApiException.notFound("Mission " + missionId + " not found"));

        if (!mission.canReceiveProposals()) {
            throw new ApiException(HttpStatus.CONFLICT, "MISSION_NOT_OPEN",
                    "Mission " + missionId + " is not open for proposals");
        }

        TalentEntity talent = talentRepository.findById(request.getTalentId()).orElse(null);
        if (talent == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "UNKNOWN_TALENT",
                    "Talent " + request.getTalentId() + " does not exist");
        }

        if (proposalRepository.existsByMissionIdAndTalentId(missionId, talent.getId())) {
            throw new ApiException(HttpStatus.CONFLICT, "DUPLICATE_PROPOSAL",
                    "Talent " + talent.getId() + " already proposed on mission " + missionId);
        }

        ProposalEntity p = new ProposalEntity();
        p.setMission(mission);
        p.setTalent(talent);
        p.setDailyRateCents(request.getDailyRateCents());
        p.setMessage(request.getMessage());
        p.setCreatedAt(Instant.now());
        p = proposalRepository.save(p);

        try {
            log.info("New proposal on mission {} by {} at {}/day (usual rate {})", missionId, talent.getName(),
                    MoneyUtils.formatCents(p.getDailyRateCents()), talent.getDisplayRate());
        } catch (Exception e) {
            log.info("New proposal on mission {} by {}", missionId, talent.getName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(MissionMapper.toProposalDto(p));
    }
}
