package com.demo.opentalk.dto.response;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OpenTalkTopicResponseDTO {
    private int topicNo;
    private String topicName;
    private String employeeName;
    private Date date;
    private String branchName;
    private String linkMeeting;
}
