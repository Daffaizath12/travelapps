package com.example.travelapps.Model;

public class TransactionModel {
    private String statusCode;
    private String statusMessage;
    private String transactionId;
    private String maskedCard;
    private String orderId;
    private String paymentType;
    private String transactionTime;
    private String transactionStatus;
    private String fraudStatus;
    private String approvalCode;
    private String signatureKey;
    private Bank bankInfo; // Objek bank yang berisi bank dan vaNumber
    private String grossAmount;
    private String channelResponseCode;
    private String channelResponseMessage;
    private String cardType;
    private String paymentOptionType;
    private String shopeepayReferenceNumber;
    private String referenceId;

    // Constructor
    public TransactionModel(String statusCode, String statusMessage, String transactionId, String maskedCard, String orderId, String paymentType, String transactionTime, String transactionStatus, String fraudStatus, String approvalCode, String signatureKey, Bank bankInfo, String grossAmount, String channelResponseCode, String channelResponseMessage, String cardType, String paymentOptionType, String shopeepayReferenceNumber, String referenceId) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.maskedCard = maskedCard;
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.fraudStatus = fraudStatus;
        this.approvalCode = approvalCode;
        this.signatureKey = signatureKey;
        this.bankInfo = bankInfo;
        this.grossAmount = grossAmount;
        this.channelResponseCode = channelResponseCode;
        this.channelResponseMessage = channelResponseMessage;
        this.cardType = cardType;
        this.paymentOptionType = paymentOptionType;
        this.shopeepayReferenceNumber = shopeepayReferenceNumber;
        this.referenceId = referenceId;
    }

    // Getter methods
    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public Bank getBankInfo() {
        return bankInfo;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getChannelResponseCode() {
        return channelResponseCode;
    }

    public String getChannelResponseMessage() {
        return channelResponseMessage;
    }

    public String getCardType() {
        return cardType;
    }

    public String getPaymentOptionType() {
        return paymentOptionType;
    }

    public String getShopeepayReferenceNumber() {
        return shopeepayReferenceNumber;
    }

    public String getReferenceId() {
        return referenceId;
    }
}

