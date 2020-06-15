package com.payping.springsample.model;

public class CreatePayRequestModel {

    public int amount;
    public String payerIdentity;
    public String payerName;
    public String description;
    public String clientRefId = "";
    public String returnUrl = "";

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPayerIdentity() {
        return payerIdentity;
    }

    public void setPayerIdentity(String payerIdentity) {
        this.payerIdentity = payerIdentity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientRefId() {
        return clientRefId;
    }

    public void setClientRefId(String clientRefId) {
        this.clientRefId = clientRefId;
    }
}
