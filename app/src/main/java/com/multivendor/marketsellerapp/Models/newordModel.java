package com.multivendor.marketsellerapp.Models;

import java.util.ArrayList;

public class newordModel {
    private String buyername;
    public newordModel() {

    }
    public newordModel(String buyername, String buyeraddr, Integer amount, ArrayList<String> items) {
        this.buyername = buyername;
        this.buyeraddr = buyeraddr;
        this.amount = amount;
        this.items = items;
    }

    private String buyeraddr;
    private Integer amount;
    private ArrayList<String> items;

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getBuyeraddr() {
        return buyeraddr;
    }

    public void setBuyeraddr(String buyeraddr) {
        this.buyeraddr = buyeraddr;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
