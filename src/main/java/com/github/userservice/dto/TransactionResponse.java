package com.github.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {

    private String userId;
    private int amount;
    private TransactionType type;
    private TransactionStatus status;
}
