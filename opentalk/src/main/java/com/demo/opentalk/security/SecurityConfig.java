package com.demo.opentalk.security;

import com.demo.opentalk.filter.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    UserService userService;

    @Bean
    public AuthTokenFilter jwtAuthenticationFilter() {
        return new AuthTokenFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // Được quyền truy cập khi chưa login
        http.authorizeRequests().antMatchers("/signup", "/signin").permitAll();

        // Có những quyền Admin sẽ được truy cập
        http.authorizeRequests().antMatchers("/employee/*", "/admin/*", "/company/*","/role/*" )
                .hasAnyAuthority("ROLE_ADMIN");

        // Có những quyền Admin, User sẽ được truy cập
        http.authorizeRequests().antMatchers("/open-talk/")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");

        // Khi không đủ quyền truy cập sẽ bị chuyển hướng
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
