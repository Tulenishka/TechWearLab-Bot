package com.example.TechWearBot.model.LotteryTableStatus;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class LotteryStatus {

    @Id
    private Integer lotteryId;

    private Long lotteryCreatorId;

    private Boolean lotteryActive;

    private Integer lotteryDate;

    private Integer lotteryWinnerTicket;

    public Integer getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Integer lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getLotteryCreatorId() {
        return lotteryCreatorId;
    }

    public void setLotteryCreatorId(Long lotteryCreatorId) {
        this.lotteryCreatorId = lotteryCreatorId;
    }

    public Boolean getLotteryActive() {
        return lotteryActive;
    }

    public void setLotteryActive(Boolean lotteryActive) {
        this.lotteryActive = lotteryActive;
    }

    public Integer getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Integer lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public Integer getLotteryWinnerTicket() {
        return lotteryWinnerTicket;
    }

    public void setLotteryWinnerTicket(Integer lotteryWinnerTicket) {
        this.lotteryWinnerTicket = lotteryWinnerTicket;
    }

    @Override
    public String toString() {
        return "LotteryStatus{" +
                "lotteryId=" + lotteryId +
                ", lotteryCreatorId=" + lotteryCreatorId +
                ", lotteryActive=" + lotteryActive +
                ", lotteryDate=" + lotteryDate +
                ", lotteryWinnerTicket=" + lotteryWinnerTicket +
                '}';
    }
}
