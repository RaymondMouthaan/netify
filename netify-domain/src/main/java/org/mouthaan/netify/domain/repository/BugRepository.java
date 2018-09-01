package org.mouthaan.netify.domain.repository;

import org.mouthaan.netify.domain.model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional(value = "jpaTransactionManager")
public interface BugRepository extends JpaRepository<Bug, String> {
    Bug getOne(String s);
}
