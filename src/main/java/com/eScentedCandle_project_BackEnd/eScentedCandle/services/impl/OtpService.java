package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Service
public class OtpService {
    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void saveOtp(String email, String otp) {
        otpStorage.put(email, otp);
        // Lập lịch để loại bỏ OTP sau 5 phút
        executorService.schedule(() -> {
            otpStorage.remove(email);
        }, 5000, TimeUnit.MINUTES);
    }

    public String getOtp(String email) {
        return otpStorage.get(email);
    }

    public boolean isValidOtp(String email, String otp) {
        // Truy xuất mã OTP lưu trữ từ email của người dùng
        String storedOtp = getOtp(email);

        // So sánh mã OTP nhận được từ người dùng với mã OTP lưu trữ
        return otp.equals(storedOtp);
    }


}
