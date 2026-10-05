package com.molt.repository;

import com.molt.entity.Kind;
import com.molt.entity.TalentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TalentRepository extends JpaRepository<TalentEntity, Long> {

    List<TalentEntity> findByKind(Kind kind);

    /**
     * Talents available right now with a solid track record (rating 4.5+ and at least 50 missions).
     */
    @Query("select t from TalentEntity t where t.isAvailable = true and t.rating >= 4.5 and t.missions >= 50 "
            + "order by t.rating desc, t.missions desc")
    List<TalentEntity> findAvailableExperts();

    /**
     * Talents whose daily rate falls within the given range, bounds included.
     *
     * @param minEuros lowest daily rate, in euros
     * @param maxEuros highest daily rate, in euros
     */
    List<TalentEntity> findByRateCentsBetween(Long minEuros, Long maxEuros);
}
