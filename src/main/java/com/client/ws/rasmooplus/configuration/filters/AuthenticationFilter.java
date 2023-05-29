package com.client.ws.rasmooplus.configuration.filters;

import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AuthenticationFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UserDetailsRepository userDetailsRepository;

    public AuthenticationFilter(TokenService tokenService, UserDetailsRepository userDetailsRepositor) {
        this.tokenService = tokenService;
        this.userDetailsRepository = userDetailsRepositor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerToken(request);

        if (tokenService.isValid(token)) {
            authByToken(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authByToken(String token) {
        //Recuperar id do usuario
        Long userId = tokenService.getUserId(token);
        var userOpt = userDetailsRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }
        UserCredentials userCredentials = userOpt.get();

        //Autenticar no Spring
        UsernamePasswordAuthenticationToken userAuth =
                new UsernamePasswordAuthenticationToken(userCredentials, null, userCredentials.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(userAuth);
    }

    private String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (Objects.isNull(token) || !token.startsWith("Bearer")) {
            return null;
        }
        return token.substring(7, token.length());
    }
}
