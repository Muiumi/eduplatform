package ru.rtstudy.educplatformsecurity.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.auth.AuthenticationService;
import ru.rtstudy.educplatformsecurity.auth.JwtService;
import ru.rtstudy.educplatformsecurity.dto.mapper.impl.UserMapper;
import ru.rtstudy.educplatformsecurity.dto.request.JwtRefreshToken;
import ru.rtstudy.educplatformsecurity.dto.request.SignInRequest;
import ru.rtstudy.educplatformsecurity.dto.request.SignUpRequest;
import ru.rtstudy.educplatformsecurity.dto.response.JwtTokenDto;
import ru.rtstudy.educplatformsecurity.dto.response.SuccessfulSignInDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.exception.entity.UserNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.user.UserAlreadyExistsException;
import ru.rtstudy.educplatformsecurity.exception.user.UserNotRegisterException;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.model.constant.Role;
import ru.rtstudy.educplatformsecurity.repository.UserRepository;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;
    private final Util util;

    @Override
    public UserDtoResponse signUp(SignUpRequest request) {
        log.info("{} trying to sign up.", request.getEmail());
        Optional<User> userExists = userRepository.findUserByEmail(request.getEmail());
        if (userExists.isEmpty()) {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_STUDENT)
                    .build();
            user = userRepository.save(user);
            return mapper.toDto(user);
        } else {
            log.error("User already exists.", new UserAlreadyExistsException("User already exists."));
            throw new UserAlreadyExistsException("User already exists.");
        }
    }

    @Override
    public SuccessfulSignInDto signIn(SignInRequest request) {
        log.info("{} trying to sign in", request.getEmail());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))
                .getPrincipal();
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Invalid email or password: {}", request.getEmail(), new IllegalArgumentException("Invalid email or password"));
                    return new IllegalArgumentException("Invalid email or password");
                });
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return SuccessfulSignInDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .surname(user.getLastName())
                .role(user.getRole())
                .accessToken(jwt)
                .accessExpiration(jwtService.extractExpiration(jwt))
                .refreshToken(refreshToken)
                .refreshExpiration(jwtService.extractExpiration(refreshToken))
                .build();
    }

    @Override
    public boolean hasCredentialToDelete(String fileName) {
        log.info("{} verification to delete filename: {}", util.findUserFromContext().getEmail(), fileName);
        Long userId = util.findUserFromContext().getId();
        return userRepository.hasCredential(fileName, userId);
    }

    @Override
    public boolean isAuthor(Long userId) {
        log.info("User by id: {} verification on author credential", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User by id: {} not found", userId);
                    return new UserNotFoundException("User not found.");
                })
                .getRole()
                .equals(Role.ROLE_AUTHOR);
    }

    @Override
    public JwtTokenDto refreshToken(JwtRefreshToken jwtRefreshToken) {
        log.info("WE ARE HERE!");
        String userEmail = jwtService.extractUser(jwtRefreshToken.refreshToken());
        if (userEmail != null) {
            User user = userRepository.findUserByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User was not found"));
            if (jwtService.isTokenValid(jwtRefreshToken.refreshToken(), user)) {
                String accessToken = jwtService.generateToken(user);
               return JwtTokenDto.builder()
                       .token(accessToken)
                       .build();
            }
        } else {
            throw new UserNotRegisterException("User not register");
        }
        return null;
    }
}