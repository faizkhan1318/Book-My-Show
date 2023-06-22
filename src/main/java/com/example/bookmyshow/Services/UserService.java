package com.example.bookmyshow.Services;

import com.example.bookmyshow.Dtos.RequestDto.AddUserDto;
import com.example.bookmyshow.Dtos.ResponseDto.UserResponseDto;
import com.example.bookmyshow.Exception.NoUserFoundException;
import com.example.bookmyshow.Models.User;
import com.example.bookmyshow.Repository.UserRepository;
import com.example.bookmyshow.Transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String addUser(AddUserDto userDto){
        User user = UserTransformer.convertDtoToEntity(userDto);
        userRepository.save(user);

        return "User Added Successfully";
    }
    public UserResponseDto getOldestUser()throws NoUserFoundException{
        //prevent u from exposing PK
        //prevent infinite recursion if occur

        List<User> userList = userRepository.findAll();
        Integer maxAge=0;

        User userAns=null;
        for(User user : userList){
            if(user.getAge()>maxAge){
                maxAge= user.getAge();
                userAns=user;
            }
        }
        if(userAns==null){
            throw new NoUserFoundException("No User Found");
        }
        // now I need to transform the user to userResponseDto
        UserResponseDto userResponseDto = UserTransformer.convertEntityToDto(userAns);
        return userResponseDto;
    }

    public List<User> getAllUSerGreaterThan(Integer age){
        List<User> userList = userRepository.findUserWithAgeGreater(age);
        return userList;
    }



}
