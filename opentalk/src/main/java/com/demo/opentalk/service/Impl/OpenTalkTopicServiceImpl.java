package com.demo.opentalk.service.Impl;

import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.dto.MailDTO;
import com.demo.opentalk.dto.request.OpenTalkTopicRequestDTO;
import com.demo.opentalk.dto.response.OpenTalkTopicResponseDTO;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.OpenTalkTopic;
import com.demo.opentalk.exception.NotFoundException;
import com.demo.opentalk.mapper.EmployeeResponseMapper;
import com.demo.opentalk.mapper.OpenTalkTopicRequestMapper;
import com.demo.opentalk.mapper.OpenTalkTopicResponseMapper;
import com.demo.opentalk.model.projection.OpenTalkTopicProjection;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.repository.OpenTalkTopicRepository;
import com.demo.opentalk.service.MailService;
import com.demo.opentalk.service.OpenTalkTopicService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenTalkTopicServiceImpl implements OpenTalkTopicService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final OpenTalkTopicRepository openTalkTopicRepository;
    private final OpenTalkTopicRequestMapper openTalkTopicRequestMapper;
    private final OpenTalkTopicResponseMapper openTalkTopicResponseMapper;
    private final EmployeeResponseMapper employeeResponseMapper;
    private final CompanyBranchRepository companyBranchRepository;
    private final MailService mailService;

    @Override
    public OpenTalkTopicResponseDTO addOpenTalkTopic(OpenTalkTopicRequestDTO openTalkTopicRequestDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findById(openTalkTopicRequestDTO.getEmployeeNo());
        if (!employeeOptional.isPresent() || openTalkTopicRequestDTO.getBranchNo() != employeeOptional.get().getCompanyBranch().getBranchNo()) {
            throw new NotFoundException(MessageConstant.EMPLOYEE_IS_NULL);
        }
        OpenTalkTopic openTalkTopic = mapper.map(openTalkTopicRequestDTO, OpenTalkTopic.class);
        openTalkTopic.setEmployee(employeeOptional.get());
        openTalkTopic.setCompanyBranch(companyBranchRepository.getById(openTalkTopicRequestDTO.getBranchNo()));
        openTalkTopicRepository.save(openTalkTopic);

        OpenTalkTopicResponseDTO openTalkTopicResponseDTO = mapper.map(openTalkTopic, OpenTalkTopicResponseDTO.class);
        openTalkTopicResponseDTO.setEmployeeName(openTalkTopic.getEmployee().getLastName() + " " + openTalkTopic.getEmployee().getFirstName());
        openTalkTopicResponseDTO.setBranchName(openTalkTopic.getCompanyBranch().getBranchName());

        return openTalkTopicResponseDTO;
    }

    @Override
    public OpenTalkTopicResponseDTO updateOpenTalkTopic(OpenTalkTopicRequestDTO openTalkTopicRequestDTO) {
        Optional<OpenTalkTopic> openTalkTopicOptional = openTalkTopicRepository.findById(openTalkTopicRequestDTO.getTopicNo());
        Optional<Employee> employeeOptional = employeeRepository.findById(openTalkTopicRequestDTO.getEmployeeNo());
        if (!openTalkTopicOptional.isPresent() || !employeeOptional.isPresent()) {
            throw new NotFoundException(MessageConstant.OPEN_TALK_TOPIC_OR_EMPLOYEE_IS_NULL);
        }
        OpenTalkTopic openTalkTopic = openTalkTopicOptional.get();
        openTalkTopic = openTalkTopicRequestMapper.toEntity(openTalkTopicRequestDTO);
        openTalkTopic.setEmployee(employeeOptional.get());

        OpenTalkTopicResponseDTO openTalkTopicResponseDTO = openTalkTopicResponseMapper.toDTO(openTalkTopic);
        openTalkTopicResponseDTO.setEmployeeName(openTalkTopic.getEmployee().getLastName() +" "
                + openTalkTopic.getEmployee().getFirstName());
        openTalkTopicResponseDTO.setBranchName(openTalkTopic.getCompanyBranch().getBranchName());

        return openTalkTopicResponseDTO;
    }

    @Override
    public String deleteOpenTalkTopic(Integer id) {
        if (!openTalkTopicRepository.findById(id).isPresent()) {
            throw new NotFoundException(MessageConstant.OPEN_TALK_TOPIC_IS_NULL);
        }
        openTalkTopicRepository.deleteById(id);
        return MessageConstant.DELETE_DONE;
    }

    @Override
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicByCriteria(Pageable pageable, Boolean active, Integer branchNo, String firstName, Date startDate, Date endDate) {
        Page<OpenTalkTopic> openTalkTopicPage = openTalkTopicRepository.getOpenTalkTopicByCriteria(pageable, active, branchNo, firstName, startDate, endDate);
        return getOpenTalkTopicResponseDTOS(openTalkTopicPage);
    }

    @Override
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicUpComing(Pageable pageable, Integer branchNo, String firstName, Date startDate, Date endTDate) {
        Page<OpenTalkTopic> openTalkTopicPage = openTalkTopicRepository.getOpenTalkTopicUpComing(pageable, branchNo, firstName, startDate, endTDate);
        return getOpenTalkTopicResponseDTOS(openTalkTopicPage);
    }

    @Override
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicForEmployeeNo(Integer employeeNo, Pageable pageable, Date startDate, Date endDate) {
        Page<OpenTalkTopic> openTalkTopicPage = openTalkTopicRepository.getOpenTalkTopicForEmployeeNo(employeeNo, pageable, startDate, endDate);
        return getOpenTalkTopicResponseDTOS(openTalkTopicPage);
    }

    @Override
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicIsRegisterForEmployee(Pageable pageable, Integer employeeNo) {
        Page<OpenTalkTopic> openTalkTopicPage = openTalkTopicRepository.getOpenTalkTopicIsRegisterForEmployee(pageable, employeeNo);
        return getOpenTalkTopicResponseDTOS(openTalkTopicPage);
    }

    @Override
    public String sendMailOpenTalk(Integer topicNo, String[] email) throws MessagingException {
        OpenTalkTopic openTalkTopic = openTalkTopicRepository.findById(topicNo).get();
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(email);
        mailDTO.setSubject("Invitation: [" + openTalkTopic.getCompanyBranch().getBranchName() + "] [OFFLINE] " + openTalkTopic.getTopicName()
                + " - " + openTalkTopic.getEmployee().getLastName() + ' ' + openTalkTopic.getEmployee().getFirstName()
                + " " + openTalkTopic.getDate() + " 10am - 12am");
        mailDTO.setContent("Link Meeting: " + openTalkTopic.getLinkMeeting());
        try {
            mailService.sendMail(mailDTO);
            return MessageConstant.SEND_MAIL_DONE;
        } catch (Exception e) {
            return MessageConstant.SEND_MAIL_FAIL;
        }
    }
    @Scheduled(fixedRate = 120000)
//    @Scheduled(cron = " 0 0 11 * * FRI ")
    @Override
    public String sendMailWithScheduled() {
        OpenTalkTopicProjection openTalkTopicProjection = openTalkTopicRepository.getOpenTalkTopicIsComingSoon();
//        List<String> emails = employeeRepository.getEmployeesByBranchNo(openTalkTopicProjection.getBranchNo()).stream()
//                .map(Employee::getEmail)
//                .collect(Collectors.toList());
//        String[] email = new String[emails.size()];
//        emails.toArray(email);
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(new String[]{"lananh2801.ncc@gmail.com", "anh.phamthilan@ncc.asia"});
        mailDTO.setSubject("Invitation: [" + openTalkTopicProjection.getBranchName() + "] [OFFLINE] " + openTalkTopicProjection.getTopicName()
                + " - " + openTalkTopicProjection.getFullName()
                + " " + openTalkTopicProjection.getDate() + " 10am - 12am");
        mailDTO.setContent("Link Meeting: \n" + openTalkTopicProjection.getLinkMeeting());
        try {
            mailService.sendMail(mailDTO);
            return MessageConstant.SEND_MAIL_DONE;
        } catch (Exception e) {
            return MessageConstant.SEND_MAIL_FAIL;
        }
    }

    private List<OpenTalkTopicResponseDTO> getOpenTalkTopicResponseDTOS(Page<OpenTalkTopic> openTalkTopicPage) {
        List<OpenTalkTopicResponseDTO> openTalkTopicResponseDTOList = new ArrayList<>();
        for (OpenTalkTopic openTalkTopic: openTalkTopicPage.getContent()) {
            OpenTalkTopicResponseDTO openTalkTopicResponseDTO = openTalkTopicResponseMapper.toDTO(openTalkTopic);
            openTalkTopicResponseDTO.setEmployeeName(openTalkTopic.getEmployee().getLastName() +" "
                                                        + openTalkTopic.getEmployee().getFirstName());
            openTalkTopicResponseDTO.setBranchName(openTalkTopic.getCompanyBranch().getBranchName());
            openTalkTopicResponseDTOList.add(openTalkTopicResponseDTO);
        }
        return openTalkTopicResponseDTOList;
    }
}
