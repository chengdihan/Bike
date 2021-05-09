package com.example.bike.config;

import java.util.Arrays;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${application.anonendpoints}")
  String anonendpoints;

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtRequestFilter jwtRequestFilter;

  private final BikeAuthenticationProvider appAuthenticationProvider;

  public WebSecurityConfig(
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter,
      BikeAuthenticationProvider appAuthenticationProvider) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtRequestFilter = jwtRequestFilter;
    this.appAuthenticationProvider = appAuthenticationProvider;
  }

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    // configure AuthenticationManager so that it knows from where to load
    // user for matching credentials
    // Use BCryptPasswordEncoder
    appAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    auth.authenticationProvider(appAuthenticationProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public StandardPBEStringEncryptor stringEncryption() {
    StandardPBEStringEncryptor strongEncryptor = new StandardPBEStringEncryptor();
    strongEncryptor.setPassword("bike-secret");

    return strongEncryptor;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // We don't need CSRF for this example
    httpSecurity.antMatcher("/**")
        .csrf().disable().cors().and()
        // dont authenticate this particular request
        .authorizeRequests()
        .antMatchers(
            Arrays.stream(anonendpoints.split(",")).map(String::trim).toArray(String[]::new))
        .permitAll()
        // all other requests need to be authenticated
        .anyRequest().authenticated()
        .and()
        // make sure we use stateless session; session won't be used to
        // store user's state.
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity.headers().frameOptions().disable();

    // Add a filter to validate the tokens with every request
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  // @Bean
  // CorsConfigurationSource corsConfigurationSource() {
  // 	CorsConfiguration configuration = new CorsConfiguration();
  // 	configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://jlin.homeip.net:8080"));
  // 	configuration.setAllowedMethods(Arrays.asList("GET","POST"));
  // 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  // 	source.registerCorsConfiguration("/**", configuration);
  // 	return source;
  // }
}