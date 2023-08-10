package com.example.TechWearBot.model.UserSizeTable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Size {

    @Id
    private Long chatId;

    private Integer bootSize;

    private String outfitSize;

    private String lastCompilation;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getBootSize() {
        return bootSize;
    }

    public void setBootSize(Integer bootSize) {
        this.bootSize = bootSize;
    }

    public String getOutfitSize() {
        return outfitSize;
    }

    public void setOutfitSize(String outfitSize) {
        this.outfitSize = outfitSize;
    }

    public String getLastCompilation() {
        return lastCompilation;
    }

    public void setLastCompilation(String lastCompilation) {
        this.lastCompilation = lastCompilation;
    }

    @Override
    public String toString() {
        return "Size{" +
                "chatId=" + chatId +
                ", bootSize=" + bootSize +
                ", outfitSize='" + outfitSize + '\'' +
                ", lastCompilation='" + lastCompilation + '\'' +
                '}';
    }
}
