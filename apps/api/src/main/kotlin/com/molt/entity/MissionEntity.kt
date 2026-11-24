package com.molt.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "mission")
class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var title: String? = null

    @Column(length = 4000)
    var description: String? = null

    @ManyToOne
    @JoinColumn(name = "client_id")
    var client: ClientEntity? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mission_skills", joinColumns = [JoinColumn(name = "mission_id")])
    @Column(name = "skill")
    var skills: MutableList<String> = ArrayList()

    @Enumerated(EnumType.STRING)
    var status: MissionStatus? = null

    var durationDays: Int? = null

    var remote: Boolean? = null

    var createdAt: LocalDate? = null

    @OneToMany(mappedBy = "mission", cascade = [CascadeType.ALL])
    @OrderBy("createdAt ASC")
    var proposals: MutableList<ProposalEntity> = ArrayList()

    @JsonIgnore
    fun canReceiveProposals(): Boolean = status == MissionStatus.OPEN
}
