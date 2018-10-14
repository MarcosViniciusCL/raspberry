package util

import model.Laboratorio
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.FileReader
import java.io.BufferedReader



object ArqDAO{
    private var labs = ArrayList<Laboratorio>()

    fun lerArq(): String{
        val br = BufferedReader(FileReader("texto/labs.txt"))
        var linha = StringBuffer()
        while (br.ready()) {
            linha.append(br.readLine())
        }
        br.close()
        return linha.toString()
    }

    fun carregarTexto(): ArrayList<Laboratorio> {
        var text = lerArq()
        var buffer = StringBuffer(text)
        var textoSala = StringBuffer()
        var numeroSala = StringBuffer()

        var state = 0;

        for (i in buffer.indices){
            if (buffer[i].equals('\\') && state == 2){
                labs.add(Laboratorio(numeroSala.toString().replace("\\", "", false).trim(), textoSala.toString().replace(":", "", false).trim()))
                textoSala = StringBuffer()
                numeroSala = StringBuffer()
                state = 1
            }
            if(buffer[i].equals('\\')){
                state = 1
            }
            if(buffer[i].equals(':') && state == 1){
                state = 2
            }

            when (state){
                2 -> {
                    textoSala.append(buffer[i])
                }

                1 -> {
                    numeroSala.append(buffer[i])
                }
            }
            if(i == buffer.length-1){
                labs.add(Laboratorio(numeroSala.toString().replace("\\", "", false).trim(), textoSala.toString().replace(":", "", false).trim()))
            }

        }
        return labs
    }
}