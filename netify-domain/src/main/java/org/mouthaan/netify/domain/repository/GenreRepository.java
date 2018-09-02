package org.mouthaan.netify.domain.repository;

import org.mouthaan.netify.domain.model.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional(value = "jpaTransactionManager", readOnly = true)
public interface GenreRepository extends BaseRepository<Genre, Integer> {
    Genre findByName(String name);

    // A custom query example
    @Query(value = "SELECT g.name FROM Genre g WHERE g.id = :id")
    String findGenreNameById(@Param("id") Integer id);
}
