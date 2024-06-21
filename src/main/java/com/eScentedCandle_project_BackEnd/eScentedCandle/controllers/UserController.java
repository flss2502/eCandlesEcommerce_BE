package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AuthenticationDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ChangePassWordDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UpdateUserDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UserDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.InvalidParamException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@CrossOrigin

public class UserController {
    private final UserService userService;
    private final LogoutHandler logoutHandler;

    @PostMapping("")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        return userService.register(userDto, bindingResult);
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponses> login(@RequestBody AuthenticationDto authenticationDto) {
        try {
            return ResponseEntity.ok(userService.login(authenticationDto));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    UserResponses.builder().message(e.getMessage()).build()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    UserResponses.builder().message(e.getMessage()).build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PutMapping("/verify-account")
//    public ResponseEntity<String> verifyAccount(@RequestParam String email,
//                                                @RequestParam String otp) {
//        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
//    }

    @PutMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<ChangePassWordResponse> changePassword(@RequestBody ChangePassWordDTO request, Principal connectedUser) throws IllegalAccessException {
        return userService.changePassword(request, connectedUser);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResetPassWordResponse> resetPassword(@RequestParam String email,
                                                               @RequestParam String otp,
                                                               @RequestHeader String newPassword) {
        return  userService.resetPassword(email, otp,newPassword);
    }

    @PostMapping("/refreshToken")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        userService.refresh(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(
                    ObjectResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Logout successful")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ObjectResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Logout failed")
                            .build()
            );
        }
    }


    @GetMapping("/search")
    public ResponseEntity<ListObjectResponse> search(@RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) Long id) {
        try {
            ListObjectResponse listObjectResponse = userService.search(keyword, id);
            HttpStatus status = HttpStatus.valueOf(listObjectResponse.getStatus());
            return ResponseEntity.status(status)
                    .body(listObjectResponse);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ListUserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO userDto) throws InvalidParamException {
        ListUserResponse updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ObjectResponse> updateUser(@PathVariable Long id) throws InvalidParamException, AccessDeniedException {
        ObjectResponse updatedUser = userService.deleteUser(id);
        return ResponseEntity.ok(updatedUser);
    }


    @GetMapping("/getUsers")
    public ResponseEntity<ResponseObject<Page<UserResponseDTO>>> getAllUsers(
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "size", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "fullName") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<UserResponseDTO> userPage = userService.getAllUsers(pageRequest);
        if (page >= userPage.getTotalPages()) {
            ResponseObject<Page<UserResponseDTO>> responseObject = new ResponseObject<>(
                    400,
                    "Page number out of range",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
        }
        ResponseObject<Page<UserResponseDTO>> responseObject = new ResponseObject<>(
                200,
                "Data fetched successfully",
                userPage,
                null
        );
        return ResponseEntity.ok(responseObject);

    }
}
