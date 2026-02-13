package com.kukusha.users_service.controller;

import com.kukusha.users_service.dto.UserResponse;
import com.kukusha.users_service.dto.UserUpdateDTO;
import com.kukusha.users_shared_lib.model.User;
import com.kukusha.users_shared_lib.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping(value = "/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        String username = principal.getName();
        Optional<User> byUsername = usersService.findByUsername(username);

        if (byUsername.isPresent()) {
            return new ResponseEntity<>(new UserResponse(byUsername.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("message", "User not found " + username), HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid UserUpdateDTO userUpdateDTO,
                                        Principal principal) {
        String username = principal.getName();
        Optional<User> byUsername = usersService.findByUsername(username);

        if (byUsername.isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "User not found " + username), HttpStatus.NOT_FOUND);
        }

        User user = byUsername.get();

        Optional.ofNullable(userUpdateDTO.getFirstname())
                .filter(StringUtils::hasLength)
                .ifPresent(user::setName);

        Optional.ofNullable(userUpdateDTO.getSurname())
                .filter(StringUtils::hasLength)
                .ifPresent(user::setSurname);

        usersService.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
