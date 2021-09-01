package com.bjpowernode.licai.model.ext;

import com.bjpowernode.licai.model.IncomeRecord;

import java.io.Serializable;

public class IncomeInfo extends IncomeRecord implements Serializable {

    private static final long serialVersionUID = -8578473922444521656L;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
