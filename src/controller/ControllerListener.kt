package controller

import java.io.*
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.collections.ArrayList

class ControllerListener(): Observable() {
    private lateinit var server: Server

    fun novaMensagem(msg: String){
        if(!msg.equals("NADA")){

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
}

private class Cliente(var s: Socket, var controllerListener: ControllerListener) {
    var entrada = Scanner(InputStreamReader(s.getInputStream()))
    var saida   = ObjectOutputStream(s.getOutputStream())
    var work = true

    init {
        atenderCliente()
    }

    fun atenderCliente(){
        Thread(Runnable {
            while (work) {
                Thread.sleep(500)
                controllerListener.novaMensagem(lerMensagem())
            }
        }).start()
    }

    fun lerMensagem(): String{
        try {
            var msg:String? = ""
            var inp = s.keepAlive
            //msg = entrada.readLine()
            while (entrada.hasNext() && !s.isClosed){
                msg += "${entrada.next()} "
            }
            if(!msg.isNullOrEmpty())
                println("${s.remoteSocketAddress} -> $msg")
            return msg ?: "NADA"
        } catch (e: EOFException){
            close()
            return "NADA"
        } catch (e: SocketException){
            close()
            return "NADA"
        }
    }

    fun enviarMensagem(msg: String){
        saida.flush()
        saida.write(msg.toByteArray())
    }

    fun close(){
        entrada.close()
        saida.close()
        s.close()
        work = false
        println("Cliente ${s.remoteSocketAddress} desconectado.")
    }
}