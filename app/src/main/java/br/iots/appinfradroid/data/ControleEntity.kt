package br.iots.appinfradroid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "controles")
data class ControleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val frequencia: String
)
