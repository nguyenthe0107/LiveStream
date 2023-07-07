package olmo.wellness.android.data.model.user_info

import androidx.room.*

@Dao
interface UserInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(userInfoEntity: UserInfoEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserInfo(userInfoEntity: UserInfoEntity)

    @Query("select * from user_info_table")
    suspend fun getAllUserInfo(): List<UserInfoEntity>

    @Query("select * from user_info_table WHERE userId=:userId")
    suspend fun getUserInfo(userId: Int): UserInfoEntity?

    @Query("delete from user_info_table")
    fun deleteAllUserInfo()

    @Delete
    fun deleteUserInfo(userInfoEntity: UserInfoEntity);

}