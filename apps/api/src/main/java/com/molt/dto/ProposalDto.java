package com.molt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {
    private Long id;
    private Long talentId;
    private String talentName;
    private Long dailyRateCents;
    private String message;
    private Instant createdAt;
}
