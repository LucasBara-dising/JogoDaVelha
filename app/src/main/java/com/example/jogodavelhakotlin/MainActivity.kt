package com.example.jogodavelhakotlin

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReinicia.setOnClickListener{
            reinicairJogo()
        }
    }

    fun btnClick(view: View){
        //val= valor fixo
        //var= valor vriavel

        val btnSelect= view as ImageView
        var boxId=""

        when(btnSelect.id){
            //atrelanod um valor a cada box
            R.id.btn_A1->boxId="A1"
            R.id.btn_A2->boxId="A2"
            R.id.btn_A3->boxId="A3"
            R.id.btn_B1->boxId="B1"
            R.id.btn_B2->boxId="B2"
            R.id.btn_B3->boxId="B3"
            R.id.btn_C1->boxId="C1"
            R.id.btn_C2->boxId="C2"
            R.id.btn_C3->boxId="C3"
        }

        playGame(boxId,btnSelect)
    }

    //arrays para guardar as jogadas
    var player1=ArrayList<String>()
    var player2=ArrayList<String>()

    //turnos
    var activePlayer=1

    fun playGame(boxId:String, btnSelect:ImageView){
        if (activePlayer==1){
            txtvez.text="Vez do Jogador 2"
            btnSelect.setImageResource(R.drawable.cross)
            player1.add(boxId)
            activePlayer=2
        }
        else{
            txtvez.text= "Vez do Jogador 1"
            btnSelect.setImageResource(R.drawable.letter_o)
            player2.add(boxId)
            activePlayer=1
        }

        btnSelect.isEnabled=false

        //toda vez que tiver uma jogada verifica o ganhador
        CheckWiner()
    }
    var contRodadas=0
    //checa quem ganhou
    fun CheckWiner(){
        var winer=-1
        contRodadas++

        //se jogador 1 ganhar
        if (//verica linhas
            (player1.contains("A1")&& player1.contains("A2") && player1.contains("A3")) ||
            (player1.contains("B1")&& player1.contains("B2") && player1.contains("B3")) ||
            (player1.contains("C1")&& player1.contains("C2") && player1.contains("C3")) ||
            //verica colunas
            (player1.contains("A1")&& player1.contains("B1") && player1.contains("C1")) ||
            (player1.contains("A2")&& player1.contains("B2") && player1.contains("C2")) ||
            (player1.contains("A3")&& player1.contains("B3") && player1.contains("C3")) ||
            //diagonal
            (player1.contains("A1")&& player1.contains("B2") && player1.contains("C3")) ||
            (player1.contains("C1")&& player1.contains("B2") && player1.contains("C3"))){
            winer=1
            vitoria("Jogador 1 Ganhou!!")
        }

        //se jogador 2 ganhar
        if (//verica linhas
            (player2.contains("A1")&& player2.contains("A2") && player2.contains("A3")) ||
            (player2.contains("B1")&& player2.contains("B2") && player2.contains("B3")) ||
            (player2.contains("C1")&& player2.contains("C2") && player2.contains("C3")) ||
            //verica colunas
            (player2.contains("A1")&& player2.contains("B1") && player2.contains("C1")) ||
            (player2.contains("A2")&& player2.contains("B2") && player2.contains("C2")) ||
            (player2.contains("A3")&& player2.contains("B3") && player2.contains("C3")) ||
            //diagonal
            (player2.contains("A1")&& player2.contains("B2") && player2.contains("C3")) ||
            (player2.contains("C1")&& player2.contains("B2") && player2.contains("C3"))){
            winer=2
            vitoria("Jogador 2 Ganhou!!")
        }

        if(winer==-1 && contRodadas==9){
            vitoria("Deu Velha")

        }

    }


    fun vitoria(QuemGanhou:String){
        player1.clear()
        player2.clear()

        val dialogBinding=layoutInflater.inflate(R.layout.style_dialog, null)
        val dialog=Dialog(this)

        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        dialog.show()

        val txtQuemGanhou =dialogBinding.findViewById<TextView>(R.id.textView)
        txtQuemGanhou.setText(QuemGanhou)

        val btnReinicia= dialogBinding.findViewById<Button>(R.id.btn_login)
        btnReinicia.setOnClickListener{
            reinicairJogo()
        }
    }

    fun reinicairJogo(){
        val dialog=Dialog(this)
        dialog.dismiss()
        finish()
        startActivity(getIntent())
    }
}