package com.example.users14;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest // не запускается все приложение, а только его часть связанная с JPA и базой данных
public class DatabaseTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Max");
        user.setEmail("max@gmail.com");
        userRepo.save(user);
        assertEquals((long) user.getId(), 1L);
        assertEquals(userRepo.count(), 1L);
    }
}
