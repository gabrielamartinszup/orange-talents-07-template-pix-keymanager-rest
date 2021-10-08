package br.com.zupacademy.gabrielamartins.controller

import br.com.zupacademy.gabrielamartins.CarregarChavePixResponse
import br.com.zupacademy.gabrielamartins.KeyManagerCarregaServiceGrpc
import br.com.zupacademy.gabrielamartins.TipoChave
import br.com.zupacademy.gabrielamartins.TipoConta
import br.com.zupacademy.gabrielamartins.utils.KeyManagerFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@MicronautTest(transactional = false)
internal class CarregaChavePixControllerTest {

    @field:Inject
    lateinit var carregaChaveStub: KeyManagerCarregaServiceGrpc.KeyManagerCarregaServiceBlockingStub


    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CHAVE_EMAIL = "teste@teste.com.br"
    val CHAVE_CELULAR = "+5511912345678"
    val CONTA_CORRENTE = TipoConta.CONTA_CORRENTE
    val TIPO_DE_CHAVE_EMAIL = TipoChave.EMAIL
    val TIPO_DE_CHAVE_CELULAR = TipoChave.TELEFONE
    val INSTITUICAO = "Itau"
    val TITULAR = "Woody"
    val DOCUMENTO_DO_TITULAR = "34597563067"
    val AGENCIA = "0001"
    val NUMERO_DA_CONTA = "1010-1"
    val CHAVE_CRIADA_EM = LocalDateTime.now()

    @Test
    internal fun `deve carregar uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(carregaChaveStub.carregarChavePix(Mockito.any())).willReturn(carregaChavePixResponse(clienteId, pixId))


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
    }


    private fun carregaChavePixResponse(clienteId: String, pixId: String) =
        CarregarChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChave(CarregarChavePixResponse.ChavePix
                .newBuilder()
                .setTipoChave(TIPO_DE_CHAVE_EMAIL)
                .setChave(CHAVE_EMAIL)
                .setConta(
                    CarregarChavePixResponse.ChavePix.ContaInfo.newBuilder()
                        .setTipoConta(CONTA_CORRENTE)
                        .setInstituicao(INSTITUICAO)
                        .setNomeTitular(TITULAR)
                        .setCpfTitular(DOCUMENTO_DO_TITULAR)
                        .setAgencia(AGENCIA)
                        .setNumeroConta(NUMERO_DA_CONTA)
                        .build()
                )
                .setCriadaEm(CHAVE_CRIADA_EM.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
            ).build()

    @Factory
    @Replaces(factory = KeyManagerFactory::class)
    internal class MockitoStubFactory {


        @Singleton
        fun stubDetalhesMock() =
            Mockito.mock(KeyManagerCarregaServiceGrpc.KeyManagerCarregaServiceBlockingStub::class.java)
    }
}