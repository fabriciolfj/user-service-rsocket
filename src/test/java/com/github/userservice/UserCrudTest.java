package com.github.userservice;

import com.github.userservice.dto.OperationType;
import com.github.userservice.dto.UserDto;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import jdk.dynalink.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserCrudTest {

    private RSocketRequester requester;

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setRequester() {
        this.requester = this.builder
                .transport(TcpClientTransport.create("localhost", 7071));
    }

    @Test
    void getAllUsersTest() {
        var flux = this.requester.route("user.get.all")
                .retrieveFlux(UserDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectComplete();
    }

    @Test
    void getSingleUsersTest() {
        final var userDto = this.getRandomUser();

        var mono = this.requester.route("user.get.{id}", userDto.getId() )
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextMatches(p -> p.getId().equals(userDto.getId()))
                .verifyComplete();
    }

    @Test
    void postUserTest() {
        final var userDto = new UserDto();
        userDto.setBalance(999);
        userDto.setName("fabricio");

        var mono = this.requester.route("user.create", userDto.getId() )
                .data(userDto)
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void putUsersTest() {
        final var userDto = this.getRandomUser();
        userDto.setBalance(-10);

        var mono = this.requester.route("user.update.{id}", userDto.getId())
                .data(userDto)
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextMatches(p -> p.getBalance() == userDto.getBalance())
                .verifyComplete();
    }

    @Test
    void deleteUsersTest() {
        final var userDto = this.getRandomUser();

        var mono = this.requester.route("user.delete.{id}", userDto.getId())
                .send();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    void metadataTest() {
        MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.APPLICATION_CBOR.getString());

        UserDto dto = new UserDto();
        dto.setName("teste");
        dto.setBalance(1000);

        var mono = this.requester.route("user.operation.type")
                .metadata(OperationType.PUT, mimeType)
                .data(dto)
                .send();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    private UserDto getRandomUser() {
        return this.requester.route("user.get.all")
                .retrieveFlux(UserDto.class)
                .next()
                .block();
    }
}
