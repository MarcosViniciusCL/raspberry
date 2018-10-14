package view

import java.io.DataOutputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*

fun main (args: Array<String>){
    var s = Socket("127.0.0.1", 3333)
    DataOutputStream(s.getOutputStream()).writeUTF("Oi Rasp, tudo bem?")
    var r: String
    while(true){
        r = Scanner(System.`in`).nextLine()
        if(r.equals('q')) break
        DataOutputStream(s.getOutputStream()).writeUTF(r+"\n")
    }
    s.close()
}