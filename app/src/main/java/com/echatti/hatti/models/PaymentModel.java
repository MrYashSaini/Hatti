package com.echatti.hatti.models;

public class PaymentModel {
    String NoOfProduct,Amount,Pay,Date,Time,due;
    int orderId;


    public PaymentModel() {
    }

    public PaymentModel(String noOfProduct, String amount, String pay, String date, String time) {
        this.NoOfProduct = noOfProduct;
        this.Amount = amount;
        this.Pay = pay;
        this.Date = date;
        this.Time = time;
    }

    public PaymentModel(String noOfProduct, String amount, String pay, String date, String time, String due, int orderId) {
        NoOfProduct = noOfProduct;
        Amount = amount;
        Pay = pay;
        Date = date;
        Time = time;
        this.due = due;
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getNoOfProduct() {
        return NoOfProduct;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setNoOfProduct(String noOfProduct) {
        NoOfProduct = noOfProduct;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPay() {
        return Pay;
    }

    public void setPay(String pay) {
        Pay = pay;
    }

}

