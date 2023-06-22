package com.example.bookmyshow.Dtos.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private int age;
    private String name;
    private String mobNo;

    private String statusCode;
    private String statusMessage;

}
