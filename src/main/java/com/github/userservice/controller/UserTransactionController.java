package com.github.userservice.controller;

import com.github.userservice.dto.TransactionRequest;
import com.github.userservice.dto.TransactionResponse;
import com.github.userservice.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserTransactionController {

    private final UserTransactionService transactionService;

    @MessageMapping("transaction")
    public Mono<TransactionResponse> toTransaction(final Mono<TransactionRequest> requestMono) {
        return requestMono.flatMap(transactionService::doTransaction);
    }
}
