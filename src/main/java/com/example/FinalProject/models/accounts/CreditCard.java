package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity
public class CreditCard extends Account{

    @DecimalMin("0.1")
    @DecimalMax("0.2")
    private BigDecimal interestRate;
    @Min(100)
    @Max(100000)
    private BigDecimal creditLimit;

    public CreditCard() {
        this.interestRate = BigDecimal.valueOf(0.2D);
        this.creditLimit = BigDecimal.valueOf(100D);
    }

    public CreditCard(BigDecimal balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
        this.interestRate = BigDecimal.valueOf(0.2D);
        this.creditLimit = BigDecimal.valueOf(100D);
    }


    public CreditCard(BigDecimal balance, AccountHolder primaryOwner, BigDecimal interestRate, BigDecimal creditLimit) {
        super(balance, primaryOwner);
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
    }

    public CreditCard(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}
