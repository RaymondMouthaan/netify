package org.mouthaan.netify.service.mapper;

import org.junit.jupiter.api.Test;
import org.mouthaan.netify.domain.model.Bug;
import org.mouthaan.netify.service.dto.BugDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BugServiceMapperTest {

    @Test
    void mapBugToBugDtoTest() {

        // given
        Bug bug = new Bug();
        bug.setBugKey("key1");
        bug.setBugDescription("This is a bug");
        bug.setBugEnabled(true);
        bug.setBugGroup(23);
        bug.setBugLocation("Rotterdam");

        // when
        BugDto bugDto = BugServiceMapper.INSTANCE.toBugDto(bug);

        // then
        assertNotNull(bugDto);
        assertEquals(bug.getBugKey(), bugDto.getBugKey());
        assertEquals(bug.getBugDescription(), bugDto.getBugDescription());
        assertEquals(bug.getBugEnabled(), bugDto.getBugEnabled());
        assertEquals(bug.getBugGroup(), bugDto.getBugGroup());
        assertEquals(bug.getBugLocation(), bugDto.getBugLocation());

    }

    @Test
    void mapBugDtoToBugTest() {

        // given
        BugDto bugDto = new BugDto();
        bugDto.setBugKey("key1");
        bugDto.setBugDescription("This is a bug");
        bugDto.setBugEnabled(true);
        bugDto.setBugGroup(23);
        bugDto.setBugLocation("Rotterdam");

        // when
        Bug bug = BugServiceMapper.INSTANCE.toBug(bugDto);

        // then
        assertNotNull(bug);
        assertEquals(bugDto.getBugKey(), bug.getBugKey());
        assertEquals(bugDto.getBugDescription(), bug.getBugDescription());
        assertEquals(bugDto.getBugEnabled(), bug.getBugEnabled());
        assertEquals(bugDto.getBugGroup(), bug.getBugGroup());
        assertEquals(bugDto.getBugLocation(), bug.getBugLocation());

    }

}