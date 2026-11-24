package com.molt.dto

import java.time.Instant

data class ProposalDto(
    var id: Long? = null,
    var talentId: Long? = null,
    var talentName: String? = null,
    var dailyRateCents: Long? = null,
    var message: String? = null,
    var createdAt: Instant? = null,
)
