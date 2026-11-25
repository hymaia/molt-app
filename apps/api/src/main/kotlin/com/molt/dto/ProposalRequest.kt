package com.molt.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProposalRequest(

    @field:NotNull
    var talentId: Long? = null,

    @field:NotNull
    @field:Min(1)
    var dailyRateCents: Long? = null,

    @field:NotNull
    @field:Size(min = 10, max = 2000)
    var message: String? = null,
)
