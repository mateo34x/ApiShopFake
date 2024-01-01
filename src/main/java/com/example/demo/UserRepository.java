package com.example.demo;

import com.example.demo.domain.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<Users,String> {

    Users findItemByEmail(String email);
}
