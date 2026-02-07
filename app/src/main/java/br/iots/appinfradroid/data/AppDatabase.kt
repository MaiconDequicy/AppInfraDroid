package br.iots.appinfradroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ControleEntity::class, BotaoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun controleDao(): ControleDao
    abstract fun botaoDao(): BotaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "infradroid_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
