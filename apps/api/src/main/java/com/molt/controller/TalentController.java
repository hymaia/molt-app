package com.molt.controller;

import com.molt.entity.Kind;
import com.molt.entity.TalentEntity;
import com.molt.exception.ApiException;
import com.molt.repository.TalentRepository;
import com.molt.service.TalentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/talents")
public class TalentController {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentService talentService;

    @GetMapping
    public Map<String, Object> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) Kind kind,
                                      @RequestParam(required = false) String skill,
                                      @RequestParam(required = false) Boolean available,
                                      @RequestParam(required = false, defaultValue = "relevance") String sort,
                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                      @RequestParam(required = false, defaultValue = "12") Integer size) {
        if (page < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "page must be >= 0");
        }
        if (size < 1 || size > 50) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "size must be between 1 and 50");
        }

        List<TalentEntity> all = kind != null ? talentRepository.findByKind(kind) : talentRepository.findAll();
        List<TalentEntity> filtered = new ArrayList<>();

        for (TalentEntity t : all) {
            if (q != null && !q.isBlank()) {
                String needle = q.trim().toLowerCase();
                boolean match = false;
                if (t.getName() != null && t.getName().toLowerCase().contains(needle)) {
                    match = true;
                } else if (t.getTitle() != null && t.getTitle().toLowerCase().contains(needle)) {
                    match = true;
                } else {
                    for (String s : t.getSkills()) {
                        if (s.toLowerCase().contains(needle)) {
                            match = true;
                            break;
                        }
                    }
                }
                if (!match) {
                    continue;
                }
            }
            if (kind != null) {
                if (t.getKind() != kind) {
                    continue;
                }
            }
            if (skill != null && !skill.isBlank()) {
                boolean hasSkill = false;
                for (String s : t.getSkills()) {
                    if (s.equalsIgnoreCase(skill.trim())) {
                        hasSkill = true;
                    }
                }
                if (!hasSkill) {
                    continue;
                }
            }
            if (available != null && available) {
                if (!t.isAvailable()) {
                    continue;
                }
            }
            filtered.add(t);
        }

        // default ordering, same as TalentService.search
        if (sort == null || sort.equals("relevance")) {
            filtered.sort(Comparator.comparing(TalentEntity::getId));
        } else if (sort.equals("rating")) {
            filtered.sort((a, b) -> {
                int c = Double.compare(b.getRating(), a.getRating());
                return c != 0 ? c : a.getId().compareTo(b.getId());
            });
        } else if (sort.equals("missions")) {
            filtered.sort((a, b) -> {
                int c = Integer.compare(b.getMissions(), a.getMissions());
                return c != 0 ? c : a.getId().compareTo(b.getId());
            });
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Unknown sort: " + sort);
        }

        if (log.isDebugEnabled()) {
            log.debug("Talent search: {} results, {} expensive", filtered.size(),
                    filtered.stream().filter(TalentEntity::isExpensive).count());
        }

        int from = page * size;
        List<TalentEntity> items;
        if (from >= filtered.size()) {
            items = new ArrayList<>();
        } else {
            items = filtered.subList(from, Math.min(from + size, filtered.size()));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", items);
        result.put("page", page);
        result.put("size", size);
        result.put("total", (long) filtered.size());
        return result;
    }

    @GetMapping("/{id}")
    public TalentEntity get(@PathVariable Long id) {
        return talentService.getTalent(id);
    }
}
