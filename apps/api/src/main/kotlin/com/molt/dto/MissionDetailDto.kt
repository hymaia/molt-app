package com.molt.dto

class MissionDetailDto : MissionDto() {
    var description: String? = null
    var proposals: MutableList<ProposalDto> = ArrayList()
}
