package com.example.thenewsappmobile.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.thenewsappmobile.models.Source

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


// Purpose : enable the Room database to properly handle the Source object,
// which is a custom class, when storing and retrieving Article entities.

@TypeConverters
class Converters {

    // Purpose : convert a Source object into a String representation. ( We just need name )
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }


    // Purpose : convert a String representation back into a Source object.
    @TypeConverter
    fun toSource(name : String): Source{
        return Source(name, name)
    }
}