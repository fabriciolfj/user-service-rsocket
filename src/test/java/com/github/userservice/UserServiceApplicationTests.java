package com.github.userservice;

import com.github.userservice.dto.TransactionRequest;
import com.github.userservice.dto.TransactionResponse;
import com.github.userservice.dto.TransactionType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	void transactionTest() {
		var request = builder.transport(TcpClientTransport.create("localhost", 7073));

		var result = request.route("user.transaction")
				.data(TransactionRequest.builder()
						.userId("001")
						.amount(10)
						.type(TransactionType.CREDIT)
						.build()).retrieveMono(TransactionResponse.class)
				.doOnNext(System.out::println);

		StepVerifier.create(result)
				.expectNextCount(1)
				.verifyComplete();

	}

}
