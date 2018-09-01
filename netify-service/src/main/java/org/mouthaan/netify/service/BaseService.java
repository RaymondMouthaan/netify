package org.mouthaan.netify.service;

import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T, ID extends Serializable> {
    CountDto countAll();
    boolean isExists(T t);
    List<T> findAll(Map<String,String> queryParams) throws NotFoundException;
    Page<T> findPaginated(Pageable p);
    T findById(ID id) throws NotFoundException;
    T add(T t);
    T update(ID id, T t) throws NotFoundException;
    void delete(ID id) throws NotFoundException;
}
