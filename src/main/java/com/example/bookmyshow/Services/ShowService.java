package com.example.bookmyshow.Services;

import com.example.bookmyshow.Dtos.RequestDto.AddShowDto;
import com.example.bookmyshow.Dtos.RequestDto.ShowSeatsDto;
import com.example.bookmyshow.Dtos.RequestDto.ShowTimingsDto;
import com.example.bookmyshow.Enums.SeatType;
import com.example.bookmyshow.Exception.MovieNotFound;
import com.example.bookmyshow.Exception.ShowNotFound;
import com.example.bookmyshow.Exception.TheaterNotFound;
import com.example.bookmyshow.Models.*;
import com.example.bookmyshow.Repository.MovieRepository;
import com.example.bookmyshow.Repository.ShowRepository;
import com.example.bookmyshow.Repository.TheaterRepository;
import com.example.bookmyshow.Transformers.ShowTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private MovieRepository movieRepository;

    public String addShow(AddShowDto showDto) throws MovieNotFound, TheaterNotFound {
        Show show = ShowTransformer.convertDtoToEntity(showDto);
        Optional<Movie> movieOptional = movieRepository.findById(showDto.getMovieId());
        if(movieOptional.isEmpty()){
            throw new MovieNotFound("Movie Not Found");
        }
        Optional<Theater> theaterOptional = theaterRepository.findById(showDto.getMovieId());
        if(theaterOptional.isEmpty()){
            throw new TheaterNotFound("Theater Not Found");
        }

        Movie movie = movieOptional.get();
        Theater theater = theaterOptional.get();

        //setting the movie theater entity as FK
        show.setMovie(movie);
        show.setTheater(theater);

        //and after that save the show because parent of these show entity save twice
        show = showRepository.save(show);

        //adding the bidirectional relation
        movie.getShowList().add(show);
        movieRepository.save(movie);


        theater.getShowList().add(show);
        theaterRepository.save(theater);

        return "Show has been Added and ShowId is" + show.getId();

    }

    public String associateSeats(ShowSeatsDto showSeatsDto) throws ShowNotFound{
        Optional<Show> showOptional = showRepository.findById(showSeatsDto.getShowId());
        if(showOptional.isEmpty()){
            throw new ShowNotFound("showId is incorrect");
        }
        Show show = showOptional.get();
        Theater theater = show.getTheater();

        List<TheaterSeat> theaterSeatList = theater.getTheaterSeatList();
        List<ShowSeat> showSeatList = show.getShowSeatList();

        for(TheaterSeat theaterSeat : theaterSeatList){
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeatNo(theaterSeat.getSeatNo());
            showSeat.setSeatType(theaterSeat.getSeatType());

            if(showSeat.getSeatType().equals(SeatType.CLASSIC))
                showSeat.setPrice(showSeatsDto.getPriceForClassicSeats());
            else
                showSeat.setPrice(showSeatsDto.getPriceForPremiumSeats());

            showSeat.setShow(show);
            showSeat.setAvailable(true);
            showSeat.setFoodAttached(false);

            showSeatList.add(showSeat);
        }
        showRepository.save(show);

        return "Show seats has been successfully added";
    }

    public String movieHavingMostShows() {
        Integer movieId = showRepository.getMostShowsMovies();
        return movieRepository.findById(movieId).get().getMovieName();
    }

    public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
        Date date = showTimingsDto.getDate();
        Integer theaterId = showTimingsDto.getTheaterId();
        Integer movieId = showTimingsDto.getMovieId();
        return showRepository.getShowTimingsOnDate(date, theaterId, movieId);
    }
}
