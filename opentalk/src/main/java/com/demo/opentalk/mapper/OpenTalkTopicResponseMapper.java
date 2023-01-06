package com.demo.opentalk.mapper;

import com.demo.opentalk.common.EntityMapper;
import com.demo.opentalk.dto.response.OpenTalkTopicResponseDTO;
import com.demo.opentalk.entity.OpenTalkTopic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OpenTalkTopicResponseMapper extends EntityMapper<OpenTalkTopicResponseDTO, OpenTalkTopic> {
}
