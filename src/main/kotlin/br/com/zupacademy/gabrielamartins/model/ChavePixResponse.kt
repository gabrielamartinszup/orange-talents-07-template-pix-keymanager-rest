package br.com.zupacademy.gabrielamartins.model

import br.com.zupacademy.gabrielamartins.ListarChavesPixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chavePix: ListarChavesPixResponse.ChavePix) {

    val id = chavePix.pixId
    val chave = chavePix.chave
    val tipo = chavePix.tipoChave
    val tipoDeConta = chavePix.tipoConta
    val criadaEm = chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}