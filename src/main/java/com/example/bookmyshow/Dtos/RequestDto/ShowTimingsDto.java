package com.example.bookmyshow.Dtos.RequestDto;

import lombok.Data;

import java.util.Date;

@Data
public class ShowTimingsDto {
    private Date date;
    private Integer theaterId;
    private Integer movieId;
}
