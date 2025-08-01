package com.asc.function;

import java.util.function.Function;
import org.springframework.stereotype.Component;
import com.asc.domain.model.Greeting;
import com.asc.domain.model.User;

@Component
public class Hello implements Function<User, Greeting> {

    @Override
    public Greeting apply(User user) {
        return new Greeting("Hello, " + user.getName() + "!\n");
    }
}
