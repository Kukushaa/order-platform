package com.kukusha.users_shared_lib.service;

import com.kukusha.users_shared_lib.dto.RegisterRequestDTO;
import com.kukusha.users_shared_lib.model.User;
import com.kukusha.users_shared_lib.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public void register(RegisterRequestDTO requestDTO) {
        User user = new User();

        user.setEnabled(true);
        user.setRole(User.Roles.ROLE_USER);
        user.setUsername(requestDTO.username());
        user.setName(requestDTO.name());
        user.setSurname(requestDTO.surname());
        user.setEmail(requestDTO.email());
        user.setPassword(passwordEncoder.encode(requestDTO.password()));

        save(user);
    }

    public void save(User user) {
        repository.save(user);
    }
}
