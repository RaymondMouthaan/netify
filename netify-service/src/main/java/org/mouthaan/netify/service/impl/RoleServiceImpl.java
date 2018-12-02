package org.mouthaan.netify.service.impl;

import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.domain.model.Role;
import org.mouthaan.netify.domain.repository.RoleRepository;
import org.mouthaan.netify.service.RoleService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.RoleDto;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.mouthaan.netify.service.mapper.RoleServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("roleService")
@EnableTransactionManagement
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final BaseDao baseDAO;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, BaseDao baseDAO) {
        this.roleRepository = roleRepository;
        this.baseDAO = baseDAO;
    }

    @Override
    public CountDto countAll() {
        return new CountDto(this.roleRepository.count());
    }

    @Override
    public Page<RoleDto> findPaginated(Pageable pageable) {
        return this.roleRepository.findAll(pageable)
                .map(RoleServiceMapper.MAPPER::toRoleDto);
    }

    @Override
    public List<RoleDto> findAll(Map<String, String> filterParams) {

        List<SearchCriteria> queryParams = new ArrayList<>();
        filterParams.forEach((k, v) -> {
            if (k.equalsIgnoreCase("character")) queryParams.add(new SearchCriteria(k, "%", v));
            if (k.equalsIgnoreCase("order")
                    || k.equalsIgnoreCase("movieId")
                    || k.equalsIgnoreCase("actorId")) queryParams.add(new SearchCriteria(k, "=", v));
        });

        List<RoleDto> roleDtos = this.baseDAO.findAllPredicated(queryParams, Role.class).stream()
                .map(RoleServiceMapper.MAPPER::toRoleDto)
                .collect(Collectors.toList());
        if (roleDtos.size() == 0) {
            String filter = queryParams.stream().map(SearchCriteria::toString)
                    .collect(Collectors.joining("; "));
            throw new NotFoundException("element.type.role", filter);
        }
        return roleDtos;
    }

    public List<RoleDto> findAll() {
        return this.roleRepository.findAll().stream()
                .map(RoleServiceMapper.MAPPER::toRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto findById(Integer id) {
        Role role = this.roleRepository.getOne(id);
        if (null == role) {
            throw new NotFoundException("element.type.role", "id=\'" + id + "\'");
        }
        return RoleServiceMapper.MAPPER.toRoleDto(role);
    }

    @Override
    public RoleDto add(RoleDto roleDto) {
        if (this.isExists(roleDto)) {
            throw new AlreadyExistException("element.type.role", roleDto.getCharacter());
        }
        return RoleServiceMapper.MAPPER.toRoleDto(roleRepository.saveAndFlush(RoleServiceMapper.MAPPER.toRole(roleDto)));
    }

    @Override
    public RoleDto update(Integer id, RoleDto roleDto) {
        Optional<Role> role = this.roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException("element.type.role", "id=\'" + id + "\'");
        }
        if (null != roleDto.getCharacter()) {
            role.get().setCharacter(roleDto.getCharacter());
        }
        if (null != roleDto.getOrder()) {
            role.get().setOrder(roleDto.getOrder());
        }
        if (null != roleDto.getActor()) {
            // Todo
            //role.get().setActor(roleServiceMapper.map(roleDto.getActor(), Actor.class));
        }
//        if (null != roleDto.getMovie()) {
//            role.setMovie(roleServiceMapper.map(roleDto.getMovie(), Movie.class));
//        }
        if (null != roleDto.getId()) {
            role.get().setId(roleDto.getId());
        }
        return RoleServiceMapper.MAPPER.toRoleDto(this.roleRepository.saveAndFlush(role.get()));
    }

    @Override
    public void delete(Integer id) {
//        Role role = this.roleRepository.findById(id);
//        if (null == role) {
//            throw new NotFoundException("element.type.role", "id=\'" + String.valueOf(id) + "\'");
//        }
//        this.roleRepository.delete(role);
    }

    @Override
    public boolean isExists(RoleDto role) {
        return roleRepository.findById(role.getId()).isPresent();
    }

}
