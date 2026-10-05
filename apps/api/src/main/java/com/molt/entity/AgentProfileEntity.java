package com.molt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "agent_profile")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AgentProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String model;

    @ElementCollection
    @CollectionTable(name = "agent_profile_tools", joinColumns = @JoinColumn(name = "agent_profile_id"))
    @Column(name = "tool")
    private List<String> tools = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "operator_id")
    @JsonIgnore
    private TalentEntity operator;

    @JsonProperty("operator")
    public Map<String, Object> getOperatorRef() {
        if (operator == null) {
            return null;
        }
        Map<String, Object> ref = new LinkedHashMap<>();
        ref.put("id", operator.getId());
        ref.put("name", operator.getName());
        return ref;
    }
}
