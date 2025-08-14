package com.washintontech.app.controller;

import com.washintontech.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client/user/")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

//    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
//        return Mono.just(request)
//                .doOnEach(logOnNext(req -> log.debug("Received request: {}", request)))
//                .flatMap(userService::register);
//    }
}
