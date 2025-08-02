package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.example.model.Greeting;
import com.example.model.User;
import reactor.core.publisher.Mono;

public class HelloTest {

    @Test
    public void test() {
        Mono<Greeting> result = new Hello().apply(Mono.just(new User("foo")));
        assertThat(result.block().getMessage()).isEqualTo("Hello, foo!\n");
    }
}
