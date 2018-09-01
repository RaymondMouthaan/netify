package org.mouthaan.netify.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    long count();
    List<T> findAll();
    Page<T> findAll(Pageable pageable);


//    T findOne(ID id);
    Optional<T> findById(ID id);

    //    @Modifying
//    @Transactional(value = "jpaTransactionManager")
    <S extends T> S saveAndFlush(S element);

    //    @Modifying
//    @Transactional(value = "jpaTransactionManager")
    void delete(T deleted);
}
