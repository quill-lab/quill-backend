package lab.ujumeonji.literaturebackend.domain.common.dto

data class PageResponse<T>(
    val items: List<T>,
    val totalCount: Int,
    val offset: Int,
    val limit: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
)
