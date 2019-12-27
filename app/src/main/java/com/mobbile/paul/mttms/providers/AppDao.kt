package com.mobbile.paul.mttms.providers

import androidx.room.*
import com.mobbile.paul.mttms.models.*


@Dao
interface AppDao {

    @Query("DELETE FROM modules")
    fun deleteModules()

    /*@Query("DELETE FROM replist")
    fun deleteRepList()

     */



    @Query("DELETE FROM spiners")
    fun deleteSpiners()

    /*@Query("DELETE FROM allcustomers")
    fun deleteAllcustomers()

     */

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

   /* @Query("insert into allcustomers (auto, employeeid, ecode, custcode, fullname, mode) values (:auto, :employeeid,:ecode,:custcode,:fullname, :mode)")
    fun insertIntoAllcustomers(auto:Int, employeeid:Int, ecode:String, custcode:String, fullname:String, mode:String)
    */

    /*@Query("SELECT * FROM allcustomers")
    fun fetchEntityAllCustomersList(): List<EntityAllCustomersList>
     */

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

    @Query("UPDATE salesentries SET orders=:orders, inventory=:inventory, pricing=:pricing, entrytime=:entry_time, orderrice=:salesprice, contorder=:contOrder, contprincing=:contPrincing, continventory=:contInventory, mtamt = mtcom * :orders where  productid=:product_id")
    fun updateDailySales(orders: Double, inventory: Double, pricing: Int, entry_time: String,  product_id: String, salesprice: Double, contOrder: String, contPrincing: String, contInventory: String)

    @Query("SELECT count(id) FROM salesentries WHERE contorder= '' OR contprincing = '' OR continventory = ''")
    fun validateSalesEntry() : Int

    @Query("SELECT count(auto) from alloutlets")
    fun naviBtwcustAndRep():Int

    @Query("SELECT * FROM salesentries order by seperator asc")
    fun fetchAllEntryPerDay(): List<EntityGetSalesEntry>


    @Query("SELECT SUM(orders) AS sorder, SUM(inventory) AS sinventory, SUM(pricing) AS spricing, SUM(price*orders) AS stotalsum  FROM salesentries")
    fun sumAllSalesEntry(): SumSales

    @Query("Insert into custometvisitsequence (id, nexts, self) values (:id, :nexts, :self)")
    fun SequencetManager(id: Int, nexts:Int, self:String)

    @Query("select * from custometvisitsequence where id=:id limit 1")
    fun ValidateSeque(id: Int): EntityCustomerVisitSequence

    @Query("update custometvisitsequence set nexts=:nexts,self=:self where id=:id")
    fun UpdateSeque(id: Int,nexts:Int,self:String)

}




