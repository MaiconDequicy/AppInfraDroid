package br.iots.appinfradroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BotaoDao {
    @Query("SELECT * FROM botoes WHERE controleId = :controleId")
    fun getBotoesByControle(controleId: Int): Flow<List<BotaoEntity>>

    @Insert
    suspend fun insert(botao: BotaoEntity)
}
