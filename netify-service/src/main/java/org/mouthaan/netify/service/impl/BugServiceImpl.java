package org.mouthaan.netify.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.domain.model.Bug;
import org.mouthaan.netify.domain.repository.BugRepository;
import org.mouthaan.netify.service.BugService;
import org.mouthaan.netify.service.dto.BugDto;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.mapper.BugServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("bugService")
@Slf4j
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final BaseDao baseDao;

    @Autowired
    public BugServiceImpl(BugRepository bugRepository, BaseDao baseDao) {
        this.bugRepository = bugRepository;
        this.baseDao = baseDao;

    }

    @Override
    public Long countAll() {
        return this.bugRepository.count();
    }

    @Override
    public List<BugDto> findAll(List<SearchCriteria> queryParams) {
        return this.baseDao.findAllPredicated(queryParams, Bug.class).stream()
                .map(BugServiceMapper.INSTANCE::toBugDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BugDto> findAll() {
        return this.bugRepository.findAll().stream()
                .map(BugServiceMapper.INSTANCE::toBugDto)
                .collect(Collectors.toList());
    }

    @Override
    public BugDto findByBugKey(String bugKey) {
        Bug bug = this.bugRepository.getOne(bugKey);
        BugDto bugDto = BugServiceMapper.INSTANCE.toBugDto(bug);
        if (bugDto == null) {
            return new BugDto(bugKey, "unknown", false, 0, "bug with key='" + bugKey + "' not found, return default false.");
        }
        return bugDto;
    }

    @Override
    public BugDto add(BugDto bugDto) {
        if (this.isExist(bugDto)) {
            throw new AlreadyExistException("element.type.bug", bugDto.getBugKey());
        }
        return BugServiceMapper.INSTANCE.toBugDto(this.bugRepository.saveAndFlush(BugServiceMapper.INSTANCE.toBug(bugDto)));
    }

    @Override
    public BugDto update(BugDto bugDto) {
        Bug bug = this.bugRepository.getOne(bugDto.getBugKey());

        // Todo: copy updated fields to bug
//        bugServiceMapper.map(bugDto, bug);
        Bug updatedBug = bugRepository.saveAndFlush(bug);
//        log.debug("Updated bug [bugKey='{}', bugLocation='{}', bugEnabled='{}', bugGroup='{}', bugDescription='{}']",
//                updatedBug.getBugKey(),
//                updatedBug.getBugLocation(),
//                updatedBug.getBugEnabled(),
//                updatedBug.getBugGroup(),
//                updatedBug.getBugDescription()
//        );
        return BugServiceMapper.INSTANCE.toBugDto(updatedBug);
    }

    @Override
    public boolean isExist(BugDto bugDto) {
        return this.bugRepository.getOne(bugDto.getBugKey()) != null;
    }
}
