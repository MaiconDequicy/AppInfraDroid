package br.iots.appinfradroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ControleDao {
    @Query("SELECT * FROM controles")
    fun getAll(): Flow<List<ControleEntity>>

    @Insert
    suspend fun insert(controle: ControleEntity)
}
