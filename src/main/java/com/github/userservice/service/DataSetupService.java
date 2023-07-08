package com.github.userservice.service;

import com.github.userservice.entities.User;
import com.github.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class DataSetupService implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        final User user1 = new User("sam", 10000);
        final User user2 = new User("mike", 10000);
        final User user3 = new User("jake", 10000);

        Flux.just(user1, user2, user3)
                .flatMap(userRepository::save)
                .doOnNext(System.out::println)
                .subscribe();
    }
}
