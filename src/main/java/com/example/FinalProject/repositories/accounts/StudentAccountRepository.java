package com.example.FinalProject.repositories.accounts;

import com.example.FinalProject.models.accounts.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
}
