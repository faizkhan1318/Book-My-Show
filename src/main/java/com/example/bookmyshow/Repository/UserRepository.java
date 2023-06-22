package com.example.bookmyshow.Repository;

import com.example.bookmyshow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users where age >= :age",nativeQuery = true)
    List<User> findUserWithAgeGreater(Integer age);
    //this is a custom function which you need to defined
    //write a query at top of this
    // semicolon : used for a to defind the for which entry or value
    //nativeQuery is used to run like a sql query
}
