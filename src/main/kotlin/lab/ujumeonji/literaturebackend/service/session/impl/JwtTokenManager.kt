package lab.ujumeonji.literaturebackend.service.session.impl

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import lab.ujumeonji.literaturebackend.service.session.TokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class JwtTokenManager(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-validity}") private val accessTokenValidity: Long,
    @Value("\${jwt.refresh-token-validity}") private val refreshTokenValidity: Long
) : TokenManager {

    override fun createToken(payload: Map<String, *>, issuedAt: LocalDateTime): String =
        with(issuedAt.toInstant(ZoneOffset.UTC)) {
            val now = Date.from(this)
            val expirationDate = Date(now.time + accessTokenValidity)

            Jwts.builder()
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .claims(payload)
                .compact()
        }
}
