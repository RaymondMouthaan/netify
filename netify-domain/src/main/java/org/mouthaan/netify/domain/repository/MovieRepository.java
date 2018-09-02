package org.mouthaan.netify.domain.repository;

import org.mouthaan.netify.domain.model.Movie;
import org.springframework.stereotype.Repository;

import java.sql.Date;

/**
 * Created by in482mou on 23-6-2017.
 */
@Repository
//@Transactional(value = "jpaTransactionManager", readOnly = true)
public interface MovieRepository extends BaseRepository<Movie, Integer> {
    Movie findByTitleAndReleaseDate(String title, Date releaseDate);
}
