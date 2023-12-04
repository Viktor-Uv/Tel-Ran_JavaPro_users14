package com.example.users14;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring") // спринг автоматически создаст экземпляр при старте
public interface UserMapper {
    // https://mapstruct.org/

    // Convert field's name
    @Mapping(source = "numberOfYears", target = "age")
    // Convert field with function
    @Mapping(source = "numberOfYears", target = "adult", qualifiedByName = "yearsToAdult")
    // Convert firstName and lastName into fullName with function
    @Mapping(source = ".", target = "name", qualifiedByName = "fullName")
    User fromDtoToUser(UserDto userDto);

    @Named("fullName")
    default String fullName(UserDto userDto) {
        return String.format("%s %s", userDto.getFirstName(), userDto.getLastName());
    }

    @Named("yearsToAdult")
    default Boolean yearsToAdult(int numberOfYears) {
        return numberOfYears >= 18;
    }
}
