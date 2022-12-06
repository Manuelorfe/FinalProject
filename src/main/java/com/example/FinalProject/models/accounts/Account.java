package com.example.FinalProject.models.accounts;

import com.example.FinalProject.models.users.AccountHolder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final BigDecimal PENALTY_FEE = BigDecimal.valueOf(40D);
    @NotNull
    @Min(0)
    private BigDecimal balance;
    @ManyToOne
    @NotNull
    private AccountHolder primaryOwner;
    @ManyToOne
    private AccountHolder secondaryOwner;

    @OneToMany(mappedBy = "accountSender", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Transaction> listSenders = new ArrayList<>();
    @OneToMany(mappedBy = "accountReceiver", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Transaction> listReceivers = new ArrayList<>();

    public Account(BigDecimal balance, AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
    }

    public Account(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getPENALTY_FEE() {
        return PENALTY_FEE;
    }

    public List<Transaction> getListSenders() {
        return listSenders;
    }

    public void setListSenders(List<Transaction> listSenders) {
        this.listSenders = listSenders;
    }

    public List<Transaction> getListReceivers() {
        return listReceivers;
    }

    public void setListReceivers(List<Transaction> listReceivers) {
        this.listReceivers = listReceivers;
    }
}
