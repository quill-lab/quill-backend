package lab.ujumeonji.literaturebackend.support.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.*
import java.util.regex.Pattern

class UUIDValidator : ConstraintValidator<ValidUUID, String> {
    companion object {
        private val UUID_PATTERN =
            Pattern.compile(
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            )
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (value == null) {
            return false
        }

        if (!UUID_PATTERN.matcher(value).matches()) {
            return false
        }

        return try {
            UUID.fromString(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
