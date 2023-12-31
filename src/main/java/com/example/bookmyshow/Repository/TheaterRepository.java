package com.example.bookmyshow.Repository;

import com.example.bookmyshow.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    //internally runs the query like sql
    public Theater findByLocation(String location);

}
