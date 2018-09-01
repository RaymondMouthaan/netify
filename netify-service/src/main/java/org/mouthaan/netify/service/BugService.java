package org.mouthaan.netify.service;

import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.service.dto.BugDto;

import java.util.List;

public interface BugService {
    Long countAll();
    BugDto findByBugKey(String bugKey);
    List<BugDto> findAll(List<SearchCriteria> queryParams);
    List<BugDto> findAll();
    BugDto add(BugDto bugDto);
    BugDto update(BugDto bugDto);
    boolean isExist(BugDto bugDto);

}
