package com.lab.sports.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lab.sports.data.local.dao.FavoriteDao
import com.lab.sports.data.local.entities.TeamEntity

@Database(entities = [TeamEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
}