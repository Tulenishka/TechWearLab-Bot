package com.example.TechWearBot.model.LotteryTableStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LotteryStatusRepository extends CrudRepository<LotteryStatus, Long> {

    @Query(value = "select lotteryActive from lottetystatus where lotteryId = 1", nativeQuery = true)
    boolean getActive();

    @Query(value = "select lotteryWinnerTicket from lottetystatus where lotteryId = 1", nativeQuery = true)
    int getWinnerTicket();

    @Query(value = "select lotteryDate from lottetystatus where lotteryId = 1", nativeQuery = true)
    int getDate();
}

