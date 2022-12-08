package com.example.FinalProject.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfiguration {


    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/admin/add-third-party-user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/add-admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/admin/change-balance/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/admin/get-users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/admin/add-mailing-adress-ah/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/update-ah/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/remove-user/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/create-bank-account").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/create-saving-account").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/create-credit-card").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/account-holder/my-accounts/**").hasRole("ACCOUNTHOLDER")
                .requestMatchers(HttpMethod.GET, "/account-holder/my-balance/**").hasRole("ACCOUNTHOLDER")
                .requestMatchers(HttpMethod.POST, "/account-holder/transference").hasRole("ACCOUNTHOLDER")
                .requestMatchers(HttpMethod.POST, "/third-party/transference").hasRole("THIRDPARTY")
                .anyRequest().permitAll();

        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

}