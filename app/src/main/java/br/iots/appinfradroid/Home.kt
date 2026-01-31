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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

data class Controle(val nome: String)

class Home : AppCompatActivity() {

    private val listaControles = mutableListOf(
        Controle("Controle da Fita Led"),
        Controle("Controle da TV"),
        Controle("Ar Condicionado")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        configurarToolbar()
        configurarRecyclerView()
        configurarCliques()
    }

    private fun configurarRecyclerView() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerControls)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ControleAdapter(listaControles, this)
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

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddControle::class.java))
        }

        btnTexto.setOnClickListener {
            startActivity(Intent(this, AddControle::class.java))
        }
    }
}

class ControleAdapter(
    private val lista: List<Controle>,
    private val context: AppCompatActivity
) : RecyclerView.Adapter<ControleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNome: TextView = view.findViewById(R.id.textNomeControle)
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
            intent.putExtra("controle_nome", controle.nome)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size
}
