package br.com.zupacademy.gabrielamartins.utils

import br.com.zupacademy.gabrielamartins.KeyManagerCadastraServiceGrpc
import br.com.zupacademy.gabrielamartins.KeyManagerCarregaServiceGrpc
import br.com.zupacademy.gabrielamartins.KeyManagerListaServiceGrpc
import br.com.zupacademy.gabrielamartins.KeyManagerRemoveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class KeyManagerFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun cadastraChave() = KeyManagerCadastraServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = KeyManagerRemoveServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaChaves() = KeyManagerCarregaServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeyManagerListaServiceGrpc.newBlockingStub(channel)
}