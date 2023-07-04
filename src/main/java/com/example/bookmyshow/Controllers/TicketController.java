package com.example.bookmyshow.Controllers;

import com.example.bookmyshow.Dtos.RequestDto.TicketRequestDto;
import com.example.bookmyshow.Dtos.ResponseDto.TicketResponseDto;
import com.example.bookmyshow.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/book-ticket")
    public ResponseEntity<TicketResponseDto> bookTicket(@RequestBody TicketRequestDto ticketRequestDto){
        try{
            TicketResponseDto response = ticketService.bookTicket(ticketRequestDto);
            response.setResponseStatus("Ticket Booked Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            TicketResponseDto response =  new TicketResponseDto();
            response.setResponseStatus(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
