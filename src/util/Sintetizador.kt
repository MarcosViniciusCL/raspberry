package util

import java.util.*

class Sintetizador {

    private var prog = "espeak" // -vpt-br -s 80"

    private fun executar(ler: String){
        var p = ProcessBuilder(prog, "-vpt-br", "-s60", ler)
        p.environment().put("espeak", "/usr/bin/espeak")
        var pro = p.start()
        var s = Scanner(pro.inputStream)
        while (s.hasNext()){
            print(s.next())
        }
//        Runtime.getRuntime().exec(prog)
//        var lere = "Sala utilizada para criação de novos projetos em auxilio do IEEE onde nele podesse ingressar e realizar"
//        var cmd = "${prog}"
//        var proc = Runtime.getRuntime().exec(prog)
    }

    fun ditarTexto(texto: String, listener: (indexLinha: Int) -> Any){
        var list = texto.split('.')
        list.forEachIndexed({i, s ->
            executar(s)
            listener(i+1)
        })
        listener(-1) // <-- Informa que a mensagem acabou
    }
}