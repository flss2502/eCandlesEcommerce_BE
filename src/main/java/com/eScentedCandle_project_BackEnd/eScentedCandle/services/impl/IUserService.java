package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.UserConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AuthenticationDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ChangePassWordDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UpdateUserDTO;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.UserDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.InvalidParamException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Token;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Role;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.TokenType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.TokenRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.JwtService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.UserService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.EmailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailUtil emailUtil;
    private final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(emailRegex);


    @Override
    public ResponseEntity<RegisterResponse> register(UserDto userDto, BindingResult bindingResult) {
        // Kiểm tra lỗi xác nhận
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(createErrorResponse("Validation failed", errors));
        }

        // Kiểm tra xem email đã được sử dụng chưa
        long existingUser = userRepository.countByEmail(userDto.getEmail());
        if (existingUser > 0) {
            return ResponseEntity.badRequest().body(createErrorResponse("Email is already in use", null));
        }

        // Xử lý đăng ký người dùng
        var user = User.builder()
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(user);

        // Phản hồi đăng ký thành công
        return ResponseEntity.ok(
                RegisterResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Register successfully")
                        .register(RegisterResponse.DataRegister.builder()
                                .userResponseDTO(convertToResponseDTO(user))
                                .build())
                        .build()
        );
    }

    // Phương thức trợ giúp để tạo phản hồi lỗi
    private RegisterResponse createErrorResponse(String message, List<String> errors) {
        String errorMessage = message;
        if (errors != null && !errors.isEmpty()) {
            errorMessage = String.join(", ", errors);
        }

        return RegisterResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // Sửa lại thành mã trạng thái 400 Bad Request
                .message(errorMessage)
                .register(RegisterResponse.DataRegister.builder()
                        .userResponseDTO(null)
                        .build())
                .build();
    }


    @Override
    public UserResponses login(AuthenticationDto authenticationDto) throws DataNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDto.getEmail(),
                            authenticationDto.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return UserResponses.builder()
                    .status(HttpStatus.OK.value())
                    .message("Invalid email or password")
                    .data(null)
                    .build();
        }

        var user = userRepository.findByEmail(authenticationDto.getEmail()).orElseThrow(() ->
                new DataNotFoundException("User with email " + authenticationDto.getEmail() + " not found")
        );
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveToken(user, jwtToken);
        revokeAllUser(user);
        return UserResponses.builder()
                .status(HttpStatus.OK.value())
                .message("Login successfully")
                .data(UserResponses.ResponseData.builder()
                        .token(jwtToken)
                        .refreshToken(refreshToken)
                        .user(convertDto(user))
                        .build())
                .build();
    }

    private UserConverter convertDto(User user) {
        return UserConverter.builder()
                .phone(user.getPhoneNumber())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserEmail(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var newToken = jwtService.generateToken(user);
                revokeAllUser(user);
                saveToken(user, newToken);
                var authResponse = AuthenticationResponse.builder()
                        .status("New token")
                        .token(newToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


    @Override
    public Page<UserResponseDTO> getAllUsers(PageRequest pageRequest) {
        Page<User> userResponses = userRepository.findAll(pageRequest);
        return userResponses.map(this::convertToResponseDTO);
    }

    @Override
    public ListObjectResponse search(String keyword, Long id) {
        List<UserResponseDTO> data = null;
        HttpStatus status = HttpStatus.OK;
        String message = "Search successful";

        if (id != null) {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                data = Collections.singletonList(convertToResponseDTO(user));
            } else {
                message = "User not found with id: " + id;
                status = HttpStatus.NOT_FOUND;
            }
        } else if (keyword != null && !keyword.isEmpty()) {
            List<User> users = userRepository.findByFullNameContainingIgnoreCase(keyword);
            data = convertToUserResponseDTO(users);
        } else {
            message = "Invalid search criteria";
            status = HttpStatus.BAD_REQUEST;
        }

        return ListObjectResponse.builder()
                .status(status.value())
                .message(message)
                .data(ListObjectResponse.DataObjectResponse.builder()
                        .list(data)
                        .build())
                .build();
    }


    @Override
    public ListUserResponse updateUser(Long id, UpdateUserDTO userDto)  {
        // Tìm người dùng theo ID
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "User not found");
        }
        // Kiểm tra dữ liệu đầu vào
        if (!StringUtils.hasText(userDto.getEmail()) || !pattern.matcher(userDto.getEmail()).matches()) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid or missing email format.");
        }
        if (!user.getEmail().equals(userDto.getEmail())) {
            long emailCount = userRepository.countByEmail(userDto.getEmail());
            if (emailCount > 0) {
                return createErrorResponse(HttpStatus.BAD_REQUEST, "Email is already in use.");
            }
        }
        if (!StringUtils.hasText(userDto.getPhoneNumber())) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Phone number is required.");
        }
        if (userDto.getPhoneNumber().length() != 10) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid phone number format.");
        }
        if (!StringUtils.hasText(userDto.getFullName())) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Full name is required.");
        }

        // Cập nhật thông tin người dùng
        updateUserInfo(user, userDto);
        userRepository.save(user);
        // Xây dựng phản hồi
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .active(user.isActive())
                .build();
        return ListUserResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Update successful")
                .data(new ListUserResponse.DataResponse(userResponseDTO))
                .build();
    }

    private ListUserResponse createErrorResponse(HttpStatus status, String message) {
        return ListUserResponse.builder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
    }


    @Override
    public ObjectResponse deleteUser(Long id) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return ObjectResponse.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message("You do not have permission to delete users.")
                    .build();
        }
        try {
            User user = userRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("User not found!"));

            user.setActive(false);
            userRepository.save(user);

            return ObjectResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Delete success")
                    .build();
        } catch (ResourceNotFoundException e) {
            return ObjectResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ObjectResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while deleting the user.")
                    .build();
        }
    }

    @Override
    public ForgotPasswordResponse forgotPassword(String email) {
        var user = userRepository.findByEmail(email).orElse(null);
        try {
            emailUtil.sendSetPassword(email);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Unable to send set password email try again");
        }
        return ForgotPasswordResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .forgot(ForgotPasswordResponse.Forgot.builder()
                        .note("Please check your email")
                        .build())
                .build();
    }


    @Override
    public ResponseEntity<ResetPassWordResponse> resetPassword(String email, String otp, String newPassword) {
        try {
            if (email.isEmpty() || otp.isEmpty() || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body(ResetPassWordResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Please enter email, OTP, and new password")
                        .build());
            }
            if (!otpService.isValidOtp(email, otp)) {
                return ResponseEntity.badRequest().body(ResetPassWordResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid OTP")
                        .build());
            }
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return ResponseEntity.ok().body(ResetPassWordResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Reset password successful")
                        .build());
            } else {
                return ResponseEntity.badRequest().body(ResetPassWordResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("User not found")
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResetPassWordResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("An error occurred")
                    .build());
        }
    }


    @Override
    public ResponseEntity<ChangePassWordResponse> changePassword(ChangePassWordDTO request, Principal
            connectedUser) throws IllegalAccessException {
        var user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
        if (!passwordEncoder.matches(request.getCurrentPassWord(), user.getPassword())) {
//            throw new IllegalAccessException("Wrong password");
            return ResponseEntity.badRequest().body(ChangePassWordResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Wrong password")
                    .build());
        }
        if (!request.getNewPassWord().equals(request.getConfirmPasWord())) {
//            throw new IllegalAccessException("Password are not same");
            return ResponseEntity.badRequest().body(ChangePassWordResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("PassWord are not same")
                    .build());
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassWord()));
        userRepository.save(user);
        return ResponseEntity.ok().body(ChangePassWordResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Password success")
                .build());
    }

//    public String regenerateOtp(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        String otp = otpUtil.generateOtp();
//        try {
//            emailUtil.sendOtpEmail(email, otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        } catch (jakarta.mail.MessagingException e) {
//            throw new RuntimeException(e);
//        }
//        user.setOtp(otp);
//        user.setOtpGeneratedTime(LocalDateTime.now());
//        userRepository.save(user);
//        return "Email sent... please verify account within 1 minute";
//    }


    private void updateUserInfo(User user,UpdateUserDTO userDto) {
        if (StringUtils.hasText(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (StringUtils.hasText(userDto.getFullName())) {
            user.setFullName(userDto.getFullName());
        }
        if (StringUtils.hasText(userDto.getPhoneNumber())) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
    }


    private List<UserResponseDTO> convertToUserResponseDTO(List<User> users) {
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
            userResponseDTOs.add(convertToResponseDTO(user));
        }
        return userResponseDTOs;
    }


    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }


    private void saveToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUser(User userExited) {
        var tokenList = tokenRepository.findAllUserTokenByUserId(userExited.getId());
        tokenList.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
    }
}
