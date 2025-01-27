package lab.ujumeonji.literaturebackend.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDSLConfig @Autowired constructor(
    @PersistenceContext private val entityManager: EntityManager
) {

    @Bean
    fun queryFactory(): JPAQueryFactory = JPAQueryFactory(entityManager)
}
