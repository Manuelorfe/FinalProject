package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
public class CheckingAccount extends Account{

    private final BigDecimal MINIMUM_BALANCE;
    private final BigDecimal MONTHLY_MAINTENANCE_FEE;
    @NotNull
    private String secretKey;
    @NotNull
    private LocalDate creationDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public CheckingAccount() {
        this.status = Status.ACTIVE;
        MINIMUM_BALANCE = BigDecimal.valueOf(250D);
        MONTHLY_MAINTENANCE_FEE = BigDecimal.valueOf(12D);
    }

    public CheckingAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey, LocalDate creationDate) {
        super(balance,primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.status = Status.ACTIVE;
        MONTHLY_MAINTENANCE_FEE = BigDecimal.valueOf(12D);
        MINIMUM_BALANCE = BigDecimal.valueOf(250D);
    }

    public CheckingAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, LocalDate creationDate) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.status = Status.ACTIVE;
        MONTHLY_MAINTENANCE_FEE = BigDecimal.valueOf(12D);
        MINIMUM_BALANCE = BigDecimal.valueOf(250D);
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

    public BigDecimal getMINIMUM_BALANCE() {
        return MINIMUM_BALANCE;
    }

    public BigDecimal getMONTHLY_MAINTENANCE_FEE() {
        return MONTHLY_MAINTENANCE_FEE;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        if(balance.compareTo(MINIMUM_BALANCE) == -1 ){
            balance = balance.subtract(super.getPENALTY_FEE());
        }
        super.setBalance(balance);
    }
}