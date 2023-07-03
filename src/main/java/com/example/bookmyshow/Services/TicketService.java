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
        Ticket tickett = new Ticket();

        //calculate the total price of requested seats
        int totalPrice = calculatePrice(show, ticketRequestDto.getRequestedSeats());
        tickett.setTotalTicketPrice(totalPrice);

        //convert the list of seats to string
        String bookedSeats = convertToString(ticketRequestDto.getRequestedSeats());
        tickett.setBookedSeats(bookedSeats);

        // do bidirectional mapping
        User user = userOptional.get();

        tickett.setUser(user);
        tickett.setShow(show);

        tickett = ticketRepository.save(tickett);

        user.getTicketList().add(tickett);
        userRepository.save(user);

        //saving the relevant repository
        show.getTicketList().add(tickett);
        showRepository.save(show);

        return createTicketResponseDto(show, tickett);
    }

    private TicketResponseDto createTicketResponseDto(Show show, Ticket ticket) {
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .bookedSeats(ticket.getBookedSeats())
                .location(show.getTheater().getLocation())
                .theaterName(show.getTheater().getName())
                .movieName(show.getMovie().getMovieName())
                .showDate(show.getDate())
                .showTime(show.getTime())
                .totalPrice(ticket.getTotalTicketPrice())
                .build();
        return ticketResponseDto;
    }


    private boolean validateRequestedAvailability(Show show, List<String> requestedSeats){
        List<ShowSeat> showSeatList = show.getShowSeatList();

        for(ShowSeat showSeat : showSeatList){
            String seatNo = showSeat.getSeatNo();
            if(requestedSeats.contains(seatNo)){
                if(showSeat.isAvailable()==false)
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
                price = price + showSeat.getPrice();
                System.out.println(price);
                //this seat is not available
                showSeat.setAvailable(false);
            }
        }
        System.out.println(price);
        return price;
    }
    String convertToString(List<String> requestedSeats) {
        String result = "";
        for(String seatNo : requestedSeats){
            result = result + seatNo + ", ";
        }
        return result;
    }
}
