package com.bjpowernode.vo;

import java.io.Serializable;

public class TopBean implements Serializable {

    private static final long serialVersionUID = -4245391432224808048L;
    private String phone;
    private Double money;

    public TopBean() {
    }

    public TopBean(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
