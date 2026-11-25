package com.molt.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "agent_profile")
@JsonInclude(JsonInclude.Include.ALWAYS)
class AgentProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long? = null

    var model: String? = null

    @ElementCollection
    @CollectionTable(name = "agent_profile_tools", joinColumns = [JoinColumn(name = "agent_profile_id")])
    @Column(name = "tool")
    var tools: MutableList<String> = ArrayList()

    @ManyToOne
    @JoinColumn(name = "operator_id")
    @JsonIgnore
    var operator: TalentEntity? = null

    @get:JsonProperty("operator")
    val operatorRef: Map<String, Any?>?
        get() {
            val operator = operator ?: return null
            val ref = LinkedHashMap<String, Any?>()
            ref["id"] = operator.id
            ref["name"] = operator.name
            return ref
        }
}
