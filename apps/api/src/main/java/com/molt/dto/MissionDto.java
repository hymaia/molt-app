package com.molt.dto;

import com.molt.entity.ClientEntity;
import com.molt.entity.MissionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MissionDto {
    private Long id;
    private String title;
    private ClientEntity client;
    private List<String> skills;
    private MissionStatus status;
    private Integer durationDays;
    private Boolean remote;
    private LocalDate createdAt;
}
