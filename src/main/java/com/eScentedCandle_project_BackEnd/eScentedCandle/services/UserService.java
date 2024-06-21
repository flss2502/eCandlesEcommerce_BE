package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AuthenticationDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ChangePassWordDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UpdateUserDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UserDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.InvalidParamException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    ResponseEntity<RegisterResponse> register(UserDto userDto, BindingResult bindingResult) ;

    UserResponses login(AuthenticationDto authenticationDto) throws DataNotFoundException;

    void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Page<UserResponseDTO> getAllUsers(PageRequest pageRequest);


    ListObjectResponse search(String keyword, Long id);


    ListUserResponse updateUser(Long id, UpdateUserDTO userDto) throws InvalidParamException;

    ObjectResponse deleteUser(Long id) throws AccessDeniedException;

    ForgotPasswordResponse forgotPassword(String email);



    ResponseEntity<ResetPassWordResponse> resetPassword(String email,String otp, String newPassword);

    ResponseEntity<ChangePassWordResponse> changePassword(ChangePassWordDTO request, Principal connectedUser) throws IllegalAccessException;
}
