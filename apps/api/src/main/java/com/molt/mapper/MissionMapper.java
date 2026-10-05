package com.molt.mapper;

import com.molt.dto.MissionDetailDto;
import com.molt.dto.MissionDto;
import com.molt.dto.ProposalDto;
import com.molt.entity.MissionEntity;
import com.molt.entity.ProposalEntity;

import java.util.ArrayList;

public final class MissionMapper {

    private MissionMapper() {
    }

    public static MissionDto toDto(MissionEntity m) {
        MissionDto dto = new MissionDto();
        copy(m, dto);
        return dto;
    }

    public static MissionDetailDto toDetailDto(MissionEntity m) {
        MissionDetailDto dto = new MissionDetailDto();
        copy(m, dto);
        dto.setDescription(m.getDescription());
        for (ProposalEntity p : m.getProposals()) {
            dto.getProposals().add(toProposalDto(p));
        }
        return dto;
    }

    public static ProposalDto toProposalDto(ProposalEntity p) {
        return new ProposalDto(p.getId(), p.getTalent().getId(), p.getTalent().getName(),
                p.getDailyRateCents(), p.getMessage(), p.getCreatedAt());
    }

    private static void copy(MissionEntity m, MissionDto dto) {
        dto.setId(m.getId());
        dto.setTitle(m.getTitle());
        dto.setClient(m.getClient());
        dto.setSkills(new ArrayList<>(m.getSkills()));
        dto.setStatus(m.getStatus());
        dto.setDurationDays(m.getDurationDays());
        dto.setRemote(m.getRemote());
        dto.setCreatedAt(m.getCreatedAt());
    }
}
