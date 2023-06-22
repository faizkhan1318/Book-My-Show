package com.example.bookmyshow.Controllers;

import com.example.bookmyshow.Dtos.RequestDto.AddUserDto;
import com.example.bookmyshow.Dtos.ResponseDto.UserResponseDto;
import com.example.bookmyshow.Models.User;
import com.example.bookmyshow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody AddUserDto userDto){

        try{
            String result = userService.addUser(userDto);
            return result;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @GetMapping("/getoldestuser")
    public UserResponseDto getOldestUser(){
        try{
           UserResponseDto userResponseDto =userService.getOldestUser();
           userResponseDto.setStatusCode("200");
           userResponseDto.setStatusMessage("SUCCESS");
           return userResponseDto;
        }catch (Exception e){
            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setStatusCode("500");
            responseDto.setStatusMessage("FAILURE");
            return responseDto;
        }
    }

    @GetMapping("/findUsersGreaterThanAAge")
    public List<User> getALlUsers(@RequestParam("age") Integer age){
        return userService.getAllUSerGreaterThan(age);
    }
}
