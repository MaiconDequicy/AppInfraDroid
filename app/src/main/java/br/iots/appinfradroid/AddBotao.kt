package br.iots.appinfradroid

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddBotao : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_botao)

        configurarToolbar()
        configurarSalvar()
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
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false
    }

    private fun configurarSalvar() {
        val editHex = findViewById<TextInputEditText>(R.id.editHex)
        val btnSalvar = findViewById<MaterialButton>(R.id.btnSalvarHex)

        btnSalvar.setOnClickListener {
            val hex = editHex.text.toString().trim()

            if (hex.isNotEmpty()) {
                // depois vamos devolver esse HEX para BotoesControle
                finish()
            }
        }
    }
}
