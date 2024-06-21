package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query("""
                    SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id\s
                    WHERE u.id = :id AND (t.expired = FALSE OR t.revoked = FALSE)
                                                                                        
            """)
    List<Token> findAllUserTokenByUserId(long id);



    Optional<Token> findByToken(String token);


}
