package com.example.FinalProject.repositories.accounts;

import com.example.FinalProject.models.accounts.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
}
