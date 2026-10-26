
package cl.duoc.proyectolince.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ServicioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LinceDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao

    companion object {

        @Volatile
        private var INSTANCE: LinceDatabase? = null

        fun obtenerDatabase(context: Context): LinceDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    LinceDatabase::class.java,
                    "southbound_database"
                ).build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
