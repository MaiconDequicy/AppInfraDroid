/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.iots.appinfradroid

import android.content.Context
import android.graphics.Color
import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class Home : AppCompatActivity() {

    private val TAG = "InfraDroidHome"
    private var mIrManager: ConsumerIrManager? = null

    // Padrões IR fornecidos pelo seu amigo (Frequência 38kHz)
    private val FREQUENCY = 38000

    private val PATTERN_ON = intArrayOf(
        9000, 4500, 600, 500, 600, 550, 600, 500, 600, 550, 600, 500, 600, 550, 600, 550, 
        550, 550, 600, 1650, 550, 1650, 600, 1650, 550, 1650, 600, 550, 600, 1600, 600, 1650, 
        600, 1600, 600, 1650, 600, 1650, 550, 550, 600, 550, 550, 550, 600, 550, 600, 500, 
        600, 550, 600, 500, 600, 550, 600, 1600, 600, 1650, 600, 1600, 600, 1650, 600, 1650, 
        550, 1650, 600
    )

    private val PATTERN_OFF = intArrayOf(
        9000, 4500, 550, 550, 600, 550, 550, 550, 600, 550, 600, 500, 600, 550, 600, 500, 
        600, 550, 600, 1600, 600, 1650, 600, 1650, 550, 1650, 600, 550, 600, 1600, 600, 1650, 
        600, 1600, 600, 550, 600, 1600, 600, 550, 600, 550, 550, 550, 600, 550, 550, 550, 
        600, 550, 550, 1650, 600, 550, 600, 1600, 600, 1650, 600, 1600, 600, 1650, 600, 1650, 
        550, 1650, 600
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        configurarToolbar()
        
        // No AOSP, usamos o serviço oficial de Infravermelho
        mIrManager = getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager

        if (mIrManager == null || !mIrManager!!.hasIrEmitter()) {
            Log.e(TAG, "Emissor IR não encontrado neste dispositivo")
            Toast.makeText(this, "Hardware IR não disponível", Toast.LENGTH_LONG).show()
        }

        configurarBotoes()
    }

    private fun configurarBotoes() {
        findViewById<MaterialButton>(R.id.btn_on).setOnClickListener {
            transmitirComando(PATTERN_ON, "LIGAR")
        }

        findViewById<MaterialButton>(R.id.btn_off).setOnClickListener {
            transmitirComando(PATTERN_OFF, "DESLIGAR")
        }
        
        // Outros botões podem ser mapeados se você tiver os códigos
    }

    private fun transmitirComando(padrao: IntArray, nome: String) {
        if (mIrManager?.hasIrEmitter() == true) {
            try {
                mIrManager?.transmit(FREQUENCY, padrao)
                Log.d(TAG, "Comando IR enviado: $nome")
                Toast.makeText(this, "Enviado: $nome", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao transmitir IR", e)
            }
        } else {
            Toast.makeText(this, "Emissor IR não disponível", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#333333")
        window.navigationBarColor = Color.parseColor("#333333")

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
    }
}
