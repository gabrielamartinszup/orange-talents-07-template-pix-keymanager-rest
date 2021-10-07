package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.KeyManagerRemoveServiceGrpc
import br.com.zupacademy.gabrielamartins.RemoverChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import java.util.*


@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub) {

    @Delete("/pix/{pixId}")
    fun deletar(clienteId: UUID, pixId: UUID): HttpResponse<Any>{

        removeChavePixClient.removerChavePix(RemoverChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setPixId(pixId.toString())
            .build())

        return HttpResponse.ok()
    }
}