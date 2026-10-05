package com.molt.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MissionDetailDto extends MissionDto {
    private String description;
    private List<ProposalDto> proposals = new ArrayList<>();
}
