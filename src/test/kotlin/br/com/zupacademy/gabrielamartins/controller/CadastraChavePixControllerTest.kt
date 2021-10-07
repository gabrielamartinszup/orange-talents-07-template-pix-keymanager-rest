package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.CadastrarChavePixResponse
import br.com.zupacademy.gabrielamartins.KeyManagerCadastraServiceGrpc
import br.com.zupacademy.gabrielamartins.model.ChavePixRequest
import br.com.zupacademy.gabrielamartins.model.TipoChaveRequest
import br.com.zupacademy.gabrielamartins.model.TipoContaRequest
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class CadastraChavePixControllerTest {

    @field:Inject
    lateinit var registraStub: KeyManagerCadastraServiceGrpc.KeyManagerCadastraServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = CadastrarChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.cadastrarChavePix(Mockito.any())).willReturn(respostaGrpc)


        val novaChavePix = ChavePixRequest(tipoConta = TipoContaRequest.CONTA_CORRENTE,
            chave = "teste@teste.com.br",
            tipoChave = TipoChaveRequest.EMAIL
        )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, ChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }

    @Factory
    @Replaces(factory = KeyManagerFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerCadastraServiceGrpc.KeyManagerCadastraServiceBlockingStub::class.java)
    }
}