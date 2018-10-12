package view

import controller.ControllerListener
import java.awt.Frame
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.*


fun main(args: Array<String>) {
//    var controllerListener = ControllerListener()
//    controllerListener.iniciarServidor(3333)
    var actionFechar = BotaoFecharAction()

    var painel = JPanel()
    var actMap = painel.actionMap
    actMap.put("fechar", actionFechar)
    painel.actionMap = actMap

    var imap = painel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK), "fechar")

    var janela = javax.swing.JFrame("Primeira tela")
    janela.add(painel)
    janela.setSize(800,600)
    janela.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    janela.extendedState = Frame.MAXIMIZED_BOTH
    janela.isUndecorated = true
    janela.setLocationRelativeTo(null)
    janela.isVisible = true
}

private class BotaoFecharAction() : AbstractAction() {


    override fun actionPerformed(e: ActionEvent) {
        if(JOptionPane.showConfirmDialog(null, "Você gostaria de fechar a aplicação?") == 0) {
            System.exit(0)
        }
    }
}
