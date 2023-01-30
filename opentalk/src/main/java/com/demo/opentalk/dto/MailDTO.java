package com.demo.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MailDTO {
    private String[] to;
    private String subject;
    private String content;
    private Map<String, Object> props;
}
