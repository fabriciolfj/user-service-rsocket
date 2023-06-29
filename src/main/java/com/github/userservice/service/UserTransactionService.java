package com.github.userservice.service;

import com.github.userservice.dto.TransactionRequest;
import com.github.userservice.dto.TransactionResponse;
import com.github.userservice.dto.TransactionStatus;
import com.github.userservice.dto.TransactionType;
import com.github.userservice.entities.User;
import com.github.userservice.repository.UserRepository;
import com.github.userservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final UserRepository userRepository;

    public Mono<TransactionResponse> doTransaction(final TransactionRequest request) {
        var operation = TransactionType.CREDIT.equals(request.getType()) ? credit(request) : debit(request);
        return this.userRepository.findById(request.getUserId())
                .transform(operation)
                .flatMap(this.userRepository::save)
                .map(u -> EntityDtoUtil.toResponse(request, TransactionStatus.COMPLETED))
                .defaultIfEmpty(EntityDtoUtil.toResponse(request, TransactionStatus.FAILED));
    }

    private UnaryOperator<Mono<User>> credit(final TransactionRequest request) {
        return userMono -> userMono
                .doOnNext(u -> u.setBalance(u.getBalance() + request.getAmount()));
    }

    private UnaryOperator<Mono<User>> debit(final TransactionRequest request) {
        return userMono -> userMono
                .filter(u -> u.getBalance() >= request.getAmount())
                .doOnNext(u -> u.setBalance(u.getBalance() - request.getAmount()));
    }
}
