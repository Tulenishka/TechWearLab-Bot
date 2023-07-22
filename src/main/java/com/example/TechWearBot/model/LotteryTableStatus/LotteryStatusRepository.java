package com.example.TechWearBot.model.LotteryTableStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LotteryStatusRepository extends CrudRepository<LotteryStatus, Long> {

    @Query(value = "select lotteryActive from lottety_status where lotteryId = 1", nativeQuery = true)
    boolean getActive();

    @Query(value = "select lotteryWinnerTicket from lottety_status where lotteryId = 1", nativeQuery = true)
    int getWinnerTicket();

    @Query(value = "select lotteryCreatorId from lottety_status where lotteryId = 1", nativeQuery = true)
    int getCreatorId();

}

