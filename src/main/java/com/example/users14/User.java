package com.example.users14;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory") // name cannot be empty
    // Validator проверяет данные на стадии создания экземпляра класса, ещё до момента вставки в базу данных,
    // при этом @Column(nullable = false) выдаст Exception уже при вставке в таблицу
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    // @Column() - формирует sql для create table ...
    private String email;

    private Integer age;
    private Boolean adult;
}
