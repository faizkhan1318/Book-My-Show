package com.example.bookmyshow.Transformers;

import com.example.bookmyshow.Dtos.RequestDto.AddShowDto;
import com.example.bookmyshow.Models.Show;

public class ShowTransformer {

    public static Show convertDtoToEntity(AddShowDto showDto){
        Show show = Show.builder().time(showDto.getShowStartTime())
                .date(showDto.getShowDate()).build();

        return show;
    }
}
