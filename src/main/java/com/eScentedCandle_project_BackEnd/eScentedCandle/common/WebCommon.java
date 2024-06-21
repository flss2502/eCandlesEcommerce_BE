package com.eScentedCandle_project_BackEnd.eScentedCandle.common;

import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebCommon {
    private static final UserRepository userRepository = null;
    private static final JwtService jwtService = null;

    public static String generateCodeFromName(String name) {
        return name.replaceAll("\\s", "-").toLowerCase();
    }

    public static Long extractUserIdFromToken(String token) throws DataNotFoundException {
        String userEmail = jwtService.extractUserEmail(token); // Extract email from token
        User user = userRepository.findByEmail(userEmail) // Find user by email
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        return user.getId();
    }
}
