package com.demo.opentalk.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OpenTalkTopicRequestDTO {
    private int topicNo;
    private String topicName;
    private int employeeNo;
    private Date date;
    private int branchNo;
    private String linkMeeting;
}
