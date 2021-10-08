package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.KeyManagerListaServiceGrpc
import br.com.zupacademy.gabrielamartins.ListarChavesPixRequest
import br.com.zupacademy.gabrielamartins.model.ChavePixResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class ListaChavesPixController(val listaChavesPixClient: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub) {


    @Get("/pix/")
    fun lista(clienteId: UUID): HttpResponse<Any> {


        val pix = listaChavesPixClient.listarChavesPix(
            ListarChavesPixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .build()
        )

        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }
}