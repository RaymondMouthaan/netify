package org.mouthaan.netify.domain.repository;

import org.mouthaan.netify.domain.model.Role;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional(value = "jpaTransactionManager", readOnly = true)
public interface RoleRepository extends BaseRepository<Role, Integer> {

}
