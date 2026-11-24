package com.molt.controller

import com.molt.entity.Kind
import com.molt.entity.TalentEntity
import com.molt.exception.ApiException
import com.molt.repository.TalentRepository
import com.molt.service.TalentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.min

@RestController
@RequestMapping("/talents")
class TalentController {

    @Autowired
    private lateinit var talentRepository: TalentRepository

    @Autowired
    private lateinit var talentService: TalentService

    @GetMapping
    fun search(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) kind: Kind?,
        @RequestParam(required = false) skill: String?,
        @RequestParam(required = false) available: Boolean?,
        @RequestParam(required = false, defaultValue = "relevance") sort: String?,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "12") size: Int,
    ): Map<String, Any> {
        if (page < 0) {
            throw ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "page must be >= 0")
        }
        if (size < 1 || size > 50) {
            throw ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "size must be between 1 and 50")
        }

        val all = if (kind != null) talentRepository.findByKind(kind) else talentRepository.findAll()
        val filtered = ArrayList<TalentEntity>()

        for (t in all) {
            if (q != null && !q.isBlank()) {
                val needle = q.trim().lowercase()
                var match = false
                if (t.name != null && t.name!!.lowercase().contains(needle)) {
                    match = true
                } else if (t.title != null && t.title!!.lowercase().contains(needle)) {
                    match = true
                } else {
                    for (s in t.skills) {
                        if (s.lowercase().contains(needle)) {
                            match = true
                            break
                        }
                    }
                }
                if (!match) {
                    continue
                }
            }
            if (kind != null) {
                if (t.kind != kind) {
                    continue
                }
            }
            if (skill != null && !skill.isBlank()) {
                var hasSkill = false
                for (s in t.skills) {
                    if (s.equals(skill.trim(), ignoreCase = true)) {
                        hasSkill = true
                    }
                }
                if (!hasSkill) {
                    continue
                }
            }
            if (available != null && available) {
                if (!t.isAvailable) {
                    continue
                }
            }
            filtered.add(t)
        }

        // default ordering, same as TalentService.search
        if (sort == null || sort == "relevance") {
            filtered.sortBy { it.id }
        } else if (sort == "rating") {
            filtered.sortWith { a, b ->
                val c = b.rating!!.compareTo(a.rating!!)
                if (c != 0) c else a.id!!.compareTo(b.id!!)
            }
        } else if (sort == "missions") {
            filtered.sortWith { a, b ->
                val c = b.missions!!.compareTo(a.missions!!)
                if (c != 0) c else a.id!!.compareTo(b.id!!)
            }
        } else {
            throw ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Unknown sort: $sort")
        }

        if (log.isDebugEnabled) {
            log.debug("Talent search: {} results, {} expensive", filtered.size, filtered.count { it.isExpensive })
        }

        val from = page * size
        val items: List<TalentEntity>
        if (from >= filtered.size) {
            items = ArrayList()
        } else {
            items = filtered.subList(from, min(from + size, filtered.size))
        }

        val result = LinkedHashMap<String, Any>()
        result["items"] = items
        result["page"] = page
        result["size"] = size
        result["total"] = filtered.size.toLong()
        return result
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): TalentEntity {
        return talentService.getTalent(id)
    }

    companion object {
        private val log = LoggerFactory.getLogger(TalentController::class.java)
    }
}
