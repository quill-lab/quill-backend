package lab.ujumeonji.literaturebackend.graphql.auth

import lab.ujumeonji.literaturebackend.support.session.TokenManager
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GraphQLAuthInterceptor(
    private val tokenManager: TokenManager,
    private val authContext: AuthContext,
) : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain,
    ): Mono<WebGraphQlResponse> =
        request.headers
            .getFirst("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.let { authHeader ->
                runCatching {
                    authHeader.removePrefix("Bearer ")
                        .let { token -> tokenManager.verifyToken(token) }
                        .let { claims -> claims["id"]?.toString() }
                        ?.also { userId -> authContext.accountId = userId }

                    chain.next(request)
                }.getOrElse { chain.next(request) }
            } ?: chain.next(request)
}
