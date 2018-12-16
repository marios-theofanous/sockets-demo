package com.theofanous.socketsdemo.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.theofanous.socketsdemo.models.AppProperties
import com.theofanous.socketsdemo.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter(authManager: AuthenticationManager, private val appProperties: AppProperties) : BasicAuthenticationFilter(authManager) {

    @Value("\${app.secret}")
    lateinit var secret : String

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader("Authorization") ?: req.getParameter("access_token")

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(req)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {

        val tokenString = request.getHeader("Authorization") ?: request.getParameter("access_token")
        if (tokenString != null) {
            // parse the token.
            val tokenObj = JWT.require(Algorithm.HMAC512(appProperties.secret))
                    .build()
                    .verify(tokenString.replace("Bearer ", ""))


            val user = User()
            user.firstName = tokenObj.getClaim("firstName").asString()
            user.lastName = tokenObj.getClaim("lastName").asString()
            user.username = tokenObj.subject

            return if (user.username != null) {
                UsernamePasswordAuthenticationToken(user, null, ArrayList<GrantedAuthority>())
            } else null
        }
        return null
    }
}