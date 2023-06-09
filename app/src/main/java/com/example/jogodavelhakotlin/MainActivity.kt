package com.example.jogodavelhakotlin

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReinicia.setOnClickListener {
            reinicairJogo()
        }

        //-----------------------dialog-----------------
        val dialogBinding=layoutInflater.inflate(R.layout.style_dialog_begin_game, null)
        val dialog=Dialog(this)

        dialog.setContentView(dialogBinding)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        dialog.show()

        val btn2Players= dialogBinding.findViewById<Button>(R.id.btn_gamePlayer)
        btn2Players.setOnClickListener{
            modoGame="2jogadores"
            dialog.dismiss()
        }

        val btnGameIA= dialogBinding.findViewById<Button>(R.id.btn_gameIa)
        btnGameIA.setOnClickListener{
            modoGame="maquina"
            dialog.dismiss()
        }

    }


    //arrays para guardar as jogadas
    var player1=ArrayList<String>()
    var player2=ArrayList<String>()

    //modo de jogo
    var modoGame=""

    //turnos
    var activePlayer=1

    fun btnClick(view: View){
        val btnSelect= view as ImageView
        var boxId=""

        when(btnSelect.id){
            //atrelando um valor a cada box
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

        //2 jogadores
        if(modoGame == "2jogadores"){
            playGame2Players(boxId,btnSelect)
        }
        //maquina
        else if(modoGame.equals("maquina")){
            //se for vez da maquina recebe tudo null
            if(activePlayer==2){
                playGameIA(null,null)
            } else{
                //se for player recebe valores
                playGameIA(boxId,btnSelect)
            }

        }
    }

    private fun playGame2Players(boxId:String, btnSelect:ImageView){
        if (activePlayer==1){
            txtvez.text= R.string.txtVezjogador2.toString()
            btnSelect.setImageResource(R.drawable.cross)
            player1.add(boxId)
            activePlayer=2
        }
        else{
            txtvez.text= R.string.txtVezjogador1.toString()
            btnSelect.setImageResource(R.drawable.letter_o)
            player2.add(boxId)
            activePlayer=1
        }

        //desabilita o btn ja clicado
        btnSelect.isEnabled=false

        //toda vez que tiver uma jogada verifica o ganhador
        checkWiner()
    }

    private fun playGameIA(boxId:String?, btnSelect: ImageView?){
        //jogador
        if (activePlayer==1 && boxId!=null && btnSelect!=null){
            txtvez.text="Vez da Máquina"
            btnSelect.setImageResource(R.drawable.letter_o)
            btnSelect.isEnabled=false
            player1.add(boxId)
            activePlayer=2
            checkWiner()
        }
        //máquina
        if (activePlayer==2){
            txtvez.setText(R.string.txtSuaVez)
            //retorna uma box pro jogo
            maquinaJoga()
            activePlayer=1
            checkWiner()
        }

    }

    //checka se a caixa ja foi usada
    private fun checkClick(boxId: String):Boolean{
        return player1.contains(boxId)==false && player2.contains(boxId)==false
    }

    fun selectBoxCross(boxId: String) {
        player2.add(boxId)

        val imgCross=when(boxId){
            "A1"->findViewById(R.id.btn_A1)
            "A2"->findViewById(R.id.btn_A2)
            "A3"->findViewById(R.id.btn_A3)

            "B1"->findViewById(R.id.btn_B1)
            "B2"->findViewById(R.id.btn_B2)
            "B3"->findViewById(R.id.btn_B3)

            "C1"->findViewById(R.id.btn_C1)
            "C2"->findViewById(R.id.btn_C2)
            "C3"->findViewById(R.id.btn_C3)

            else->findViewById<ImageView>(R.id.btn_A3)
        }

        imgCross.setImageResource(R.drawable.cross)
        imgCross.isEnabled=false
    }

    fun geraJogada():String{
        val MoviGame = when((1..8).random()) {
            1 -> "A1"
            2 -> "A2"
            3 -> "A3"
            4 -> "B1"
            5 -> "B2"
            6 -> "B3"
            7 -> "C1"
            8 -> "C2"
            else -> "C3"
        }
        return MoviGame
    }

    fun maquinaJoga(){
        //primneira jogada nos cantos
        Log.e("Arry", player2.size.toString())
        if(player2.isEmpty()){
            val FirstGame = when((1..3).random()) {
                1 -> "A1"
                2 -> "A3"
                3 -> "C1"
                else -> "C3"
            }
            //verifica se pode, se não poder tenta denovo
            if (checkClick(FirstGame)==true){
                selectBoxCross(FirstGame)
            }
            else{
                maquinaJoga()
            }
        }

        /*else  if(player2.size>1) {
            //gera uma caixa verifica se pode, se não poder tenta denovo
            val jogadaMqn=geraJogada()

            if (checkClick(jogadaMqn)==true){
                selectBoxCross(jogadaMqn)
            }
            else{
                maquinaJoga()
            }
        }*/
        //verifica se pode jogar no maio, se não poder tenta denovo
        else if(player2.size==1 && checkClick("B2")==true) {
            selectBoxCross("B2")
        }

        //define jogadas para ganhar
        else if (player2.size>1){
            //-----ganha se jogra nas pontas-----
            if((player2.contains("A2")&& player2.contains("A3")) ||
                (player2.contains("B1")&& player2.contains("C1") ||
                (player2.contains("B2")&& player2.contains("C3"))) && checkClick("A1")){
                    selectBoxCross("A1")
                }
            else if((player2.contains("A1")&& player2.contains("A2")) ||
                (player2.contains("B3")&& player2.contains("C3")) ||
                (player2.contains("B2")&& player2.contains("C1"))&& checkClick("A3")){
                selectBoxCross("A3")
            }

            else if((player2.contains("A1")&& player2.contains("B1")) ||
                (player2.contains("C2")&& player2.contains("C3")) ||
                (player2.contains("B2")&& player2.contains("A3")) && checkClick("C1")){
                selectBoxCross("C1")
            }

            else if((player2.contains("A3")&& player2.contains("B3")) ||
                (player2.contains("C1")&& player2.contains("C2"))  ||
                (player2.contains("B2")&& player2.contains("A1"))&& checkClick("C3")){
                selectBoxCross("C3")
            }

            //------------joga nos meios-----------
            else if((player2.contains("A1")&& player2.contains("A3")) ||
                (player2.contains("B2")&& player2.contains("C3")) && checkClick("A2")){
                selectBoxCross("A2")
            }
            else if((player2.contains("A3")&& player2.contains("C3")) ||
                (player2.contains("B1")&& player2.contains("B2"))&& checkClick("B3")){
                selectBoxCross("B3")
            }

            else if((player2.contains("A2")&& player2.contains("B2")) ||
                (player2.contains("C1")&& player2.contains("C3")) && checkClick("C2")){
                selectBoxCross("C2")
            }

            else if((player2.contains("A1")&& player2.contains("C1")) ||
                (player2.contains("B2")&& player2.contains("B3"))&& checkClick("B1")){
                selectBoxCross("B1")
            }

            else {
                Log.d("Jogada","Aleatoria")
                val jogadaMqn=geraJogada()
                if (checkClick(jogadaMqn)==true){
                    selectBoxCross(jogadaMqn)
                }
                else{
                    maquinaJoga()
                }

            }

        }

        else {
            Log.d("Jogada", "Aleatoria")
            val jogadaMqn = geraJogada()
            if (checkClick(jogadaMqn) == true) {
                selectBoxCross(jogadaMqn)
            } else {
                maquinaJoga()
            }
        }

    }

    var contRodadas=0
    //checa quem ganhou
    private fun checkWiner(){
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
            (player1.contains("C1")&& player1.contains("B2") && player1.contains("A3"))){
            winer=1
            if (modoGame.equals("maquina")){
                    vitoria("Você Ganhou!!")
                }
            else{
                vitoria("Jogador 1 Ganhou!!")
            }

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
            (player2.contains("C1")&& player2.contains("B2") && player2.contains("A3"))){
            winer=2
            vitoria("Jogador 2 Ganhou!!")
        }

        if(winer==-1 && contRodadas==9){
            vitoria("Deu Velha")
        }

    }


    private fun vitoria(QuemGanhou:String){
        player1.clear()
        player2.clear()

        val dialogBinding=layoutInflater.inflate(R.layout.style_dialog, null)
        val dialog=Dialog(this)

        dialog.setContentView(dialogBinding)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_corner)
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