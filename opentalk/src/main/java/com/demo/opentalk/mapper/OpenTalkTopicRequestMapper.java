package com.demo.opentalk.mapper;

import com.demo.opentalk.common.EntityMapper;
import com.demo.opentalk.dto.request.OpenTalkTopicRequestDTO;
import com.demo.opentalk.entity.OpenTalkTopic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OpenTalkTopicRequestMapper extends EntityMapper<OpenTalkTopicRequestDTO, OpenTalkTopic> {
}
