package com.mobbile.paul.mttms.providers

import androidx.room.*
import com.mobbile.paul.mttms.models.EntityModules
import com.mobbile.paul.mttms.models.EntitySpiners



@Dao
interface AppDao {

    @Query("DELETE FROM modules")
    fun deleteModules()

    @Query("DELETE FROM replist")
    fun deleteRepList()

    @Query("DELETE FROM spiners")
    fun deleteSpiners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveModulesANDspiners(
        modules: List<EntityModules>,
        spinners: List<EntitySpiners>
    )

    @Query("SELECT * FROM modules")
    fun fetchModules(): List<EntityModules>

}


