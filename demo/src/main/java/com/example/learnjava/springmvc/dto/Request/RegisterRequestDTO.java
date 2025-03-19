package com.example.learnjava.springmvc.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Tuổi không được để trống")
    private int age;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái viết hoa")
    private String password;
}
