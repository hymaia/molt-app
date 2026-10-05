package com.molt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProposalRequest {

    @NotNull
    private Long talentId;

    @NotNull
    @Min(1)
    private Long dailyRateCents;

    @NotNull
    @Size(min = 10, max = 2000)
    private String message;
}
