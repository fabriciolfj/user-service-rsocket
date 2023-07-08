package com.github.userservice.controller;

import com.github.userservice.dto.OperationType;
import com.github.userservice.dto.UserDto;
import com.github.userservice.entities.User;
import com.github.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @MessageMapping("get.all")
    public Flux<UserDto> allUsers() {
        return this.service.getAllUsers();
    }

    @MessageMapping("get.{id}")
    public Mono<UserDto> getUserById(@DestinationVariable final String id) {
        return this.service.getUserById(id);
    }

    @MessageMapping("create")
    public Mono<UserDto> createUser(final Mono<UserDto> userDtoMono) {
        return this.service.createUser(userDtoMono);
    }

    @MessageMapping("update.{id}")
    public Mono<UserDto> updateUser(@DestinationVariable final String id, Mono<UserDto> userMono) {
        return this.service.updateUser(id, userMono);
    }

    @MessageMapping("delete.{id}")
    public Mono<Void> deleteUser(@DestinationVariable final String id) {
        return this.service.deleteUser(id);
    }

    @MessageMapping("operation.type")
    public Mono<Void> metadataOperationType(@Header("operation-type") final OperationType type, Mono<UserDto> userDtoMono) {
        System.out.println(type);

        userDtoMono.doOnNext(System.out::println).subscribe();
        return Mono.empty();
    }

}
