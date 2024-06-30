package com.example.userservice.message.email;

import com.example.userservice.message.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Locale;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class EmailMessage extends BaseMessage {
    private String template;

    private String subject;

    private String sender;

    private String receiver;

    private Map<String, String> args;

    private Locale locale;
}
