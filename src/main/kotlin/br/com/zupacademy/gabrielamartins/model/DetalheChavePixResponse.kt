package br.com.zupacademy.gabrielamartins.model

import br.com.zupacademy.gabrielamartins.CarregarChavePixResponse
import br.com.zupacademy.gabrielamartins.TipoConta
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DetalheChavePixResponse(chaveResponse: CarregarChavePixResponse) {

    val pixId = chaveResponse.pixId
    val tipo = chaveResponse.chave.tipoChave
    val chave = chaveResponse.chave.chave

    val criadaEm = chaveResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (chaveResponse.chave.conta.tipoConta) {
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(
        Pair("tipo", tipoConta),
        Pair("instituicao", chaveResponse.chave.conta.instituicao),
        Pair("nomeDoTitular", chaveResponse.chave.conta.nomeTitular),
        Pair("cpfDoTitular", chaveResponse.chave.conta.cpfTitular),
        Pair("agencia", chaveResponse.chave.conta.agencia),
        Pair("numero", chaveResponse.chave.conta.numeroConta)
    )

}
