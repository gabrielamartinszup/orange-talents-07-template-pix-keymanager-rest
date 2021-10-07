package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.KeyManagerRemoveServiceGrpc
import br.com.zupacademy.gabrielamartins.RemoverChavePixResponse
import br.com.zupacademy.gabrielamartins.utils.KeyManagerFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.util.*

@MicronautTest(transactional = false)
internal class RemoveChavePixControllerTest {


    @field:Inject
    lateinit var removeStub: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve remover uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoverChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()
        given(removeStub.removerChavePix(any())).willReturn(respostaGrpc)


        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerFactory::class)
    internal class RemoveStubFactory {

        @Singleton
        fun deletaChave() = mock(KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub::class.java)
    }

}