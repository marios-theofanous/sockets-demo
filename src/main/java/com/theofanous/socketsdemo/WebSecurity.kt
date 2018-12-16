package com.theofanous.socketsdemo

import com.theofanous.socketsdemo.filters.JWTAuthenticationFilter
import com.theofanous.socketsdemo.filters.JWTAuthorizationFilter
import com.theofanous.socketsdemo.models.AppProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@EnableWebSecurity
@Order(1)
open class WebSecurity(@Qualifier("userDetailsServiceImpl") var userDetailsService: UserDetailsService, val appProperties: AppProperties) : WebSecurityConfigurerAdapter() {


    private var bCryptPasswordEncoder : BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().and().authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthorizationFilter(authenticationManager(), appProperties))
                .addFilter(JWTAuthenticationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.csrf().disable() // Not needed since we do not use cookies for authentication
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedOrigins = Arrays.asList("http://localhost:8081")
        configuration.allowedMethods = Arrays.asList("GET", "POST")
        configuration.allowedHeaders = Arrays.asList("content-type", "Authorization", "Access-Control-Allow-Credentials")
        configuration.exposedHeaders = Arrays.asList("content-type", "Authorization", "Access-Control-Allow-Credentials")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}