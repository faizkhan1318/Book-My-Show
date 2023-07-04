package com.example.bookmyshow.Repository;

import com.example.bookmyshow.Models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Integer> {
    @Query(value = "select movie_id from shows group by movie_id order by count(*) desc limit 1;", nativeQuery = true )
    public Integer getMostShowsMovies();

    @Query(value = "select time from shows where date = :date and movie_id = :movieId and theater_id = :theaterId" , nativeQuery = true)
    List<Time> getShowTimingsOnDate(Date date, Integer theaterId, Integer movieId);
}
