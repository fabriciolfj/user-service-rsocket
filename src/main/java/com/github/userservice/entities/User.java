package com.github.userservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private int balance;

    public User(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
}
