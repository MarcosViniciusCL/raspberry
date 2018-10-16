package controller

import model.Laboratorio
import model.SalaEnum
import util.ArqDAO
import view.TelaInicial
import java.util.*
import javax.swing.JFrame
import kotlin.collections.ArrayList

class Controller(var frame: TelaInicial): Observer, Observable(){
    var labAtual: Laboratorio? = null
    var labs: ArrayList<Laboratorio>
    var controllerListener = ControllerListener(this)

    init {
        addObserver(frame)
        labs = ArqDAO.carregarTexto()
        controllerListener.iniciarServidor(3333)
    }

    fun salaApresentada(){
        controllerListener.enviar("END")
    }

    override fun update(o: Observable?, arg: Any?) {
        var cmd = arg as String
        selecionarSala(cmd)
        setChanged()
        notifyObservers(labAtual)
    }

    fun selecionarSala(nome: String){
        labs.forEach {
            if (it.numeroSala.equals(nome)){
                labAtual = it
                return
            }
        }
        labAtual = null
    }

}