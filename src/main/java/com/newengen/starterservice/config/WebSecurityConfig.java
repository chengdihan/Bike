package com.newengen.starterservice.config;

import com.newengen.starterservice.config.properties.ApplicationProperties;
import com.newengen.vordt.web.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Order(PriorityOrdered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter           tokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ApplicationProperties    applicationProperties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement() // Make sure we use stateless session.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // Unauthenticated requests return 401 status code.
            .and()
            .addFilterBefore(tokenFilter, BasicAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                // Anonymous endpoints
                .antMatchers(applicationProperties.getAnonEndpoints().stream().map(String::trim).toArray(String[]::new))
                    .permitAll()
                .anyRequest()
                .fullyAuthenticated();
    }
}