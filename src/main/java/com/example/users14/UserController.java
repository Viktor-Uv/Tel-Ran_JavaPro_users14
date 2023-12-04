package com.example.users14;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/users")
    public String addUser(
            @Valid // будут выполнены проверки валидаций из Entity - NonBlank, Email
                  // ещё до выполнения тела метода
            @RequestBody User user
    ) {
        userRepo.save(user);
        return "User created";
    }


    // Если пользователь не проходит валидацию в UserController, срабатывает обработчик исключения, которое
    // мы перехватываем в методе handleValidationErrors, делаем некоторый Json, и возвращаем его обратно
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) // делает метод реакцией на ошибку
    public Map<String, String> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(
                error ->
                        errors.put(
                                ((FieldError) error).getField(),
                                error.getDefaultMessage()
                        )
        );
        return errors;
        /*
        Result:
            {
                "field": "Error",
                "field1": "Error #1",
            }
         */
    }

    @Autowired
    private CalcService calcService;

    // GET http://localhost:8080/sum?a=5&b=9
    @GetMapping("/sum")
    public int sum(
            @RequestParam(defaultValue = "0") int a,
            @RequestParam(defaultValue = "0") int b
    ) {
        return calcService.sum(a, b);
    }

    // GET http://localhost:8080/upper?text=hello
    @GetMapping("/upper")
    public Map<String, String> toUpper(
            @RequestParam String text
    ) {
        Map<String, String> result = new HashMap<>();
        String toUpper = text == null ? "" : text.toUpperCase();
        result.put(text, toUpper);
        return result;
    }


    @Autowired
    private UserService userService;

    // POST http://localhost:8080/check
    @PostMapping("/check")
    public Boolean CheckUser(
            @RequestBody UserDto userDto
    ) {
        return userService.isAdult(userDto);
    }
}
