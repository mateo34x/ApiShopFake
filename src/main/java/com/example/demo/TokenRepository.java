package com.example.demo;

import com.example.demo.domain.Tokens;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Tokens,String> {

}
