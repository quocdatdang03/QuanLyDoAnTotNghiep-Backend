package com.quanlydoantotnghiep.DoAnTotNghiep.security.jwt;


import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

// Execute before Executing Spring Security Filters
// Validate the JWT token and Provides user details to Spring Security for Authentication
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            // get JWT token from HTTP request
            String jwtToken = getTokenFromRequest(request);

            // Validate token
            if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {

                // get username from token
                String username = jwtTokenProvider.getUsername(jwtToken);

                // get user object from DB by username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu thông tin của authentication để dùng cho những request tiếp theo trong phiên Session mà không cần bắt login lại
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        }
        catch (ApiException ex) {
            response.setStatus(ex.getHttpStatus().value());
            response.setContentType("application/json");
            response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now() + "\", \"message\":\"" + ex.getMessage() + "\"}");
            return; // Stop filter chain processing
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        // get JWT token from Header of request :
        String bearerToken = request.getHeader("Authorization"); // format of bearer token : Bearer ...jwtToken

        // check bearerToken != null and not blank
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

            // get jwtToken from Request Header
            String jwtToken = bearerToken.substring(7);

            return jwtToken;
        }

        return null;

    }
}
