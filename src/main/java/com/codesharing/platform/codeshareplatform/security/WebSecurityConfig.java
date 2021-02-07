package com.codesharing.platform.codeshareplatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationService authenticationService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication();
//        auth.inMemoryAuthentication();
        auth.authenticationProvider(authenticationService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                    .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/*", "/js/*", "/signin", "/signup", "/v2/api-docs",
                        "/swagger-ui", "/api/code/latest", "/api/user/new", "/h2/**", "/h2/*", "/h2-console/**", "/h2-console/*")
                .permitAll()
                .anyRequest()
                .authenticated();

        httpSecurity.formLogin()
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error=true");

        httpSecurity.formLogin()
                .defaultSuccessUrl("/", true);

        httpSecurity.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login");

        //For h2 console
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }


}
