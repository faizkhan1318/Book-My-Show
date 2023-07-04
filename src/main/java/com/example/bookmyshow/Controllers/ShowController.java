package com.example.bookmyshow.Controllers;

import com.example.bookmyshow.Dtos.RequestDto.AddShowDto;
import com.example.bookmyshow.Dtos.RequestDto.ShowSeatsDto;
import com.example.bookmyshow.Dtos.RequestDto.ShowTimingsDto;
import com.example.bookmyshow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    ShowService showService;

    @PostMapping("/add")
    public String addShow(@RequestBody AddShowDto addShowDto){
        try{
            return showService.addShow(addShowDto);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/associate-seats")
    public String associatesSeats(@RequestBody ShowSeatsDto showSeatsDto){
        try{
            return showService.associateSeats(showSeatsDto);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @GetMapping("/showTimingsOnDate")
    public ResponseEntity<List<Time>> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
        try {
            List<Time> result = showService.showTimingsOnDate(showTimingsDto);
            return new ResponseEntity<>(result, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/movieHavingMostShows")
    public ResponseEntity<String> movieHavingMostShows() {
        try {
            String movie = showService.movieHavingMostShows();
            return new ResponseEntity<>(movie, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
