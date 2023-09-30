package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.GridLayout
import android.widget.ImageView

class JogoDaMemoria(
    private val contexto: Context,
    private val gridLayout: GridLayout,
    private val aoCompletarJogo: () -> Unit
) {

    private val imagensLista = arrayOf(
        R.drawable.tm01,
        R.drawable.tm02,
        R.drawable.tm03,
        R.drawable.tm04,
        R.drawable.tm05,
        R.drawable.tm06,
        R.drawable.tm07,
        R.drawable.tm08
    )

    private val listaDeCards = mutableListOf<Card>()
    private val cardsVirados = mutableListOf<Card>()
    private var paresCorrepondentes = 0
    private val manipulador = Handler(Looper.getMainLooper())

    init {
        iniciarJogo()
    }

    fun iniciarJogo() {
        listaDeCards.clear()
        imagensLista.shuffle()

        for (i in 0 until 16) {
            val card = Card(i, imagensLista[i % imagensLista.size])
            listaDeCards.add(card)
        }
        listaDeCards.shuffle()
    }

    fun imagesToImageViews() {
        for (i in 1..16) {
            val imageViewId = contexto.resources.getIdentifier("imageView$i", "id", contexto.packageName)
            val imageView = gridLayout.findViewById<ImageView>(imageViewId)
            val card = listaDeCards[i - 1]

            if (card.voltadaParaCima) {
                imageView.setImageResource(card.idImagem)
            } else {
                imageView.setImageResource(R.drawable.back)
            }

            imageView.setOnClickListener {
                clicked(card, imageView)
            }
        }
    }

    private fun clicked(clickedCard: Card, imageView: ImageView) {
        if (!clickedCard.voltadaParaCima && cardsVirados.size < 2) {
            clickedCard.voltadaParaCima = true
            imageView.setImageResource(clickedCard.idImagem)
            cardsVirados.add(clickedCard)

            if (cardsVirados.size == 2) {
                val (card1, card2) = cardsVirados

                if (card1.idImagem == card2.idImagem) {
                    card1.combinada = true
                    card2.combinada = true
                    paresCorrepondentes++

                    cardsVirados.clear()

                    verificarSeFimDeJogo()
                } else {
                    manipulador.removeCallbacksAndMessages(null)
                    manipulador.postDelayed({
                        cardsVirados.forEach {
                            it.voltadaParaCima = false
                            val cardImageView = encontrarCard(it.idCard)
                            cardImageView?.setImageResource(R.drawable.back)
                        }
                        cardsVirados.clear()
                        setGrid()
                    }, 1500)
                }
            }
        }
    }

    private fun encontrarCard(cardId: Int): ImageView? {
        val imageViewId = contexto.resources.getIdentifier("imageView$cardId", "id", contexto.packageName)
        return gridLayout.findViewById(imageViewId)
    }

    fun setGrid() {
        for (i in 1..16) {
            val imageView = encontrarCard(i)
            if (imageView != null) {
                val card = listaDeCards[i - 1]

                if (card.voltadaParaCima) {
                    imageView.setImageResource(card.idImagem)
                } else {
                    imageView.setImageResource(R.drawable.back)
                }
            }
        }

        verificarSeFimDeJogo()
    }

    private fun verificarSeFimDeJogo() {
        if (paresCorrepondentes == listaDeCards.size / 2) {
            // Todas as cartas estão emparelhadas, o jogador ganhou.
            val intent = Intent(contexto, MainActivityGanhou::class.java)
            contexto.startActivity(intent)
        } else if (cardsVirados.size == 2 && !cardsVirados[0].combinada && !cardsVirados[1].combinada) {
            // O jogador perdeu, inicie a tela de derrota.
            val intent = Intent(contexto, MainActivityPerdeu::class.java)
            contexto.startActivity(intent)
        }
    }


}