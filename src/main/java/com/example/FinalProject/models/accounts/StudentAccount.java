package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class StudentAccount extends Account{

    @NotNull
    private String secretKey;
    @NotNull
    private LocalDate creatingDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;


    public StudentAccount() {
        this.status = Status.ACTIVE;
    }

    public StudentAccount(BigDecimal balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.creatingDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public StudentAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.creatingDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDate getCreatingDate() {
        return creatingDate;
    }

    public void setCreatingDate(LocalDate creatingDate) {
        this.creatingDate = creatingDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
