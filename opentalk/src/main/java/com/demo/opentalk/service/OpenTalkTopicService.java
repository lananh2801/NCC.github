package com.demo.opentalk.service;

import com.demo.opentalk.dto.request.OpenTalkTopicRequestDTO;
import com.demo.opentalk.dto.response.OpenTalkTopicResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.sql.Date;
import java.util.List;

public interface OpenTalkTopicService {
    OpenTalkTopicResponseDTO addOpenTalkTopic(OpenTalkTopicRequestDTO openTalkTopicRequestDTO);
    OpenTalkTopicResponseDTO updateOpenTalkTopic(OpenTalkTopicRequestDTO openTalkTopicRequestDTO);
    String deleteOpenTalkTopic(Integer id);
    List<OpenTalkTopicResponseDTO> getOpenTalkTopicByCriteria(Pageable pageable, Boolean active, Integer branchNo,
                                                              String firstName, Date startDate, Date endDate);
    List<OpenTalkTopicResponseDTO> getOpenTalkTopicUpComing(Pageable pageable, Integer branchNo, String firstName,
                                                            Date startDate, Date endTDate);
    List<OpenTalkTopicResponseDTO> getOpenTalkTopicForEmployeeNo(Integer employeeNo, Pageable pageable,
                                                                 Date startDate, Date endDate);
    List<OpenTalkTopicResponseDTO> getOpenTalkTopicIsRegisterForEmployee(Pageable pageable, Integer employeeNo);
    String sendMailOpenTalk(Integer topicNo, String[] email) throws MessagingException;
    String sendMailWithScheduled();
    String uploadSlide(MultipartFile file);
    String sendMailWithHtml(Integer topicNo, String[] emails);
}
