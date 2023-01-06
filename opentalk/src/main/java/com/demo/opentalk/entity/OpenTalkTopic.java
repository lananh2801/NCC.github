package com.demo.opentalk.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "OPEN_TALK_TOPIC")
public class OpenTalkTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOPIC_NO")
    private int topicNo;

    @Column(name = "TOPIC_NAME")
    private String topicName;

    @ManyToOne()
    @JoinColumn(name = "EMPLOYEE_NO", nullable = false)
    private Employee employee;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "BRANCH_NO", nullable = false)
    private CompanyBranch companyBranch;

    @Column(name = "LINK_MEETING")
    private String linkMeeting;
}
