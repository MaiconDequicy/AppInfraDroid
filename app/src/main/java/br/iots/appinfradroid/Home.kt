package br.iots.appinfradroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.iots.appinfradroid.data.AppDatabase
import br.iots.appinfradroid.data.ControleEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: ControleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = AppDatabase.getDatabase(this)

        configurarToolbar()
        configurarRecyclerView()
        configurarCliques()
        observarDados()
    }

    private fun configurarRecyclerView() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerControls)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ControleAdapter(emptyList(), this)
        recycler.adapter = adapter
    }

    private fun observarDados() {
        lifecycleScope.launch {
            db.controleDao().getAll().collect { lista ->
                adapter.updateList(lista)
            }
        }
    }

    private fun configurarToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#333333")
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false
    }

    private fun configurarCliques() {
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabClose)
        val btnTexto = findViewById<MaterialButton>(R.id.btnAddControl)

        val openAddControle = {
            startActivity(Intent(this, AddControle::class.java))
        }

        fabAdd.setOnClickListener { openAddControle() }
        btnTexto.setOnClickListener { openAddControle() }
    }
}

class ControleAdapter(
    private var lista: List<ControleEntity>,
    private val context: AppCompatActivity
) : RecyclerView.Adapter<ControleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNome: TextView = view.findViewById(R.id.textNomeControle)
    }

    fun updateList(novaLista: List<ControleEntity>) {
        lista = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_controle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val controle = lista[position]
        holder.textNome.text = controle.nome

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BotoesControle::class.java)
            intent.putExtra("CONTROLE_ID", controle.id)
            intent.putExtra("CONTROLE_NOME", controle.nome)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size
}
