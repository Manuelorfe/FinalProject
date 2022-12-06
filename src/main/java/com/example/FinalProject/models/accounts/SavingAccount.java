package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
public class SavingAccount extends Account{

    @NotNull
    private String secretKey;
    @NotNull
    private LocalDate creationDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Max(1000)
    @Min(100)
    private BigDecimal minimumBalance;
    @DecimalMax("0.5")
    private BigDecimal interestRate;


    public SavingAccount() {
        this.interestRate = BigDecimal.valueOf(0.0025D);
        this.status = Status.ACTIVE;
        minimumBalance = BigDecimal.valueOf(1000D);
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.interestRate = BigDecimal.valueOf(0.0025D);
        this.status = Status.ACTIVE;
        minimumBalance = BigDecimal.valueOf(1000D);
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate, BigDecimal minimumBalance , BigDecimal interestRate) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.status = Status.ACTIVE;
    }

    public SavingAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, LocalDate creationDate, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
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

    @Override
    public void setBalance(BigDecimal balance) {

        if(balance.compareTo(minimumBalance) == -1 ){
            balance = balance.subtract(super.getPENALTY_FEE());
        }
        super.setBalance(balance);
    }
}
