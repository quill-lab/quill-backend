package lab.ujumeonji.literaturebackend.support.encrypt

interface PasswordEncoder {

    fun encode(password: String): String

    fun matches(password: String, encodedPassword: String): Boolean
}
