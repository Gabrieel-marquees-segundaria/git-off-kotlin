package com.g4br3.sitedentrodeapp.dataBase

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// Repository

@Entity(tableName = "Path")
/**
 *
 *
 *
 * */
data class Path(
    @PrimaryKey(autoGenerate = true) val uid: Int =0,
    @ColumnInfo(name = "string_uri") val stringUri: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "level") val level: Int?
)

@Dao
interface PathDao {
    @Insert
    suspend fun inserir(path: Path)
    @Query("SELECT * FROM path") suspend fun listar(): List<Path>
    @Query("SELECT * FROM path WHERE level LIKE :search") fun loadLevel(search: String?): List<Path>
    @Query("SELECT * FROM path WHERE name LIKE :search") fun loadName(search: String?):  List<Path>
    //search = "%fido%";
    @Delete
    suspend fun deletar(path: Path)
}

@Database(entities = [Path::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pathDao(): PathDao
}

object Banco {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "repository.db"
            ).build().also { INSTANCE = it }
        }
    }
}


