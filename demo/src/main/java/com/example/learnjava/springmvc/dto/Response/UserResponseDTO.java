package com.example.learnjava.springmvc.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private int age;
    private String email;
}
