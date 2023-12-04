package com.example.users14;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean isAdult(UserDto userDto) {
        User user = userMapper.fromDtoToUser(userDto);
        log.info("yyyyy " + user);
        return user.getAdult();
    }
}
