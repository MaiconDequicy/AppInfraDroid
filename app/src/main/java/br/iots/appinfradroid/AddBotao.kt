package br.iots.appinfradroid

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import br.iots.appinfradroid.data.AppDatabase
import br.iots.appinfradroid.data.BotaoEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddBotao : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var controleId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_botao)

        db = AppDatabase.getDatabase(this)
        controleId = intent.getIntExtra("CONTROLE_ID", -1)

        if (controleId == -1) {
            finish()
            return
        }

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
        val editNome = findViewById<TextInputEditText>(R.id.editNomeBotao)
        val editHex = findViewById<TextInputEditText>(R.id.editHex)
        val btnSalvar = findViewById<MaterialButton>(R.id.btnSalvarHex)

        btnSalvar.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val hex = editHex.text.toString().trim()

            if (nome.isNotEmpty() && hex.isNotEmpty()) {
                lifecycleScope.launch {
                    val botao = BotaoEntity(
                        controleId = controleId,
                        nome = nome,
                        codigoHex = hex
                    )
                    db.botaoDao().insert(botao)
                    Toast.makeText(this@AddBotao, "Botão salvo!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
