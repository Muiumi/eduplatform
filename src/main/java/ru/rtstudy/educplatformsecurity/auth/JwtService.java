package ru.rtstudy.educplatformsecurity.auth;

import org.springframework.security.core.userdetails.UserDetails;
import ru.rtstudy.educplatformsecurity.model.User;

import java.util.Date;

public interface JwtService {

    String extractUser(String token);

    Date extractExpiration(String token);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);
}
