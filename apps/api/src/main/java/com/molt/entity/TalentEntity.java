package com.molt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "talent")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TalentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Kind kind;

    private String name;

    private String title;

    private String location;

    @JsonProperty("avatarUrl")
    private String avatar;

    @JsonIgnore
    @Column(length = 1000)
    private String skillsCsv;

    @JsonProperty("dailyRateCents")
    private Long rateCents;

    private Double rating;

    @JsonProperty("missionCount")
    private Integer missions;

    @JsonIgnore
    private boolean isAvailable;

    @Column(length = 4000)
    private String bio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "agent_profile_id")
    private AgentProfileEntity agent;

    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonProperty("skills")
    public List<String> getSkills() {
        if (skillsCsv == null || skillsCsv.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skillsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public void setSkills(List<String> skills) {
        this.skillsCsv = skills == null ? "" : String.join(",", skills);
    }

    @JsonProperty("available")
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @JsonIgnore
    public boolean isExpensive() {
        return rateCents != null && rateCents > 120000;
    }

    @JsonIgnore
    public String getDisplayRate() {
        if (rateCents == null) {
            return "";
        }
        return String.format("%,d €", rateCents / 100).replace(',', ' ');
    }

    public void addReview(ReviewEntity review) {
        review.setTalent(this);
        reviews.add(review);
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
