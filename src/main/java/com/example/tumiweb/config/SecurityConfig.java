package com.example.tumiweb.config;

import com.example.tumiweb.application.filters.JwtRequestFilter;
import com.example.tumiweb.application.services.MyUserDetailsService;
import com.example.tumiweb.config.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final MyUserDetailsService myUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;
  private final CustomOAuth2UserService userService;

  public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter,
                        CustomOAuth2UserService userService) {
    this.myUserDetailsService = myUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.userService = userService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().configurationSource(request -> corsConfiguration())
        .and().csrf().disable()
        .authorizeRequests()
//                .antMatchers("/api/v1/auth/**").permitAll()
//                .antMatchers("/api/v1/categories/**").permitAll()
//                .antMatchers("/api/v1/chapters/**").authenticated()
//                .antMatchers("/api/v1/courses/**").permitAll()
//                .antMatchers("/api/v1/gifts/**").authenticated()
//                .antMatchers("/api/v1/helps/**").authenticated()
//                .antMatchers("/api/v1/notifications/**").permitAll()
//                .antMatchers("/api/v1/questions/**").permitAll()
//                .antMatchers("/api/v1/diaries/**").permitAll()
//                .antMatchers("/api/v1/users/**").authenticated()
////                .antMatchers("/api/v1/users/**").authenticated()
//                .antMatchers("/api/v1/user-course/**").authenticated()
        .antMatchers("/api").permitAll();
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.oauth2Login()
        .userInfoEndpoint()
        .userService(userService);

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  CorsConfiguration corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.applyPermitDefaultValues();
    corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
    corsConfiguration.addAllowedMethod(HttpMethod.PUT);
    corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
    return corsConfiguration;
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

}
