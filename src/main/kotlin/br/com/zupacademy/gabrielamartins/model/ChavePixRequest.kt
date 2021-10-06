package br.com.zupacademy.gabrielamartins.model

import br.com.zupacademy.gabrielamartins.CadastrarChavePixRequest
import br.com.zupacademy.gabrielamartins.TipoChave
import br.com.zupacademy.gabrielamartins.TipoConta
import br.com.zupacademy.gabrielamartins.validation.ChavePixValida
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator


import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ChavePixValida
data class ChavePixRequest(
    @field:NotNull val tipoConta: TipoContaRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoChave: TipoChaveRequest?
) {

    fun converteParaRequestGrpc(clienteId: UUID): CadastrarChavePixRequest {
        return CadastrarChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoConta(tipoConta?.tipoContaGrpc ?: TipoConta.DESCONHECIDO)
            .setTipoChave(tipoChave?.tipoChaveGrpc ?: TipoChave.CHAVE_DESCONHECIDA)
            .setChave(chave ?: "")
            .build()
    }

}

enum class TipoChaveRequest(val tipoChaveGrpc: TipoChave) {

    CPF(TipoChave.CPF) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            if (!chave.matches("[0-9]+".toRegex())) {
                return false
            }

            CPFValidator().run {
                initialize(null)
                return isValid(chave, null)
            }


        }
    },

    TELEFONE(TipoChave.TELEFONE) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoChave.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())

        }
    },

    ALEATORIA(TipoChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}


enum class TipoContaRequest(val tipoContaGrpc: TipoConta) {

    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}
