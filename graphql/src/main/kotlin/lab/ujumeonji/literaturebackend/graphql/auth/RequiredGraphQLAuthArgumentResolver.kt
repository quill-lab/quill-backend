package lab.ujumeonji.literaturebackend.support.auth

import com.netflix.graphql.dgs.internal.method.ArgumentResolver
import graphql.schema.DataFetchingEnvironment
import lab.ujumeonji.literaturebackend.graphql.auth.AuthContext
import lab.ujumeonji.literaturebackend.graphql.auth.RequiredGraphQLAuth
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component

@Component
class RequiredGraphQLAuthArgumentResolver(
    private val authContext: AuthContext,
) : ArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(RequiredGraphQLAuth::class.java) && parameter.parameterType == String::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        dfe: DataFetchingEnvironment,
    ): String? =
        when {
            parameter.hasParameterAnnotation(RequiredGraphQLAuth::class.java) &&
                parameter.parameterType == String::class.java ->
                authContext.accountId ?: throw BusinessException(ErrorCode.UNAUTHORIZED)

            else -> authContext.accountId
        }
}
