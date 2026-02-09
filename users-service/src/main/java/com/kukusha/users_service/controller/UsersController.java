package com.kukusha.users_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {
    @GetMapping(value = "/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
    }
}
