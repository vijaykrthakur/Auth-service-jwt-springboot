package com.Auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Auth.repository.TokenBlacklistRepository;
import com.Auth.utils.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userService;
    private final TokenBlacklistRepository blacklistRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");

        if(auth == null || !auth.startsWith("Bearer ")){
            chain.doFilter(request,response);
            return;
        }

        String token = auth.substring(7);

        if(blacklistRepo.existsByToken(token)){
            chain.doFilter(request,response);
            return;
        }

        String email = jwtService.extractUsername(token);
        UserDetails user = userService.loadUserByUsername(email);

        if(jwtService.isValid(token,user)){
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user,null,user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request,response);
    }
}
