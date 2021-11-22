package com.gorkemsavran.config.security;

import com.gorkemsavran.common.util.JwtUtil;
import com.gorkemsavran.config.userdetails.CustomUserDetailsService;
import com.gorkemsavran.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value(value = "${security.jwt.secret-key}")
    private String secretKey;

    private final CustomUserDetailsService userDetailsManager;

    public JwtRequestFilter(CustomUserDetailsService userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authentication = request.getHeader("Authorization");
        if (authentication != null && authentication.startsWith("Bearer")) {
            String jwtToken = authentication.substring(7);
            String username = JwtUtil.extractUsername(jwtToken, secretKey);

            User userDetails = (User) userDetailsManager.loadUserByUsername(username);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
