package com.example.learnjava.springmvc.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private int age;
    private String email;
}
