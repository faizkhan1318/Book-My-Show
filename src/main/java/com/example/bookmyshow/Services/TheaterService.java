package com.example.bookmyshow.Services;

import com.example.bookmyshow.Dtos.RequestDto.TheaterEntryDto;
import com.example.bookmyshow.Dtos.RequestDto.TheaterSeatsEntryDto;
import com.example.bookmyshow.Enums.SeatType;
import com.example.bookmyshow.Models.Theater;
import com.example.bookmyshow.Models.TheaterSeat;
import com.example.bookmyshow.Repository.TheaterRepository;
import com.example.bookmyshow.Transformers.TheaterTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    @Autowired
    TheaterRepository theaterRepository;

    public String addTheater(TheaterEntryDto theaterEntryDto){
        //entity that saves into DB
        //convert entry dto to --> entity
        Theater theater = TheaterTransformer.convertDtoToEntity(theaterEntryDto);
        theaterRepository.save(theater);

        return "Theater added Successfully";
    }
    public String addTheaterSeats(TheaterSeatsEntryDto theaterSeatsEntryDto){
        int columns = theaterSeatsEntryDto.getNoOfSeatsInRow();
        int noOfClassicSeats = theaterSeatsEntryDto.getNoOfClassicSeats();
        int noOfPremiumSeats = theaterSeatsEntryDto.getNoOfPremiumSeats();

        //find the theater entity
        String location = theaterSeatsEntryDto.getLocation();
        Theater theater = theaterRepository.findByLocation(location);

        List<TheaterSeat> theaterSeatList =theater.getTheaterSeatList();

        int counter=1;
        char ch='A';
        // for classic seats
        for(int count=1;count<=noOfClassicSeats; count++){
            String seatNo = counter + " ";
            seatNo = seatNo + ch; // 1A 1B 1C
            ch++; //increase the character in column

            if((ch-'A')==columns){
                ch='A';
                counter++;
            }
            TheaterSeat theaterSeat = new TheaterSeat();
            theaterSeat.setTheater(theater);
            theaterSeat.setSeatType(SeatType.CLASSIC);
            theaterSeat.setSeatNo(seatNo);

            // doing bidirectional mapping  storing the child entity in the parent entity
            theaterSeatList.add(theaterSeat);
        }

        // this is for premium seats
        for(int count=1; count<=noOfPremiumSeats; count++){
            String seatNo = counter + " ";
            seatNo = seatNo + ch; // 1A 1B 1C
            ch++; //increase the character in column

            if((ch-'A')==columns){
                ch='A';
                counter++;
            }
            TheaterSeat theaterSeat = new TheaterSeat();
            theaterSeat.setTheater(theater);
            theaterSeat.setSeatType(SeatType.PREMIUM);
            theaterSeat.setSeatNo(seatNo);

            // doing bidirectional mapping  storing the child entity in the parent entity
            theaterSeatList.add(theaterSeat);

        }
        //we just need to save parent : theater
        //child automatically save

        theaterRepository.save(theater);

        return "Theater Seats Save Successfully";

    }
}
