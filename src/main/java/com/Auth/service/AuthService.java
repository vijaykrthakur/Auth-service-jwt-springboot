package com.Auth.service;



import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Auth.dto.AuthResponse;
import com.Auth.dto.LoginRequest;
import com.Auth.dto.RegisterRequest;
import com.Auth.entity.RefreshToken;
import com.Auth.entity.Role;
import com.Auth.entity.TokenBlacklist;
import com.Auth.entity.User;
import com.Auth.exceptions.BadRequestException;
import com.Auth.exceptions.InvalidTokenException;
import com.Auth.exceptions.UserNotFoundException;
import com.Auth.mapper.UserMapper;
import com.Auth.repository.RefreshTokenRepository;
import com.Auth.repository.RoleRepository;
import com.Auth.repository.TokenBlacklistRepository;
import com.Auth.repository.UserRepository;
import com.Auth.utils.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

		private final UserRepository userRepo;
	    private final RoleRepository roleRepo;
	    private final PasswordEncoder encoder;
	    private final JwtService jwtService;
	    private final AuthenticationManager authManager;
	    private final RefreshTokenRepository refreshRepo;
	    private final TokenBlacklistRepository blacklistRepo;
	    private final UserMapper mapper;
    
    
    
    
    
    
    

    // 🔥 REGISTER USER
    public AuthResponse register(RegisterRequest request){

        // check email already exist
        if(userRepo.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already registered");
        }

        // convert dto -> entity using mapstruct
        User user = mapper.toEntity(request);

        // encrypt password
        user.setPassword(encoder.encode(request.getPassword()));

        // set default role USER
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);


        // save user
        userRepo.save(user);

        return login(new LoginRequest(request.getEmail(), request.getPassword()));
    }
    
    
    
    
    
    
    
    
    
    

    // 🔥 LOGIN USER
    public AuthResponse login(LoginRequest req){

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(), req.getPassword()
                )
        );

        User user = userRepo.findByEmail(req.getEmail())
        		.orElseThrow(() -> new UserNotFoundException("User not found"));

        String access = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), List.of()
                )
        );

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setUser(user);
        refresh.setExpiryDate(Instant.now().plus(7,ChronoUnit.DAYS));
        refreshRepo.save(refresh);

        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refresh.getToken())
                .role(user.getRoles().iterator().next().getName())
                .build();
    }

    public void logout(String token){
        TokenBlacklist b = new TokenBlacklist();
        b.setToken(token);
        b.setBlacklistedAt(Instant.now());
        blacklistRepo.save(b);
    }
    
    

    
    // 🔥 GENERATE NEW ACCESS TOKEN USING REFRESH TOKEN
    public AuthResponse refreshToken(String refreshToken){

        RefreshToken oldToken = refreshRepo.findByToken(refreshToken)
        		.orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if(oldToken.getExpiryDate().isBefore(Instant.now())){
            refreshRepo.delete(oldToken);
            throw new InvalidTokenException("Refresh token expired. Login again");
        }

        User user = oldToken.getUser();

        // 🔴 DELETE OLD REFRESH TOKEN (rotation)
        refreshRepo.delete(oldToken);

        // 🟢 CREATE NEW REFRESH TOKEN
        RefreshToken newRefresh = new RefreshToken();
        newRefresh.setToken(UUID.randomUUID().toString());
        newRefresh.setUser(user);
        newRefresh.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshRepo.save(newRefresh);

        // 🟢 CREATE NEW ACCESS TOKEN
        String newAccess = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority(r.getName()))
                                .toList()
                )
        );

        return AuthResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh.getToken()) // return new refresh
                .role(user.getRoles().iterator().next().getName())
                .build();
    }


    
    
    
}
