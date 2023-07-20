package com.example.TechWearBot.model.UserLotteryTable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Lottery {
    @Id
    private Long chatId;
    private String userName;
    private Integer ticket;
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;

    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getTicket() {
        return ticket;
    }

    @Override
    public String toString() {
        return "Lottery{" +
                "chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", ticket=" + ticket +
                '}';
    }
}
