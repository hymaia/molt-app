package com.molt.repository;

import com.molt.entity.ProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {

    boolean existsByMissionIdAndTalentId(Long missionId, Long talentId);
}
