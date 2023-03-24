package com.example.socialnetwork.domain;

import com.example.socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private Long from;
    private Long to;
    private String body;
    private LocalDateTime dateTime;

    public Message(Long from, Long to, String body, LocalDateTime dateTime) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.dateTime = dateTime;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", body='" + body + '\'' +
                ", dateTime=" + dateTime.format(Constants.DATE_TIME_FORMATTER) +
                '}';
    }
}
