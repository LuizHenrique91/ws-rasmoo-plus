package com.client.ws.rasmooplus.configuration;

import com.client.ws.rasmooplus.configuration.filters.AuthenticationFilter;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Essa configuração é para versão 2.7.. para cima.
@EnableWebSecurity
@Configuration
public class WebSecurityConfigNovo {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    //Responsável pela configuração de autorização -> Acesso a URL´s
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers("/subscription-type/*").permitAll()
                .antMatchers(HttpMethod.POST, "/payment/process").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationFilter(tokenService, userDetailsRepository), UsernamePasswordAuthenticationFilter.class).build();
    }
}


