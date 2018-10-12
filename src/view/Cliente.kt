package view

import java.io.DataOutputStream
import java.io.ObjectOutputStream
import java.net.Socket

fun main (args: Array<String>){
    var s = Socket("127.0.0.1", 3333)
    DataOutputStream(s.getOutputStream()).writeUTF("Oi Rasp, tudo bem?")
    s.close()
}