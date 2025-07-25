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
import androidx.room.RawQuery
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

// Repository

@Entity(tableName = "Path")
/**
 *
 *
 *
 * */
data class Path(
    @PrimaryKey(autoGenerate = true) val uid: Int =0,
    @ColumnInfo(name = "stringUri") val stringUri: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "level") val level: Int?,
    @ColumnInfo(name = "father") val father: String?
)

@Dao
interface PathDao {
    @Insert
    suspend fun inserir(path: Path)
    @Query("SELECT * FROM path") suspend fun listar(): List<Path>
    @Query("SELECT * FROM path WHERE level LIKE :search") fun loadLevel(search: String?): List<Path>
    //search = "%fido%";
    @Query("SELECT * FROM path WHERE name LIKE :search")
    fun searchByName(search: String): List<Path>

    @Query("SELECT * FROM path WHERE type LIKE :search")
    fun searchByType(search: String): List<Path>
    @RawQuery
    fun searchByRawQuery(query: SupportSQLiteQuery): List<Path>
//    val query = SimpleSQLiteQuery("SELECT * FROM path WHERE $column LIKE ?", arrayOf("%$search%"))
//    val result = pathDao.searchByRawQuery(query)

    @Delete
    suspend fun deletar(path: Path)
    @Query("DELETE FROM Path")
    suspend fun deletarTudo()

}

// val resultados = buscarPorColuna(pathDao, "type", "imagem")


@Database(entities = [Path::class], version = 2)
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


