package controller

import model.SalaEnum
import java.io.*
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.collections.ArrayList

class ControllerListener(val controller: Controller): Observable() {
    private lateinit var server: Server

    init {
        addObserver(controller)
    }

    fun novaComando(msg: String){
        if(!msg.equals("NADA")){
            setChanged()
            notifyObservers(msg.trim())
        }
    }

    fun iniciarServidor(port: Int){
        println("Criando servidor")
        server = Server(port, this)
        server.iniciarServer()
    }

    fun pararServidor(){
        server.pararServer()
    }

    fun enviar(msg:String){
        server.enviarBrodcast(msg)
    }

    fun removeCliente(soc:Socket){

        server.remover(soc)
    }
}

private class Server(var port: Int, var cont: ControllerListener){
    var client: ArrayList<Cliente> = ArrayList()
    lateinit var socket: ServerSocket
    var work: Boolean = false

    fun iniciarServer(){
        try {
            println("Iniciando servico na porta: $port")
            socket = ServerSocket(port)
            work = true
            println("Servico iniciado. Aguardando cliente")
            Thread(Runnable {
                while (work) {
                    var s = socket.accept()
                    println("Cliente ${s.remoteSocketAddress} conectado.")
                    novoCliente(s)
                }
            }).start()
        } catch (e: BindException){
            println("$port já está sendo usada.")
            socket.close()
        } catch (e: Exception){
            print(e)
        }
    }

    fun pararServer(){
        work = false
        socket.close()
        client.forEach { it.s.close() }
    }

    fun novoCliente(s: Socket){
        client.add(Cliente(s, cont))
    }

    fun remover(obj: Socl){
        client.forEachIndexed { index, cliente ->
            if (cliente.s == )
        }
        client.remove(obj)
    }

    fun enviarBrodcast(msg: String){
        client.forEach {
            it.enviarMensagem(msg)
        }
    }
}

class Cliente(var s: Socket, var controllerListener: ControllerListener) {
    var entrada = DataInputStream(s.getInputStream())
    var saida   = DataOutputStream(s.getOutputStream())
    var work = true

    init {
        atenderCliente()
    }

    fun atenderCliente(){
        Thread(Runnable {
            while (work) {
                Thread.sleep(500)
                controllerListener.novaComando(lerMensagem())
            }
        }).start()
    }

    fun lerMensagem(): String{
        try {
            var msg:String? = ""
            var b =  0
            while (b != '\n'.toInt()){
                b = entrada.read()
                msg += b.toChar()
            }
//            while (entrada.hasNext() && !s.isClosed){
//                msg += "${entrada.next()} "
//            }
            if(!msg.isNullOrEmpty())
                println("${s.remoteSocketAddress} -> ${msg?.replace("\n", "")}")
            return msg?.replace("\n", "")?.trim() ?: "NADA"
        } catch (e: EOFException){
            close()
            return "NADA"
        } catch (e: SocketException){
            close()
            return "NADA"
        }
    }

    fun enviarMensagem(msg: String){
        try {
            saida.write(msg.toByteArray())
            close()
        } catch (e: SocketException){
            close()
        }

    }

    fun close(){
        entrada.close()
        saida.close()
        s.close()
        work = false
        println("Cliente ${s.remoteSocketAddress} desconectado.")
        controllerListener.removeCliente(s)
        //controllerListener.novaComando("ZERA")
    }
}