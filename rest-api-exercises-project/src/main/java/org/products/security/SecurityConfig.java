package org.products.security;

import io.netty.handler.codec.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@EnableMethodSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/h2-console/**", "/swagger-ui/index.html").permitAll()
                .antMatchers(String.valueOf(HttpMethod.GET), "/api/product/**", "/api/product-type/**").authenticated()
                .antMatchers(String.valueOf(HttpMethod.POST), "/api/product/", "/api/product-type/").hasRole("ADMIN")
                .antMatchers(String.valueOf(HttpMethod.PUT),  "/api/product/", "/api/product-type/").hasRole("ADMIN")
                .antMatchers(String.valueOf(HttpMethod.DELETE), "/api/product/", "/api/product-type/").hasRole("ADMIN")
                .antMatchers(String.valueOf(HttpMethod.PUT),"/stock/decrease/**").authenticated()
                .antMatchers(String.valueOf(HttpMethod.PATCH), "/api/product/stock/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
