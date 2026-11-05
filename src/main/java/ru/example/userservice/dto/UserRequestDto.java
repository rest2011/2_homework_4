package ru.example.userservice.dto;

import jakarta.validation.constraints.*;

public class UserRequestDto {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Введен некорректный формат Email")
    private String email;

    @NotNull(message = "Возраст должен быть указан")
    @Min(value = 0, message = "Возраст должен быть от 0 до 120")
    @Max(value = 120, message = "Возраст должен быть от 0 до 120")
    private Integer age;

    public UserRequestDto() {}
    public UserRequestDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
