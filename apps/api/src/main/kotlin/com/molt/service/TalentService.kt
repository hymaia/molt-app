package com.molt.service

import com.molt.entity.Kind
import com.molt.entity.TalentEntity
import com.molt.repository.TalentRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class TalentService(private val talentRepository: TalentRepository) {

    @Transactional(readOnly = true)
    fun getTalent(id: Long): TalentEntity {
        val talent = talentRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Talent $id not found") }
        try {
            log.debug("Talent {} has {} reviews", id, talent.reviews.size)
        } catch (e: Exception) {
            log.warn("Could not load reviews for talent {}", id)
        }
        return talent
    }

    /**
     * Filters and sorts talents. Paging is left to the caller.
     */
    @Transactional(readOnly = true)
    fun search(q: String?, kind: Kind?, skill: String?, available: Boolean?, sort: String?): List<TalentEntity> {
        val all = if (kind != null) talentRepository.findByKind(kind) else talentRepository.findAll()
        val needle = q?.trim()?.lowercase()

        return all
            .filter { t ->
                needle == null || needle.isEmpty() ||
                    t.name!!.lowercase().contains(needle) ||
                    t.title!!.lowercase().contains(needle) ||
                    t.skills.any { it.lowercase().contains(needle) }
            }
            .filter { t ->
                skill == null || skill.isBlank() ||
                    t.skills.any { it.lowercase().contains(skill.trim().lowercase()) }
            }
            .filter { t -> available == null || t.isAvailable == available }
            .sortedWith(comparatorFor(sort))
    }

    private fun comparatorFor(sort: String?): Comparator<TalentEntity> {
        if (sort == "missions") {
            return compareByDescending<TalentEntity> { it.missions }.thenBy { it.id }
        }
        return compareByDescending<TalentEntity> { it.rating }.thenBy { it.id }
    }

    companion object {
        private val log = LoggerFactory.getLogger(TalentService::class.java)
    }
}
