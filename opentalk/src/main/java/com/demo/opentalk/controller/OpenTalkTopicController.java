package com.demo.opentalk.controller;

import com.demo.opentalk.dto.request.OpenTalkTopicRequestDTO;
import com.demo.opentalk.dto.response.OpenTalkTopicResponseDTO;
import com.demo.opentalk.service.MailService;
import com.demo.opentalk.service.OpenTalkTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenTalkTopicController {
    private final OpenTalkTopicService openTalkTopicService;
    private final MailService mailService;

    @PostMapping("add-open-talk-topic")
    public OpenTalkTopicResponseDTO addOpenTalkTopic(@RequestBody OpenTalkTopicRequestDTO openTalkTopicRequestDTO) {
        return openTalkTopicService.addOpenTalkTopic(openTalkTopicRequestDTO);
    }

    @PutMapping("update-open-talk-topic")
    public OpenTalkTopicResponseDTO updateOpenTalkTopic(@RequestBody OpenTalkTopicRequestDTO openTalkTopicRequestDTO) {
        return openTalkTopicService.updateOpenTalkTopic(openTalkTopicRequestDTO);
    }

    @DeleteMapping("delete-open-talk-topic/{id}")
    public String deleteOpenTalkTopic(@PathVariable Integer id) {
        return openTalkTopicService.deleteOpenTalkTopic(id);
    }

    @GetMapping("get-open-talk-topic-by-criteria")
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicByCriteria(
            Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer branchNo,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return openTalkTopicService.getOpenTalkTopicByCriteria(pageable, active, branchNo, firstName, startDate, endDate);
    }

    @GetMapping("get-open-talk-topic-up-coming")
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicUpComing(
            Pageable pageable,
            @RequestParam(required = false) Integer branchNo,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return openTalkTopicService.getOpenTalkTopicUpComing(pageable, branchNo, firstName, startDate, endDate);
    }

    @GetMapping("get-open-talk-topic-for-employeeNo")
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicForEmployeeNo(
            @RequestParam Integer employeeNo,
            Pageable pageable,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
    return openTalkTopicService.getOpenTalkTopicForEmployeeNo(employeeNo, pageable, startDate, endDate);
    }

    @GetMapping("get-open-talk-topic-is-register-for-employee")
    public List<OpenTalkTopicResponseDTO> getOpenTalkTopicIsRegisterForEmployee(Pageable pageable,
                                                                                @RequestParam Integer employeeNo){
        return openTalkTopicService.getOpenTalkTopicIsRegisterForEmployee(pageable, employeeNo);
    }

    @PostMapping("send-mail-open-talk")
    public String sendMailOpenTalk(@RequestParam Integer topicNo, @RequestParam String[] email) throws MessagingException {
        return openTalkTopicService.sendMailOpenTalk(topicNo, email);
    }

    @PostMapping("send-mail-with-scheduled")
    public String sendMailWithScheduled() {
        return openTalkTopicService.sendMailWithScheduled();
    }

    @PostMapping("upload-slide")
    public String uploadSlide(@RequestParam MultipartFile file) {
        return openTalkTopicService.uploadSlide(file);
    }
}
