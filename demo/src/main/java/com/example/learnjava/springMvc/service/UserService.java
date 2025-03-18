package com.example.learnjava.springMvc.service;

import com.example.learnjava.springMvc.model.User;
import com.example.learnjava.springMvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách tất cả User
    public List<User> getAllUsers() {
        System.out.println("🚀 getAllUsers");

        return userRepository.findAll();
    }

    // Thêm một User mới
    public User createUser(User user) {
        System.out.println("🚀 Thêm User mới");

        return userRepository.save(user);
    }

    // Tìm User theo ID
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
