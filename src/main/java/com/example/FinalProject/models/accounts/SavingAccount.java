package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

@Entity
public class SavingAccount extends Account{

    @NotNull
    private String secretKey;
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Max(1000)
    @Min(100)
    private BigDecimal minimumBalance;
    @DecimalMax("0.5")
    @Column(columnDefinition = "decimal(38,4)")
    private BigDecimal interestRate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastInterestApplied = LocalDate.now();


    public SavingAccount() {
        this.interestRate = new BigDecimal("0.0025",new MathContext(4));
        this.status = Status.ACTIVE;
        this.minimumBalance = BigDecimal.valueOf(1000D);
        this.creationDate = LocalDate.now();
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        this.interestRate = BigDecimal.valueOf(0.0025D);
        this.status = Status.ACTIVE;
        minimumBalance = BigDecimal.valueOf(1000D);
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey, BigDecimal minimumBalance , BigDecimal interestRate) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.status = Status.ACTIVE;
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.status = Status.ACTIVE;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public LocalDate getLastInterestApplied() {
        return lastInterestApplied;
    }

    public void setLastInterestApplied(LocalDate lastInterestApplied) {
        this.lastInterestApplied = lastInterestApplied;
    }

}
