package com.github.userservice.service;

import com.github.userservice.dto.UserDto;
import com.github.userservice.repository.UserRepository;
import com.github.userservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<UserDto> getAllUsers() {
        return this.userRepository.findAll()
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> getUserById(final String userId) {
        return this.userRepository.findById(userId)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> createUser(final Mono<UserDto> mono) {
        return mono.map(EntityDtoUtil::toEntity)
                .flatMap(userRepository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> updateUser(final String id, Mono<UserDto> userDtoMono) {
        return userRepository.findById(id)
                .flatMap(i -> userDtoMono.map(EntityDtoUtil::toEntity)
                        .doOnNext(s -> s.setId(id)))
                .flatMap(userRepository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<Void> deleteUser(final String id) {
        return this.userRepository.deleteById(id);
    }

}
