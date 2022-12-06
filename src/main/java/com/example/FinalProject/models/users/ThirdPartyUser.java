package com.example.FinalProject.models.users;

import jakarta.persistence.Entity;

@Entity
public class ThirdPartyUser extends User{

    private String name;
    private String hashedKey;

    public ThirdPartyUser(String username, String password, String name, String hashedKey) {
        super(username, password);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public ThirdPartyUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
