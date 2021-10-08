package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.CarregarChavePixRequest
import br.com.zupacademy.gabrielamartins.KeyManagerCarregaServiceGrpc
import br.com.zupacademy.gabrielamartins.KeyManagerListaServiceGrpc
import br.com.zupacademy.gabrielamartins.model.DetalheChavePixResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController(
    val carregaChavePixClient: KeyManagerCarregaServiceGrpc.KeyManagerCarregaServiceBlockingStub,

) {

    @Get("/pix/{pixId}")
    fun carregar(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        val chaveResponse = carregaChavePixClient.carregarChavePix(
            CarregarChavePixRequest.newBuilder()
                .setPixId(
                    CarregarChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClienteId(clienteId.toString())
                        .setPixId(pixId.toString())
                        .build()
                )
                .build()
        )

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))


    }
}