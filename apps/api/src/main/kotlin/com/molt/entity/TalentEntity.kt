package com.molt.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.OrderBy
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "talent")
@JsonInclude(JsonInclude.Include.ALWAYS)
class TalentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    var kind: Kind? = null

    var name: String? = null

    var title: String? = null

    var location: String? = null

    @JsonProperty("avatarUrl")
    var avatar: String? = null

    @JsonIgnore
    @Column(length = 1000)
    var skillsCsv: String? = null

    @JsonProperty("dailyRateCents")
    var rateCents: Long? = null

    var rating: Double? = null

    @JsonProperty("missionCount")
    var missions: Int? = null

    @JsonIgnore
    @get:JsonProperty("available")
    var isAvailable: Boolean = false

    @Column(length = 4000)
    var bio: String? = null

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "agent_profile_id")
    var agent: AgentProfileEntity? = null

    @OneToMany(mappedBy = "talent", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("createdAt DESC")
    var reviews: MutableList<ReviewEntity> = ArrayList()

    @JsonIgnore
    var createdAt: LocalDateTime? = null

    @JsonIgnore
    var updatedAt: LocalDateTime? = null

    @get:JsonProperty("skills")
    var skills: List<String>
        get() {
            val csv = skillsCsv
            if (csv == null || csv.isBlank()) {
                return ArrayList()
            }
            return csv.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
        set(value) {
            skillsCsv = value.joinToString(",")
        }

    @get:JsonIgnore
    val isExpensive: Boolean
        get() = rateCents != null && rateCents!! > 120000

    @get:JsonIgnore
    val displayRate: String
        get() {
            if (rateCents == null) {
                return ""
            }
            return String.format("%,d €", rateCents!! / 100).replace(',', ' ')
        }

    fun addReview(review: ReviewEntity) {
        review.talent = this
        reviews.add(review)
    }

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        updatedAt = createdAt
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
