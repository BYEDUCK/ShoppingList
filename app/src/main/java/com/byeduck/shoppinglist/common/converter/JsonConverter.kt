package com.byeduck.shoppinglist.common.converter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JsonConverter {

    companion object {
        private val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()

        fun gson(): Gson = gson

        class LocalDateSerializer : JsonSerializer<LocalDate> {
            override fun serialize(
                src: LocalDate?,
                typeOfSrc: Type?,
                context: JsonSerializationContext?
            ): JsonElement {
                return JsonPrimitive(src.toString())
            }
        }

        class LocalDateDeserializer : JsonDeserializer<LocalDate> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDate {
                return LocalDate.parse(json?.asString, DateTimeFormatter.ISO_DATE)
            }

        }
    }

}