package com.ktb.community.service;

import com.ktb.community.dto.login.LoginRequestDto;
import com.ktb.community.dto.login.LoginResponseDto;
import com.ktb.community.dto.password.PasswordUpdateRequestDto;
import com.ktb.community.dto.user.SignUpRequestDto;
import com.ktb.community.dto.user.SignUpResponseDto;
import com.ktb.community.dto.user.UserResponseDto;
import com.ktb.community.dto.user.UserUpdateRequestDto;
import com.ktb.community.entity.User;
import com.ktb.community.exception.ApiException;
import com.ktb.community.exception.ErrorCode;
import com.ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public SignUpResponseDto signup(SignUpRequestDto request) {

        if(userRepository.existsByEmail(request.getEmail())
                || userRepository.existsByNickname(request.getNickname())) {
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImage()
        );

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getUserId());

    }


    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if(!user.getPassword().equals(request.getPassword())) {
            throw new ApiException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = "token-user-" + user.getUserId();

        return new LoginResponseDto(user.getUserId(), accessToken);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMyPage(User loginUser, Long userId) {

        if(!loginUser.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        return new UserResponseDto(loginUser.getUserId(), loginUser.getNickname(), loginUser.getEmail(), loginUser.getProfileImage());
    }

    public void updateUser(User loginUser, Long userId, UserUpdateRequestDto request) {

        if(!loginUser.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        loginUser.update(request.getNickname(), request.getProfileImage());
    }

    public void updatePassword(User loginUser, Long userId, PasswordUpdateRequestDto request) {

        if(!loginUser.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        if(!loginUser.getPassword().equals(request.getCurPassword())) {
            throw new ApiException(ErrorCode.INVALID_PASSWORD);
        }

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }

        loginUser.updatePassword(request.getPassword());
    }

    public void deleteUser(User loginUser, Long userId) {

        if(!loginUser.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_USER);
        }

        if(loginUser.isDeleted()) {
            throw new ApiException(ErrorCode.ALREADY_DELETED);
        }

        loginUser.delete();
    }
}
