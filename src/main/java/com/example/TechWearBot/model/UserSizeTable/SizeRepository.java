package com.example.TechWearBot.model.UserSizeTable;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SizeRepository extends CrudRepository <Size, Long>{

    @Query(value = "select outfit_size from size where chat_id = :chatId", nativeQuery = true)
    String getOutfitSize(long chatId);

    @Query(value = "select boot_size from size where chat_id = :chatId", nativeQuery = true)
    Integer getBootSize(long chatId);

    @Query(value = "select last_compilation from size where chat_id = :chatId", nativeQuery = true)
    String getLastCompilation(long chatId);

    @Modifying
    @Transactional
    @Query(value = "update size set last_Compilation = :last  where chat_id = :chatId", nativeQuery = true)
    void saveLastCompilation(long chatId, String last);

}
