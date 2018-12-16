package com.theofanous.socketsdemo.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.theofanous.socketsdemo.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.ArrayList
import java.util.Date

class JWTAuthenticationFilter(private val authenticationManagers: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    @Value(value = "\${app.secret:secret}")
    internal var secret: String? = null

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse?): Authentication {
        try {
            val creds = ObjectMapper()
                    .readValue(req.inputStream, User::class.java)

            return authenticationManagers.authenticate(
                    UsernamePasswordAuthenticationToken(
                            creds.username,
                            creds.password,
                            ArrayList<GrantedAuthority>())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain?,
                                          auth: Authentication) {

        val token = JWT.create()
                .withSubject((auth.principal as User).username)
                .withClaim("firstName", (auth.principal as User).firstName)
                .withClaim("lastName", (auth.principal as User).lastName)
                .withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .sign(Algorithm.HMAC512("secret"))
        res.addHeader("Authorization", "Bearer $token")
    }
}