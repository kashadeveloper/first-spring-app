package com.studying.first_spring_app.filter;

import com.studying.first_spring_app.dto.UserCacheDto;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Duration;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver exceptionResolver;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver, RedisTemplate<String, Object> redisTemplate) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var token = header.substring(7);
            var username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String cacheKey = "security:user:" + username;
                UserDetails userDetails = null;

                try {
                    userDetails = (UserDetails) redisTemplate.opsForValue().get(cacheKey);
                    if (userDetails != null) {
                        logger.debug("User found in Redis: " + username);
                    }
                } catch (Exception e) {
                    logger.error("Redis reading failed:", e);
                }

                if (userDetails == null) {
                    logger.debug("User not in Redis, loading from DB: " + username);
                    userDetails = this.userDetailsService.loadUserByUsername(username);

                    try {
                        redisTemplate.opsForValue().set(cacheKey, UserCacheDto.from((User) userDetails), Duration.ofMinutes(5));
                    } catch (Exception e) {
                        logger.error("Redis writing failed (Check @JsonIgnore): ", e);
                    }
                }

                if (jwtService.isTokenValid(token, userDetails)) {
                    if (userDetails instanceof UserCacheDto cacheDto) {
                        userDetails = cacheDto.toUser();
                    }
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Authentication error: " + e.getMessage());
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
