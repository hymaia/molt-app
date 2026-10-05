package com.molt.controller;

import com.molt.dto.MissionDetailDto;
import com.molt.dto.MissionDto;
import com.molt.dto.ProposalDto;
import com.molt.dto.ProposalRequest;
import com.molt.entity.MissionEntity;
import com.molt.entity.MissionStatus;
import com.molt.entity.ProposalEntity;
import com.molt.exception.ApiException;
import com.molt.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private static final int MAX_MESSAGE_LENGTH = 2000;

    @Autowired
    private MissionService missionService;

    @GetMapping
    public List<MissionDto> list(@RequestParam(required = false) MissionStatus status) {
        return missionService.listMissions(status);
    }

    @GetMapping("/{id}")
    public MissionDetailDto get(@PathVariable Long id) {
        MissionEntity m = missionService.getMission(id);
        MissionDetailDto dto = new MissionDetailDto();
        dto.setId(m.getId());
        dto.setTitle(m.getTitle());
        dto.setDescription(m.getDescription());
        dto.setClient(m.getClient());
        dto.setSkills(new ArrayList<>(m.getSkills()));
        dto.setStatus(m.getStatus());
        dto.setDurationDays(m.getDurationDays());
        dto.setRemote(m.getRemote());
        dto.setCreatedAt(m.getCreatedAt());
        for (ProposalEntity p : m.getProposals()) {
            dto.getProposals().add(new ProposalDto(p.getId(), p.getTalent().getId(), p.getTalent().getName(),
                    p.getDailyRateCents(), p.getMessage(), p.getCreatedAt()));
        }
        return dto;
    }

    @PostMapping("/{id}/proposals")
    public ResponseEntity<ProposalDto> createProposal(@PathVariable Long id,
                                                      @Valid @RequestBody ProposalRequest request) {
        if (request.getMessage() != null && request.getMessage().length() > MAX_MESSAGE_LENGTH) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                    "message must be at most " + MAX_MESSAGE_LENGTH + " characters");
        }

        MissionEntity mission = missionService.getMission(id);
        if (mission.getStatus() == MissionStatus.CONTRACTED || mission.getStatus() == MissionStatus.DONE) {
            throw new ApiException(HttpStatus.CONFLICT, "MISSION_NOT_OPEN",
                    "Mission " + id + " is not open for proposals");
        }
        for (ProposalEntity existing : mission.getProposals()) {
            if (existing.getTalent().getId().equals(request.getTalentId())) {
                throw new ApiException(HttpStatus.CONFLICT, "DUPLICATE_PROPOSAL",
                        "Talent " + request.getTalentId() + " already proposed on mission " + id);
            }
        }

        return missionService.createProposal(id, request);
    }
}
