package lab.ujumeonji.literaturebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LiteratureGraphQLApplication

fun main(args: Array<String>) {
    runApplication<LiteratureGraphQLApplication>(*args)
}
