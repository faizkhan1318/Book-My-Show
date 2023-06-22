package com.example.bookmyshow.Transformers;

import com.example.bookmyshow.Dtos.RequestDto.AddUserDto;
import com.example.bookmyshow.Dtos.ResponseDto.UserResponseDto;
import com.example.bookmyshow.Models.User;

public class UserTransformer {

    public static User convertDtoToEntity(AddUserDto userDto){
//        User user = new User();
//
//       user.setName(userDto.getName());
//       user.setAge(userDto.getAge());
//       user.setEmail(userDto.getEmailId());
//       user.setMobNo(userDto.getMobNo());

       User userObj = User.builder().age(userDto.getAge()).name(userDto.getName()).email(userDto.getEmailId())
               .mobNo(userDto.getMobNo()).build();

       return userObj;
    }

    public static UserResponseDto convertEntityToDto(User user){

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .age(user.getAge())
                .name(user.getName())
                .mobNo(user.getMobNo())
                .build();

        return userResponseDto;
    }
}
