package com.mobbile.paul.mttms.providers

import androidx.room.*
import com.mobbile.paul.mttms.models.*


@Dao
interface AppDao {

    /*@Query("DELETE FROM replist")
    fun deleteRepList()

     */

    /*@Query("DELETE FROM allcustomers")
    fun deleteAllcustomers()

     */

   /* @Query("insert into allcustomers (auto, employeeid, ecode, custcode, fullname, mode) values (:auto, :employeeid,:ecode,:custcode,:fullname, :mode)")
    fun insertIntoAllcustomers(auto:Int, employeeid:Int, ecode:String, custcode:String, fullname:String, mode:String)
    */

    /*@Query("SELECT * FROM allcustomers")
    fun fetchEntityAllCustomersList(): List<EntityAllCustomersList>
    */

    @Query("DELETE FROM modules")
    fun deleteModules()

    @Query("DELETE FROM spiners")
    fun deleteSpiners()

    @Query("DELETE FROM alloutlets")
    fun deleteAlloutlets()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveModulesANDspiners(
        modules: List<EntityModules>,
        spinners: List<EntitySpiners>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntityAllOutletsList(
        alloutlets: List<EntityAllOutletsList>
    )

    @Query("SELECT * FROM modules")
    fun fetchModules(): List<EntityModules>

    @Query("SELECT * FROM alloutlets")
    fun fetchEntityAllOutletsList(): List<EntityAllOutletsList>

    @Query("SELECT count(auto) from alloutlets")
    fun naviBtwcustAndRep():Int

    @Query("SELECT * FROM salesentries order by seperator asc")
    fun fetchAllEntryPerDay(): List<EntityGetSalesEntry>

    @Query("Insert into custometvisitsequence (id, nexts, self) values (:id, :nexts, :self)")
    fun SequencetManager(id: Int, nexts:Int, self:String)

    @Query("select * from custometvisitsequence where id=:id limit 1")
    fun ValidateSeque(id: Int): EntityCustomerVisitSequence

    @Query("update custometvisitsequence set nexts=:nexts,self=:self where id=:id")
    fun UpdateSeque(id: Int,nexts:Int,self:String)

    @Query("Update alloutlets set entry_time =:time WHERE auto = :auto")
    fun setEntryTime(time: String, auto:Int)

    @Query("Update alloutlets set entry_time =:time WHERE auto = 1")
    fun setAttendantTime(time: String)

    @Query("SELECT * FROM spiners")
    fun fetchSpinners() : List<EntitySpiners>

    @Query("DELETE FROM salesentries")
    fun deleteEntityGetSalesEntry()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSalesEntry(
        salesen: List<EntityGetSalesEntry>
    )

    @Query("UPDATE salesentries SET inventory=:inventory, pricing=:pricing, entry_time=:entry_time, controlpricing=:controlpricing, controlinventory = :controlinventory where  product_code=:product_code")
    fun updateDailySales(inventory: Double, pricing: Int, entry_time: String, controlpricing:String, controlinventory:String, product_code:String)

    @Query("SELECT count(id) FROM salesentries WHERE   controlpricing = '' OR controlinventory = ''")
    fun validateSalesEntry() : Int

    @Query("SELECT  SUM(inventory) AS sinventory, SUM(pricing) AS spricing FROM salesentries")
    fun sumAllSalesEntry(): SumSales

    @Query("SELECT * FROM salesentries")
    fun pullAllSalesEntry() : List<EntityGetSalesEntry>

}




