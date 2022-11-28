package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate {

    private List<String> mailTo = new ArrayList<>();

    private String emailSubject;

    private String emailBody;

    public void addMailTo(String mailTo) {
        this.mailTo.add(mailTo);
    }

}
