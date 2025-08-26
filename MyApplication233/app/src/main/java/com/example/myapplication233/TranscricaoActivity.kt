package com.example.myapplication233

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class TranscricaoActivity : AppCompatActivity() {

    // Reconhecedor de fala
    private lateinit var reconhecedorDeVoz: SpeechRecognizer

    // Elementos da interface
    private lateinit var textoTranscricao: TextView
    private lateinit var botaoIniciarGravacao: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transcricao)

        // Ligação dos elementos da tela
        textoTranscricao = findViewById(R.id.txtResult)
        botaoIniciarGravacao = findViewById(R.id.btnStart)

        // Verifica e pede permissão de microfone
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        // Cria reconhecedor de voz
        reconhecedorDeVoz = SpeechRecognizer.createSpeechRecognizer(this)

        // Configuração do intent de reconhecimento
        val intentReconhecimento = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        // Ação do botão
        botaoIniciarGravacao.setOnClickListener {
            textoTranscricao.text = "Ouvindo..."
            reconhecedorDeVoz.startListening(intentReconhecimento)
        }

        // Listener para o reconhecimento de voz
        reconhecedorDeVoz.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                textoTranscricao.text = "Erro ao reconhecer fala."
            }

            override fun onResults(results: Bundle?) {
                val frasesReconhecidas = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                textoTranscricao.text = frasesReconhecidas?.joinToString("\n")
                    ?: "Nada reconhecido."
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }
}
