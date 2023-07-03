package com.example.bookmyshow.Services;

import com.example.bookmyshow.Dtos.RequestDto.TicketRequestDto;
import com.example.bookmyshow.Dtos.ResponseDto.TicketResponseDto;
import com.example.bookmyshow.Exception.NoUserFoundException;
import com.example.bookmyshow.Exception.ShowNotFound;
import com.example.bookmyshow.Models.Show;
import com.example.bookmyshow.Models.ShowSeat;
import com.example.bookmyshow.Models.Ticket;
import com.example.bookmyshow.Models.User;
import com.example.bookmyshow.Repository.ShowRepository;
import com.example.bookmyshow.Repository.TicketRepository;
import com.example.bookmyshow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public TicketResponseDto bookTicket(TicketRequestDto ticketRequestDto) throws NoUserFoundException, ShowNotFound, Exception {
        int userId = ticketRequestDto.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new NoUserFoundException("User ID is incorrect");
        }
        int showId = ticketRequestDto.getShowId();
        Optional<Show> showOptional = showRepository.findById(showId);
        if(showOptional.isEmpty()){
            throw new ShowNotFound("Show is not found");
        }
        Show show = showOptional.get();
        //validation for requested seats are available or not
        boolean isValid = validateRequestedAvailability(show, ticketRequestDto.getRequestedSeats());
        if(isValid==false){
            throw new Exception("Requested seats are not available");
        }
        Ticket ticket = new Ticket();

        //calculate the total price of requested seats
        int totalPrice = calculatePrice(show, ticketRequestDto.getRequestedSeats());
        ticket.setTotalTicketPrice(totalPrice);

        //convert the list of seats to string
        String bookedSeats = convertToString(ticketRequestDto.getRequestedSeats());
        ticket.setBookedSeats(bookedSeats);

        // do bidirectional mapping
        User user = userOptional.get();

        ticket.setUser(user);
        ticket.setShow(show);

        ticket = ticketRepository.save(ticket);

        user.getTicketList().add(ticket);
        userRepository.save(user);

        //saving the relevant repository
        show.getTicketList().add(ticket);
        showRepository.save(show);

        return createTicketResponseDto(show, ticket);
    }

    private TicketResponseDto createTicketResponseDto(Show show, Ticket ticket) {
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .bookedSeats(ticket.getBookedSeats())
                .location(show.getTheater().getLocation())
                .theaterName(show.getTheater().getName())
                .movieName(show.getMovie().getMovieName())
                .showDate(show.getDate())
                .showTime(show.getTime())
                .build();
        return ticketResponseDto;
    }


    private boolean validateRequestedAvailability(Show show, List<String> requestedSeats){
        List<ShowSeat> showSeatList = show.getShowSeatList();

        for(ShowSeat showSeat : showSeatList){
            String seatNo = showSeat.getSeatNo();
            if(requestedSeats.contains(seatNo) && showSeat.isAvailable()==false){
                return false;
            }
        }
        return true;
    }
    private int calculatePrice(Show show, List<String> requestedSeats) {
        int price = 0;
        List<ShowSeat> showSeatList = show.getShowSeatList();
        for(ShowSeat showSeat : showSeatList){
            if(requestedSeats.contains(showSeat.getSeatNo())){
                price = price+showSeat.getPrice();
                //this seat is not available
                showSeat.setAvailable(false);
            }
        }
        return price;
    }
    private String convertToString(List<String> requestedSeats) {
        String result = "";
        for(String seatNo : requestedSeats){
            result = result + seatNo + ", ";
        }
        return result;
    }
}
