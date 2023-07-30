package com.example.TechWearBot.model.ItemTable;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Item {

    @Id
    private Integer messageId;

    private String itemType;

    private Boolean sizeS;

    private Boolean sizeM;

    private Boolean sizeL;

    private Boolean sizeXL;

    private Boolean sizeXXL;

    private Boolean size36;

    private Boolean size37;

    private Boolean size38;

    private Boolean size39;

    private Boolean size40;

    private Boolean size41;

    private Boolean size42;

    private Boolean size43;

    private Boolean size44;

    private Boolean size45;


    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Boolean getSizeS() {
        return sizeS;
    }

    public void setSizeS(Boolean sizeS) {
        this.sizeS = sizeS;
    }

    public Boolean getSizeM() {
        return sizeM;
    }

    public void setSizeM(Boolean sizeM) {
        this.sizeM = sizeM;
    }

    public Boolean getSizeL() {
        return sizeL;
    }

    public void setSizeL(Boolean sizeL) {
        this.sizeL = sizeL;
    }

    public Boolean getSizeXL() {
        return sizeXL;
    }

    public void setSizeXL(Boolean sizeXL) {
        this.sizeXL = sizeXL;
    }

    public Boolean getSizeXXL() {
        return sizeXXL;
    }

    public void setSizeXXL(Boolean sizeXXL) {
        this.sizeXXL = sizeXXL;
    }

    public Boolean getSize36() {
        return size36;
    }

    public void setSize36(Boolean size36) {
        this.size36 = size36;
    }

    public Boolean getSize37() {
        return size37;
    }

    public void setSize37(Boolean size37) {
        this.size37 = size37;
    }

    public Boolean getSize38() {
        return size38;
    }

    public void setSize38(Boolean size38) {
        this.size38 = size38;
    }

    public Boolean getSize39() {
        return size39;
    }

    public void setSize39(Boolean size39) {
        this.size39 = size39;
    }

    public Boolean getSize40() {
        return size40;
    }

    public void setSize40(Boolean size40) {
        this.size40 = size40;
    }

    public Boolean getSize41() {
        return size41;
    }

    public void setSize41(Boolean size41) {
        this.size41 = size41;
    }

    public Boolean getSize42() {
        return size42;
    }

    public void setSize42(Boolean size42) {
        this.size42 = size42;
    }

    public Boolean getSize43() {
        return size43;
    }

    public void setSize43(Boolean size43) {
        this.size43 = size43;
    }

    public Boolean getSize44() {
        return size44;
    }

    public void setSize44(Boolean size44) {
        this.size44 = size44;
    }

    public Boolean getSize45() {
        return size45;
    }

    public void setSize45(Boolean size45) {
        this.size45 = size45;
    }

    @Override
    public String toString() {
        return "Item{" +
                "messageId=" + messageId +
                ", itemType='" + itemType + '\'' +
                ", sizeS=" + sizeS +
                ", sizeM=" + sizeM +
                ", sizeL=" + sizeL +
                ", sizeXL=" + sizeXL +
                ", sizeXXL=" + sizeXXL +
                ", size36=" + size36 +
                ", size37=" + size37 +
                ", size38=" + size38 +
                ", size39=" + size39 +
                ", size40=" + size40 +
                ", size41=" + size41 +
                ", size42=" + size42 +
                ", size43=" + size43 +
                ", size44=" + size44 +
                ", size45=" + size45 +
                '}';
    }
}
