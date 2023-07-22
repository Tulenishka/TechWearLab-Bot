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

    private Integer lotteryDateDay;

    private Integer lotteryDateMonth;

    private Integer lotteryDateHour;

    private Integer lotteryDateMinute;

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

    public Integer getLotteryDateDay() {
        return lotteryDateDay;
    }

    public void setLotteryDateDay(Integer lotteryDateDay) {
        this.lotteryDateDay = lotteryDateDay;
    }

    public Integer getLotteryDateMonth() {
        return lotteryDateMonth;
    }

    public void setLotteryDateMonth(Integer lotteryDateMonth) {
        this.lotteryDateMonth = lotteryDateMonth;
    }

    public Integer getLotteryDateHour() {
        return lotteryDateHour;
    }

    public void setLotteryDateHour(Integer lotteryDateHour) {
        this.lotteryDateHour = lotteryDateHour;
    }

    public Integer getLotteryDateMinute() {
        return lotteryDateMinute;
    }

    public void setLotteryDateMinute(Integer lotteryDateMinute) {
        this.lotteryDateMinute = lotteryDateMinute;
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
                ", lotteryDateDay=" + lotteryDateDay +
                ", lotteryDateMonth=" + lotteryDateMonth +
                ", lotteryDateHour=" + lotteryDateHour +
                ", lotteryDateMinute=" + lotteryDateMinute +
                ", lotteryWinnerTicket=" + lotteryWinnerTicket +
                '}';
    }
}
