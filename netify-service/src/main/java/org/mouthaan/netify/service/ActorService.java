package org.mouthaan.netify.service;

import org.mouthaan.netify.service.dto.ActorDto;
import org.mouthaan.netify.service.exception.NotFoundException;

public interface ActorService extends BaseService<ActorDto, Integer>{
    ActorDto findByName(String name) throws NotFoundException;
}
