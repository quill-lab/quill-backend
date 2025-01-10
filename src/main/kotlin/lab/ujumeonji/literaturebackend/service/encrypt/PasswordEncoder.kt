package lab.ujumeonji.literaturebackend.service.encrypt

interface PasswordEncoder {

    fun encode(password: String): String

    fun matches(password: String, encodedPassword: String): Boolean
}
