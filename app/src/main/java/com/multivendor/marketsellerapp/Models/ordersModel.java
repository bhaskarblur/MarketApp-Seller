package com.multivendor.marketsellerapp.Models;

import java.util.ArrayList;

public class ordersModel {
    public String getOrderstats() {
        return orderstats;
    }

    public void setOrderstats(String orderstats) {
        this.orderstats = orderstats;
    }

    public String getOrderseller() {
        return orderseller;
    }

    public void setOrderseller(String orderseller) {
        this.orderseller = orderseller;
    }

    public String getOrderlocat() {
        return orderlocat;
    }

    public void setOrderlocat(String orderlocat) {
        this.orderlocat = orderlocat;
    }

    private String ordertickid;

    public String getOrdertickid() {
        return ordertickid;
    }

    public void setOrdertickid(String ordertickid) {
        this.ordertickid = ordertickid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public ArrayList<String> getOrderitemdetails() {
        return orderitemdetails;
    }

    public void setOrderitemdetails(ArrayList<String> orderitemdetails) {
        this.orderitemdetails = orderitemdetails;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public ordersModel() {

    }
    public ordersModel(String ordertickid, String paymentstatus, String paymentmethod, String orderdate, String orderstats, Integer amount, ArrayList<String> orderitemdetails, String orderseller, String orderlocat) {
        this.ordertickid = ordertickid;
        this.paymentstatus = paymentstatus;
        this.paymentmethod = paymentmethod;
        this.orderdate = orderdate;
        this.orderstats = orderstats;
        Amount = amount;
        this.orderitemdetails = orderitemdetails;
        this.orderseller = orderseller;
        this.orderlocat = orderlocat;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    private String paymentstatus;
    private String paymentmethod;
    private String orderdate;
    private String orderstats;
    private Integer Amount;
    private ArrayList<String> orderitemdetails;
    private String orderseller;
    private String orderlocat;
}
