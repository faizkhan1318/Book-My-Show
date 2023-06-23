package com.example.bookmyshow.Transformers;

import com.example.bookmyshow.Dtos.RequestDto.TheaterEntryDto;
import com.example.bookmyshow.Models.Theater;

public class TheaterTransformer {

    public static Theater convertDtoToEntity(TheaterEntryDto theaterEntryDto){
        //write a builder annotation top of that class which are building
        Theater theater = Theater.builder().
                location(theaterEntryDto.getLocation()).
                name(theaterEntryDto.getName()).build();

        return theater;
    }
}
