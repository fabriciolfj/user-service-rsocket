package com.github.userservice;

import com.github.userservice.dto.OperationType;
import com.github.userservice.dto.TransactionRequest;
import com.github.userservice.dto.TransactionResponse;
import com.github.userservice.dto.TransactionStatus;
import com.github.userservice.dto.TransactionType;
import com.github.userservice.dto.UserDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTransactionTest {

    private RSocketRequester requester;

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setRequester() {
        this.requester = this.builder
                .transport(TcpClientTransport.create("localhost", 7071));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void transactionTest(int amount, TransactionType type, TransactionStatus status) {
        final var userDto = this.getRandomUser();

        final TransactionRequest request = new TransactionRequest(userDto.getId(), amount, type);
        var mono = this.requester.route("user.transaction")
                .data(request)
                .retrieveMono(TransactionResponse.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextMatches(p -> p.getStatus().equals(status))
                .verifyComplete();
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(2000, TransactionType.CREDIT, TransactionStatus.COMPLETED),
                Arguments.of(2000, TransactionType.DEBIT, TransactionStatus.COMPLETED),
                Arguments.of(12000, TransactionType.DEBIT, TransactionStatus.FAILED));
    }

    private UserDto getRandomUser() {
        return this.requester.route("user.get.all")
                .retrieveFlux(UserDto.class)
                .next()
                .block();
    }
}
