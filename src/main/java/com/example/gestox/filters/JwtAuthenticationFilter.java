package com.example.gestox.filters;

import com.example.gestox.utility.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/user") || request.getRequestURI().startsWith("/api/users/register") ||
                request.getRequestURI().startsWith("/api/users/confirm") ||
                request.getRequestURI().startsWith("/v3/api-docs.yaml") ||
                request.getRequestURI().startsWith("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestHeader = request.getHeader("Authorization");
        logger.info(" Header :  {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal Argument while fetching the username !!");
            } catch (ExpiredJwtException e) {
                logger.error("Given jwt token is expired !!");
            } catch (MalformedJwtException e) {
                logger.error("Some changes has been done on token !! Invalid Token");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.info("Invalid Header Value !! ");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication()!= null && !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                || username != null && SecurityContextHolder.getContext().getAuthentication()== null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Token Validation failed !!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
