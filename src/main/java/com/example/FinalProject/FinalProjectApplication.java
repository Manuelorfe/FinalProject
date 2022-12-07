package com.example.FinalProject;

import com.example.FinalProject.models.users.Admin;
import com.example.FinalProject.models.users.Role;
import com.example.FinalProject.repositories.users.AdminRepository;
import com.example.FinalProject.repositories.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class FinalProjectApplication implements CommandLineRunner {

	@Autowired
	AdminRepository adminRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Admin admin = new Admin("AdminUser", passwordEncoder.encode("123456"), "Manu");
		adminRepository.save(admin);
		roleRepository.save(new Role("ADMIN", admin));
		roleRepository.save(new Role("ACCOUNTHOLDER", admin));
		roleRepository.save(new Role("THIRDPARTY", admin));


	}
}
