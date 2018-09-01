package org.mouthaan.netify.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.repository.ActorRepository;
import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.dto.ActorDto;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.exception.NotFoundException;

import org.mouthaan.netify.service.mapper.ActorServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("actorService")
@Slf4j
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final ActorServiceMapper actorServiceMapper;
    private final BaseDao baseDao;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ActorServiceMapper actorServiceMapper, BaseDao baseDao) {
        this.actorRepository = actorRepository;
        this.actorServiceMapper = actorServiceMapper;
        this.baseDao = baseDao;
    }

    @Override
    public ActorDto findByName(String name) throws NotFoundException {
        return null;
    }

    @Override
    public CountDto countAll() {
        return new CountDto(this.actorRepository.count());
    }

    @Override
    public boolean isExists(ActorDto actorDto) {
        return false;
    }

    @Override
    public List<ActorDto> findAll(Map<String, String> queryParams) throws NotFoundException {
        return null;
    }

    @Override
    public Page<ActorDto> findPaginated(Pageable p) {
        return null;
    }

    @Override
    public ActorDto findById(Integer integer) throws NotFoundException {
        return null;
    }

    @Override
    public ActorDto add(ActorDto actorDto) {
        return null;
    }

    @Override
    public ActorDto update(Integer integer, ActorDto actorDto) throws NotFoundException {
        return null;
    }

    @Override
    public void delete(Integer integer) throws NotFoundException {

    }
}
