package br.iots.appinfradroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.iots.appinfradroid.data.AppDatabase
import br.iots.appinfradroid.data.BotaoEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class BotoesControle : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: BotaoAdapter
    private var controleId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botoes_controle)

        db = AppDatabase.getDatabase(this)
        controleId = intent.getIntExtra("CONTROLE_ID", -1)
        val controleNome = intent.getStringExtra("CONTROLE_NOME") ?: "Controle"

        if (controleId == -1) {
            finish()
            return
        }

        configurarToolbar(controleNome)
        configurarRecycler()
        configurarFab()
        observarDados()
    }

    private fun configurarToolbar(titulo: String) {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val tvTitulo = toolbar.findViewById<TextView>(R.id.toolbar_title) ?: toolbar.getChildAt(0) as? TextView
        
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // Se houver um TextView dentro da toolbar (como definido no XML), usamos ele
        // Caso contrário, o título padrão do suporte à action bar
        tvTitulo?.text = titulo

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#333333")
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false
    }

    private fun configurarRecycler() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerBotoes)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = BotaoAdapter(emptyList()) { botao ->
            Toast.makeText(this, "Enviando: ${botao.codigoHex}", Toast.LENGTH_SHORT).show()
        }
        recycler.adapter = adapter
    }

    private fun observarDados() {
        lifecycleScope.launch {
            db.botaoDao().getBotoesByControle(controleId).collect { lista ->
                adapter.updateList(lista)
            }
        }
    }

    private fun configurarFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddBotao)
        fab.setOnClickListener {
            val intent = Intent(this, AddBotao::class.java)
            intent.putExtra("CONTROLE_ID", controleId)
            startActivity(intent)
        }
    }
}

class BotaoAdapter(
    private var lista: List<BotaoEntity>,
    private val onClick: (BotaoEntity) -> Unit
) : RecyclerView.Adapter<BotaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNome: TextView = view.findViewById(R.id.textNomeBotao)
    }

    fun updateList(novaLista: List<BotaoEntity>) {
        lista = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_botao_controle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val botao = lista[position]
        holder.textNome.text = botao.nome
        holder.itemView.setOnClickListener { onClick(botao) }
    }

    override fun getItemCount(): Int = lista.size
}
