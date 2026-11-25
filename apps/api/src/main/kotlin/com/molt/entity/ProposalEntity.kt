package com.molt.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "proposal")
class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    var mission: MissionEntity? = null

    @ManyToOne
    @JoinColumn(name = "talent_id")
    var talent: TalentEntity? = null

    var dailyRateCents: Long? = null

    @Column(length = 2000)
    var message: String? = null

    var createdAt: Instant? = null
}
