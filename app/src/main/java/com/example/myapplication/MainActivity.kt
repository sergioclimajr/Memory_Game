package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout


class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var grid: GridLayout
    private lateinit var jogoDaMemoria: JogoDaMemoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grid = findViewById(R.id.gridLayout)
        startButton = findViewById(R.id.buttonStart)

        initialize()

        startButton.setOnClickListener {
            novoJogo()
        }
    }

    private fun initialize() {
        jogoDaMemoria = JogoDaMemoria(this, grid) {
            startButton.visibility = View.VISIBLE
        }
        jogoDaMemoria.iniciarJogo()
        jogoDaMemoria.imagesToImageViews()
        jogoDaMemoria.setGrid()
    }

    private fun novoJogo() {
        jogoDaMemoria.iniciarJogo()
        jogoDaMemoria.imagesToImageViews()
        jogoDaMemoria.setGrid()
    }
}
