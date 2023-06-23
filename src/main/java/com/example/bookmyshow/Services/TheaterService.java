package com.example.bookmyshow.Services;

import com.example.bookmyshow.Dtos.RequestDto.TheaterEntryDto;
import com.example.bookmyshow.Models.Theater;
import com.example.bookmyshow.Repository.TheaterRepository;
import com.example.bookmyshow.Transformers.TheaterTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public String addTheater(TheaterEntryDto theaterEntryDto){
        //entity that saves into DB
        //convert entry dto to --> entity
        Theater theater = TheaterTransformer.convertDtoToEntity(theaterEntryDto);
        theaterRepository.save(theater);

        return "Theater added Successfully";
    }
}
