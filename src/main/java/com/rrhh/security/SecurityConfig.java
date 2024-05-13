package com.rrhh.security;

import com.rrhh.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {		
		http
			.authorizeHttpRequests(authConfig -> {
				authConfig.requestMatchers("/", "/login", "/save", "/assets/**", "/templates/**", "/api/**").permitAll();
				authConfig.requestMatchers("/dashboard/**").hasAuthority("Administrador");
				authConfig.anyRequest().authenticated();
			})
			.formLogin(login -> {
				login.loginPage("/login");
				login.defaultSuccessUrl("/dashboard");
				login.failureUrl("/login?error=true");
				}
			)
			.logout(logout -> {
				logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
				logout.logoutSuccessUrl("/login?logout");
				logout.deleteCookies("JSESSIONID");
				logout.invalidateHttpSession(true);
			});
		return http.build();
	}
}