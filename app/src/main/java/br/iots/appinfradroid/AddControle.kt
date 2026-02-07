package br.iots.appinfradroid

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import br.iots.appinfradroid.data.AppDatabase
import br.iots.appinfradroid.data.ControleEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddControle : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_controle)

        db = AppDatabase.getDatabase(this)

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
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    private fun configurarSalvar() {
        val editNome = findViewById<TextInputEditText>(R.id.editNomeControle)
        val editFreq = findViewById<TextInputEditText>(R.id.editFreqControle)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)

        btnSave.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val freq = editFreq.text.toString().trim()

            if (nome.isNotEmpty() && freq.isNotEmpty()) {
                val controle = ControleEntity(nome = nome, frequencia = freq)
                lifecycleScope.launch {
                    db.controleDao().insert(controle)
                    Toast.makeText(this@AddControle, "Controle salvo!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
