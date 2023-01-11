package com.demo.opentalk.model.projection;

import java.sql.Date;

public interface OpenTalkTopicProjection {
    int getTopicNo();
    String getTopicName();
    int getBranchNo();
    String getLinkMeeting();
    String getBranchName();
    Date getDate();
    String getFullName();
}
