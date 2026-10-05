package com.molt.repository;

import com.molt.entity.MissionEntity;
import com.molt.entity.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    List<MissionEntity> findByStatusOrderByCreatedAtDescIdDesc(MissionStatus status);

    List<MissionEntity> findAllByOrderByCreatedAtDescIdDesc();
}
