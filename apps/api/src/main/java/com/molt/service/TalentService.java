package com.molt.service;

import com.molt.entity.Kind;
import com.molt.entity.TalentEntity;
import com.molt.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TalentService {

    private final TalentRepository talentRepository;

    @Transactional(readOnly = true)
    public TalentEntity getTalent(Long id) {
        TalentEntity talent = talentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent " + id + " not found"));
        try {
            log.debug("Talent {} has {} reviews", id, talent.getReviews().size());
        } catch (Exception e) {
            log.warn("Could not load reviews for talent {}", id);
        }
        return talent;
    }

    /**
     * Filters and sorts talents. Paging is left to the caller.
     */
    @Transactional(readOnly = true)
    public List<TalentEntity> search(String q, Kind kind, String skill, Boolean available, String sort) {
        List<TalentEntity> all = kind != null ? talentRepository.findByKind(kind) : talentRepository.findAll();
        String needle = q == null ? null : q.trim().toLowerCase();

        return all.stream()
                .filter(t -> needle == null || needle.isEmpty()
                        || t.getName().toLowerCase().contains(needle)
                        || t.getTitle().toLowerCase().contains(needle)
                        || t.getSkills().stream().anyMatch(s -> s.toLowerCase().contains(needle)))
                .filter(t -> skill == null || skill.isBlank()
                        || t.getSkills().stream().anyMatch(s -> s.toLowerCase().contains(skill.trim().toLowerCase())))
                .filter(t -> available == null || t.isAvailable() == available)
                .sorted(comparatorFor(sort))
                .collect(Collectors.toList());
    }

    private Comparator<TalentEntity> comparatorFor(String sort) {
        if ("missions".equals(sort)) {
            return Comparator.comparing(TalentEntity::getMissions).reversed()
                    .thenComparing(TalentEntity::getId);
        }
        return Comparator.comparing(TalentEntity::getRating).reversed()
                .thenComparing(TalentEntity::getId);
    }
}
