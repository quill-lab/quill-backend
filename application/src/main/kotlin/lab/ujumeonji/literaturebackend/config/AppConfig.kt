package lab.ujumeonji.literaturebackend.config

import lab.ujumeonji.literaturebackend.support.encrypt.PasswordEncoder
import lab.ujumeonji.literaturebackend.support.encrypt.impl.BCryptPasswordEncoder
import lab.ujumeonji.literaturebackend.support.mail.MailPort
import lab.ujumeonji.literaturebackend.support.mail.impl.SmtpMailAdapter
import lab.ujumeonji.literaturebackend.support.session.TokenManager
import lab.ujumeonji.literaturebackend.support.session.impl.JwtTokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class AppConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun tokenManager(
        @Value("\${jwt.secret.access}") secret: String,
        @Value("\${jwt.access-token-validity}") accessTokenValidity: Long,
    ): TokenManager = JwtTokenManager(secret, accessTokenValidity)

    @Bean
    fun mailClient(mailSender: JavaMailSender): MailPort = SmtpMailAdapter(mailSender)
}
