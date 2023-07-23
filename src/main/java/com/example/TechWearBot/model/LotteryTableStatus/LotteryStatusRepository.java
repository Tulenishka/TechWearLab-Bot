package com.example.TechWearBot.model.LotteryTableStatus;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LotteryStatusRepository extends CrudRepository<LotteryStatus, Long> {

    @Query(value = "select lottery_active from lottery_status where lottery_Id = 1", nativeQuery = true)
    Boolean getActive();

    @Query(value = "select lottery_date_day from lottery_status where lottery_Id = 1", nativeQuery = true)
    int getDay();

    @Query(value = "select lottery_date_month from lottery_status where lottery_Id = 1", nativeQuery = true)
    int getMonth();

    @Query(value = "select lottery_date_hour from lottery_status where lottery_Id = 1", nativeQuery = true)
    int getHour();

    @Query(value = "select lottery_date_minute from lottery_status where lottery_Id = 1", nativeQuery = true)
    int getMinute();

    @Query(value = "select lottery_Creator_Id from lottery_status where lottery_Id = 1", nativeQuery = true)
    long getCreatorId();

    @Query(value = "select lottery_winner_ticket from lottery_status where lottery_Id = 1", nativeQuery = true)
    long getWinner();

}

