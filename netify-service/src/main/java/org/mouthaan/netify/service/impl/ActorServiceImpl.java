package org.mouthaan.netify.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.domain.model.Actor;
import org.mouthaan.netify.domain.model.Gender;
import org.mouthaan.netify.domain.repository.ActorRepository;
import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.dto.ActorDto;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.mouthaan.netify.service.mapper.ActorServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("actorService")
@Slf4j
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    private final BaseDao baseDAO;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, BaseDao baseDAO) {
        this.actorRepository = actorRepository;
        this.baseDAO = baseDAO;
    }

    @Override
    public CountDto countAll() {
        return new CountDto(this.actorRepository.count());
    }

    @Override
    public List<ActorDto> findAll(Map<String,String> filterParams) {

        List<SearchCriteria> queryParams = new ArrayList<>();
        filterParams.forEach((k,v)->{
            if (k.equalsIgnoreCase("name"))     queryParams.add( new SearchCriteria(k, "%", v));
            if (k.equalsIgnoreCase("gender"))   queryParams.add( new SearchCriteria(k, "=", Gender.fromValue(v)));
        });

        List<ActorDto> actorDtoS = this.baseDAO.findAllPredicated(queryParams, Actor.class).stream()
                .map(ActorServiceMapper.MAPPER::toActorDto)
                .collect(Collectors.toList());
        if (actorDtoS.size() == 0) {
            String filter = queryParams.stream().map(SearchCriteria::toString)
                    .collect(Collectors.joining("; "));
            throw new NotFoundException("element.type.actor", filter);
        }
        return actorDtoS;
    }

    @Override
    public Page<ActorDto> findPaginated(Pageable pageable) {
        return this.actorRepository.findAll(pageable)
                .map(ActorServiceMapper.MAPPER::toActorDto);
    }

    @Override
    public ActorDto findById(Integer id) {
        Actor actor = this.actorRepository.getOne(id);

        if (null == actor) {
            throw new NotFoundException("element.type.actor", "id=\'" + id + "\'");
        }
        return ActorServiceMapper.MAPPER.toActorDto(actor);
    }

    @Override
    public ActorDto findByName(String name) {
        Actor actor = this.actorRepository.findByName(name);
        if (null == actor) {
            throw new NotFoundException("element.type.actor", name);
        }
        return ActorServiceMapper.MAPPER.toActorDto(actor);
    }

    @Override
    public ActorDto add(ActorDto actorDto) {
        if (this.isExists(actorDto)) {
            throw new AlreadyExistException("element.type.actor", actorDto.getName());
        }
        return ActorServiceMapper.MAPPER.toActorDto(this.actorRepository.saveAndFlush(ActorServiceMapper.MAPPER.toActor(actorDto)));
    }

    @Override
    public ActorDto update(Integer id, ActorDto actorDto) throws NotFoundException {
        Optional<Actor> actor = this.actorRepository.findById(id);

        if (actor.isPresent()) {
//            // Todo: Copy notNulls from updateActorDto to actor
//            ActorServiceMapper.MAPPER.toActor(actorDto);

            // save and flush actor
            Actor returnActor = this.actorRepository.saveAndFlush(actor.get());

            // return saved actor
            return ActorServiceMapper.MAPPER.toActorDto(returnActor);
        }
        else {
            throw new NotFoundException("element.type.actor", "id=\'" + id + "\'");
        }

    }

    @Override
    public void delete(Integer id) {
        Optional<Actor> actor = this.actorRepository.findById(id);
        if (actor.isPresent()) {
            this.actorRepository.delete(actor.get());
        } else {
            throw new NotFoundException("element.type.actor", "id=\'" + id + "\'");
        }

    }

    @Override
    public boolean isExists(ActorDto actor) {
        return this.actorRepository.findByName(actor.getName()) != null;
    }

}
