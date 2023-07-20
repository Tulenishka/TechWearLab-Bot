package com.example.TechWearBot.model.UserLotteryTable;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface LotteryRepository extends CrudRepository <Lottery, Long>{

      @Query(value = "select ticket from lottery where chat_id = :chatId", nativeQuery = true)
    int getTicket(long chatId);

    @Query(value = "select ticket from lottery where ticket = (select max(ticket) from lottery)", nativeQuery = true)
    int getMaxTicket();

}


