package br.com.zupacademy.gabrielamartins.exception


import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<JsonError>> {


    override fun handle(request: HttpRequest<*>, e: StatusRuntimeException): HttpResponse<JsonError> {

        val code: Status.Code = e.status.code
        val description: String = e.status.description ?: ""



        val (httpStatus: HttpStatus, message: String) = when (code) {
            Status.Code.INVALID_ARGUMENT -> Pair(HttpStatus.BAD_REQUEST, description)
            Status.Code.FAILED_PRECONDITION -> Pair(HttpStatus.BAD_REQUEST, description)
            Status.Code.PERMISSION_DENIED -> Pair(HttpStatus.FORBIDDEN, description)
            Status.Code.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, description)
            Status.Code.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            Status.Code.UNAVAILABLE -> Pair(HttpStatus.SERVICE_UNAVAILABLE, description)
            else -> {

                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado")
            }
        }

        return HttpResponse.status<JsonError>(httpStatus).body(JsonError(message))
    }

}