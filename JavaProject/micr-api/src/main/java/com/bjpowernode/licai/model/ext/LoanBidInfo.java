package com.bjpowernode.licai.model.ext;

import com.bjpowernode.licai.model.BidInfo;

import java.io.Serializable;

//手机号和投资信息
public class LoanBidInfo extends BidInfo implements Serializable {
    private static final long serialVersionUID = -3296077595312083941L;
    private String phone;

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
