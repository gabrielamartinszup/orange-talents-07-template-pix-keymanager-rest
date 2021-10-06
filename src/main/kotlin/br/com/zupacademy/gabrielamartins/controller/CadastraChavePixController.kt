package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.CarregarChavePixRequest
import br.com.zupacademy.gabrielamartins.KeyManagerCadastraServiceGrpc
import br.com.zupacademy.gabrielamartins.model.ChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.net.URI
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class CadastraChavePixController(private val cadastraChavePixClient: KeyManagerCadastraServiceGrpc.KeyManagerCadastraServiceBlockingStub) {


    @Post("/pix")
    fun cadastrar(clienteId: UUID, @Valid @Body request: ChavePixRequest): HttpResponse<Any> {

        val grpcResponse = cadastraChavePixClient.cadastrarChavePix(request.converteParaRequestGrpc(clienteId))

        val response = URI.create("api/v1/clientes/${grpcResponse.clienteId}/pix/${grpcResponse.pixId}")

        return HttpResponse.created(response)


    }

}