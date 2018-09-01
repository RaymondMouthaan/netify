package org.mouthaan.netify.domain.repository;

import org.mouthaan.netify.domain.model.Actor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends BaseRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    Actor findByName(String name);
}
