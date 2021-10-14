package com.example.bookingapp.model;

import com.google.gson.annotations.SerializedName;

public class STKPush {

    @SerializedName("BusinessShortCode")
    private final String businessShortCode;
    @SerializedName("Password")
    private final String password;
    @SerializedName("Timestamp")
    private final String timestamp;
    @SerializedName("TransactionType")
    private final String transactionType;
    @SerializedName("Amount")
    private final String amount;
    @SerializedName("PartyA")
    private final String partyA;
    @SerializedName("PartyB")
    private final String partyB;
    @SerializedName("PhoneNumber")
    private final String phoneNumber;
    @SerializedName("CallBackURL")
    private final String callBackURL;
    @SerializedName("AccountReference")
    private final String accountReference;
    @SerializedName("TransactionDesc")
    private final String transactionDesc;

    public STKPush(String businessShortCode, String password, String timestamp, String transactionType,
                   String amount, String partyA, String partyB, String phoneNumber, String callBackURL,
                   String accountReference, String transactionDesc) {
        this.businessShortCode = businessShortCode;
        this.password = password;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
        this.partyA = partyA;
        this.partyB = partyB;
        this.phoneNumber = phoneNumber;
        this.callBackURL = callBackURL;
        this.accountReference = accountReference;
        this.transactionDesc = transactionDesc;
    }
}
