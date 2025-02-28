package lab.ujumeonji.literaturebackend.support.encrypt.impl

import lab.ujumeonji.literaturebackend.support.encrypt.PasswordEncoder

class BCryptPasswordEncoder : PasswordEncoder {

    private val passwordEncoder = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()

    override fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun matches(password: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(password, encodedPassword)
    }
}
