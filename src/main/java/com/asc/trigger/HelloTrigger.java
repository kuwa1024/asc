package com.asc.trigger;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.asc.domain.model.User;
import com.asc.function.Hello;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

@Component
public class HelloTrigger {

        @Autowired
        private Hello hello;

        @FunctionName("hello")
        public HttpResponseMessage execute(@HttpTrigger(name = "request",
                        methods = {HttpMethod.GET, HttpMethod.POST},
                        authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<User>> request,
                        ExecutionContext context) {
                User user = request.getBody().filter(u -> u.getName() != null)
                                .orElseGet(() -> new User(request.getQueryParameters()
                                                .getOrDefault("name", "world")));
                context.getLogger().info("Greeting user name: " + user.getName());
                return request.createResponseBuilder(HttpStatus.OK).body(hello.apply(user))
                                .header("Content-Type", "application/json").build();
        }
}
