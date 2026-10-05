package com.molt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission")
@Getter
@Setter
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 4000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mission_skills", joinColumns = @JoinColumn(name = "mission_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    private Integer durationDays;

    private Boolean remote;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC")
    private List<ProposalEntity> proposals = new ArrayList<>();

    @JsonIgnore
    public boolean canReceiveProposals() {
        return status == MissionStatus.OPEN;
    }
}
