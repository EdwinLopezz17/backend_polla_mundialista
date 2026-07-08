package com.polla.demo.controllers;

import com.polla.demo.models.User;
import com.polla.demo.models.dtos.UserDTO;
import com.polla.demo.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getFullName(), user.getBaseScore()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getFullName(), user.getBaseScore())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam Long id, @RequestParam String pin) {
        return userRepository.findById(id)
                .map(user -> {
                    if (user.getPin().equals(pin)) {
                        return ResponseEntity.ok("Login successful. Welcome " + user.getFullName());
                    } else {
                        return ResponseEntity.status(401).body("Invalid PIN");
                    }
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }
}