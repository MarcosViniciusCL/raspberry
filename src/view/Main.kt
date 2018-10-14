package view

import controller.Controller
import model.Laboratorio
import util.Sintetizador
import java.awt.Component
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.*
import java.lang.reflect.Array.getLength
import javax.swing.text.StyleConstants
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyledDocument




fun main(args: Array<String>) {
    var tela = TelaInicial("Principal")
    tela.run()
}

class TelaInicial(title: String): JFrame(title), Observer {
    var painel = JPanel()
    var textFieldNome =  JTextField(30)
    var textPaneTexto =  JTextPane()
    var label         =  JLabel()

    var controller = Controller(this)
    var sintetizador = Sintetizador()

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            var l = arg as Laboratorio
            textPaneTexto.text = l.textoFala
            textFieldNome.text = l.numeroSala
            var f: (i:Int) -> Unit = {
                println(it)
            }
            sintetizador.ditarTexto(l.textoFala, f)
        } else {
            textPaneTexto.text = "SISTEMA DE APRESENTAÇÃO"
            textFieldNome.text = "IEEE/UEFS"
        }



    }

    fun run (){
        var actionFechar = BotaoFecharAction()
        painel.layout = BoxLayout(painel, BoxLayout.PAGE_AXIS)
        this.add(painel)
        textPaneTexto.isEditable = false
        textFieldNome.isEditable = false
        
        var actMap = painel.actionMap
        actMap.put("fechar", actionFechar)
        painel.actionMap = actMap

        var imap = painel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK), "fechar")

        textFieldNome.text = "MV"
        textFieldNome.horizontalAlignment = SwingConstants.CENTER
        textPaneTexto.text = "TEXTOZAO"

        val doc = textPaneTexto.getStyledDocument()
        val center = SimpleAttributeSet()
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
        doc.setParagraphAttributes(0, doc.getLength(), center, false)

        label.size = Dimension(300, 300)
        painel.add(label)
        painel.add(textFieldNome)
        painel.add(textPaneTexto)


        this.setSize(800,600)
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.extendedState = Frame.MAXIMIZED_BOTH
        //this.isUndecorated = true
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}


private class BotaoFecharAction() : AbstractAction() {
    override fun actionPerformed(e: ActionEvent) {
        if(JOptionPane.showConfirmDialog(null, "Você gostaria de fechar a aplicação?") == 0) {
            System.exit(0)
        }
    }
}
