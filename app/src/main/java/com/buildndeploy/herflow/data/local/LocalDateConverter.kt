package com.buildndeploy.herflow.data.local

import androidx.room.TypeConverter
import com.buildndeploy.herflow.domain.model.CervicalMucusType
import com.buildndeploy.herflow.domain.model.FlowLevel
import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun fromFlowLevel(value: FlowLevel?): String? = value?.name

    @TypeConverter
    fun toFlowLevel(value: String?): FlowLevel? = value?.let(FlowLevel::valueOf)

    @TypeConverter
    fun fromMoodType(value: MoodType?): String? = value?.name

    @TypeConverter
    fun toMoodType(value: String?): MoodType? = value?.let(MoodType::valueOf)

    @TypeConverter
    fun fromSymptomList(value: List<SymptomType>?): String? =
        value?.joinToString(separator = ",") { it.name }

    @TypeConverter
    fun toSymptomList(value: String?): List<SymptomType> =
        value
            ?.takeIf { it.isNotBlank() }
            ?.split(",")
            ?.map(SymptomType::valueOf)
            ?: emptyList()

    @TypeConverter
    fun fromCervicalMucusType(value: CervicalMucusType?): String? = value?.name

    @TypeConverter
    fun toCervicalMucusType(value: String?): CervicalMucusType? = value?.let(CervicalMucusType::valueOf)
}
