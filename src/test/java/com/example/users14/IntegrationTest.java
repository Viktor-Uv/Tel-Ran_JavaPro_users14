package com.example.users14;

/*
    Виды тестов:
        Интеграционный тест - запускает полностью спринг приложение со всеми его компонентами (ресурсоёмкий)
            Цепочка:
                передача чего-то в контроллер
                прохождение валидации
                переход в сервис
                переход через репозитори в базу данных
                переход в сервис и обработка
                возврат данных в контроллер


        Тест бизнес-логики - не требует поднятия всего приложения, тестируются отдельные его компоненты
            JUnit-ом проверяются возвращаемые методами объекты


        Тест отдельных компонентов (кусков) приложения
            частичный запуск
 */

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class) // тестирование спрингового приложения (по умолчанию используется Runner JUnit-а,
                            // который не знает как запускать спринговые приложения)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // запускаем приложение не на порту
                                                                        // 8080 (to avoid possible conflicts)
public class IntegrationTest {
    @Value(value = "${local.server.port}") // настройка порта из application.properties
    private int port; // порт на котором запущено приложение

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TestRestTemplate restTemplate; // создаваемый на время тестирования экземпляр класса,
                                           // через который можно посылать http запросы

    @Test
    public void createUser() {
        HttpHeaders headers = new HttpHeaders(); // заголовки http запроса
        headers.add("content-type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(
                "{\"name\": \"rob\", \"email\": \"rob@gmail.com\"}",
                headers
        );

        assertEquals(userRepo.count(), 0);

        // посылаем POST запрос в Spring приложение и получаем ответ
        String result = restTemplate.postForEntity(
                "http://localhost:" + port + "/users",
                request,
                String.class
        ).getBody();

        assertEquals(userRepo.count(), 1);
        assertEquals(result, "User created");
    }

    @Test
    public void checkEmptyName() throws JSONException {
        HttpHeaders headers = new HttpHeaders(); // заголовки http запроса
        headers.add("content-type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(
                "{\"email\": \"rob@gmail.com\"}",
                headers
        );

        // посылаем POST запрос в Spring приложение и получаем ответ
        ResponseEntity<String> result = restTemplate.postForEntity(
                "http://localhost:" + port + "/users",
                request,
                String.class
        );

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        String body = result.getBody();
        // {"name": "Name is mandatory"}
        JSONObject json = new JSONObject(body); // преобразуем возвращаемую строку в json
        assertTrue(json.has("name"));
        assertEquals(json.get("name"), "Name is mandatory");
    }

    // напишите тест который проверит что при посылке json вида
    // {"age":12, "email":"max"}
    // приходят правильные ответы
    // { "name": "Name is mandatory", "email", "Email must be valid" }
    @Test
    public void checkNameAndEmail() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(
                "{\"age\": \"12\", \"email\": \"max\"}",
                headers
        );

        // посылаем post запрос в приложение и получаем ответ
        ResponseEntity<String> result = restTemplate.postForEntity(
                "http://localhost:" + port + "/users",
                request,
                String.class
        );

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        String body = result.getBody();

        JSONObject json = new JSONObject(body);
        assertTrue(json.has("name"));
        assertEquals(json.get("name"), "Name is mandatory");
        assertTrue(json.has("email"));
        assertEquals(json.get("email"), "Email must be valid");
    }

}
