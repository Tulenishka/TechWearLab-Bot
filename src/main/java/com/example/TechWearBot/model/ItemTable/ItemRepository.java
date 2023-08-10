package com.example.TechWearBot.model.ItemTable;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository <Item,Long> {



    @Query(value = "DELETE FROM item where message_id = messageId", nativeQuery = true)
    void deleteItem(Integer messageId);

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where item_type = \"specific\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] specificPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topOrderPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botOrderPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoesOrderPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizes = true and item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topSPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizem = true and item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topMPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizel = true and item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizexl = true and item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topXLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizexxl = true and item_type = \"top\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] topXXLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizes = true and item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botSPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizem = true and item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botMPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizel = true and item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizexl = true and item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botXLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where sizexxl = true and item_type = \"bot\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] botXXLPool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size36 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes36Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size37 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes37Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size38 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes38Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size39 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes39Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size40 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes40Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size41 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes41Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size42 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes42Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size43 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes43Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size44 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes44Pool();

    @Modifying
    @Transactional
    @Query(value = "SELECT message_id FROM item where size45 = true and item_type = \"shoes\" ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    long[] shoes45Pool();
}
