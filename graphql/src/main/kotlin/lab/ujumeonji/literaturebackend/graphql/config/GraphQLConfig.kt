package lab.ujumeonji.literaturebackend.graphql.config

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsRuntimeWiring
import graphql.scalars.ExtendedScalars
import graphql.schema.idl.RuntimeWiring

@DgsComponent
class GraphQLConfig {

    @DgsRuntimeWiring
    fun addScalars(builder: RuntimeWiring.Builder): RuntimeWiring.Builder {
        return builder
            .scalar(ExtendedScalars.DateTime)
            .scalar(ExtendedScalars.Url)
            .scalar(ExtendedScalars.UUID)
    }
}
