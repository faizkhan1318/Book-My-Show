package com.example.bookmyshow.Transformers;

import com.example.bookmyshow.Dtos.RequestDto.MovieEntryDto;
import com.example.bookmyshow.Models.Movie;

public class MovieTransformer {

    public static Movie convertDtoToEntity(MovieEntryDto movieEntryDto){

        Movie movie = Movie.builder().movieName(movieEntryDto.getMovieName())
                .duration(movieEntryDto.getDuration())
                .rating(movieEntryDto.getRating())
                .genre(movieEntryDto.getGenre())
                .language(movieEntryDto.getLanguage())
                .releaseDate(movieEntryDto.getReleaseDate()).build();

        return movie;
    }
}
