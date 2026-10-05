package com.molt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "proposal")
@Getter
@Setter
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private MissionEntity mission;

    @ManyToOne
    @JoinColumn(name = "talent_id")
    private TalentEntity talent;

    private Long dailyRateCents;

    @Column(length = 2000)
    private String message;

    private Instant createdAt;
}
