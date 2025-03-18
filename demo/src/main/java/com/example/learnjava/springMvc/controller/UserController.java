package com.example.learnjava.springMvc.controller;

import com.example.learnjava.springMvc.model.User;
import com.example.learnjava.springMvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API: Lấy danh sách User
    @GetMapping
    public List<User> getAllUsers() {
        System.err.println("🚀 API: Lấy danh sách User");
        return userService.getAllUsers();
    }

    // API: Thêm User mới
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // API: Lấy User theo ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable int id) {
        System.out.println("🚀 API: Lấy User theo ID : ");
        return userService.getUserById(id);
    }
}
