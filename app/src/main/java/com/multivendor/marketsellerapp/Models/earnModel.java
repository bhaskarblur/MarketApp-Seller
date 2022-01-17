package com.multivendor.marketsellerapp.Models;

public class earnModel {
    private String pername;    // august , 2021 , march or whatever

    public String getPername() {
        return pername;
    }

    public void setPername(String pername) {
        this.pername = pername;
    }

    public Integer getEarncount() {
        return earncount;
    }

    public void setEarncount(Integer earncount) {
        this.earncount = earncount;
    }
    public earnModel() {

    }

    public earnModel(String pername, Integer earncount) {
        this.pername = pername;
        this.earncount = earncount;
    }

    private Integer earncount;

}
