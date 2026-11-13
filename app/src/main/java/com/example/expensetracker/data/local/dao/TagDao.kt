package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.TagEntity
import com.example.expensetracker.data.local.entity.TransactionTagCrossRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Query("SELECT * FROM tags WHERE name = :name")
    suspend fun getTagByName(name: String): TagEntity?

    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTagsFlow(): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionTagCrossRef(crossRef: TransactionTagCrossRefEntity)

    @Query("DELETE FROM transaction_tag_cross_ref WHERE transactionId = :transactionId")
    suspend fun deleteTagsForTransaction(transactionId: Long): Int

    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN transaction_tag_cross_ref ref ON t.id = ref.tagId
        WHERE ref.transactionId = :transactionId
    """)
    fun getTagsForTransactionFlow(transactionId: Long): Flow<List<TagEntity>>

    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN transaction_tag_cross_ref ref ON t.id = ref.tagId
        WHERE ref.transactionId = :transactionId
    """)
    suspend fun getTagsForTransaction(transactionId: Long): List<TagEntity>
}