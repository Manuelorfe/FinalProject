package com.example.FinalProject.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ThirdPartyTransactionDTO {

   private Long thirdPartyId;
   private String hashedKey;
   private BigDecimal amount;
   private Long accountId;
   private String secretKey;

   private LocalDateTime transactionDate = LocalDateTime.now();

    public ThirdPartyTransactionDTO(Long thirdPartyId, String hashedKey, BigDecimal amount, Long accountId, String secretKey) {
        this.thirdPartyId = thirdPartyId;
        this.hashedKey = hashedKey;
        this.amount = amount;
        this.accountId = accountId;
        this.secretKey = secretKey;
    }

    public Long getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(Long thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ThirdPartyTransactionDTO() {
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}