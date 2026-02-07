package br.iots.appinfradroid.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "botoes",
    foreignKeys = [
        ForeignKey(
            entity = ControleEntity::class,
            parentColumns = ["id"],
            childColumns = ["controleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("controleId")]
)
data class BotaoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val controleId: Int,
    val nome: String,
    val codigoHex: String
)
