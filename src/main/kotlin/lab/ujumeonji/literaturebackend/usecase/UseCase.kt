package lab.ujumeonji.literaturebackend.usecase

interface UseCase<T, R> {

    fun execute(request: T): R
}
