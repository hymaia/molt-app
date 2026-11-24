package com.molt.dto

import com.molt.entity.ClientEntity
import com.molt.entity.MissionStatus
import java.time.LocalDate

open class MissionDto {
    var id: Long? = null
    var title: String? = null
    var client: ClientEntity? = null
    var skills: List<String>? = null
    var status: MissionStatus? = null
    var durationDays: Int? = null
    var remote: Boolean? = null
    var createdAt: LocalDate? = null
}
