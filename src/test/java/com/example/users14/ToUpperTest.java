package com.example.users14;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class) // тестирование спрингового приложения
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// запускаем приложение не на порту 8080

public class ToUpperTest {
    @Value(value = "${local.server.port}") // настройка порта из application.properties
    private int port; // порт на котором запущено приложение

    @Autowired
    private TestRestTemplate restTemplate; // создаваемый на время тестирования экземпляр класса,
                                          // через который можно посылать http запросы

    @Test
    public void testHello() throws JSONException {

        ResponseEntity<String> result = restTemplate.getForEntity(
                "http://localhost:" + port + "/upper?text=hello",
                String.class
        );

        JSONObject json = new JSONObject(result.getBody());
        assertTrue(json.has("hello"));
        assertEquals(json.get("hello"), "HELLO");
    }

}
