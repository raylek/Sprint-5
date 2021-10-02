package ru.sber.serialization

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.fasterxml.jackson.databind.node.IntNode

import com.fasterxml.jackson.core.JsonProcessingException

import java.io.IOException

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*

import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode


class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule().addDeserializer(Client7::class.java, ItemDeserializer()))


        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    class ItemDeserializer : StdDeserializer<Client7>(Client7::class.java) {

        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Client7 {
            val node = jp.readValueAsTree<ObjectNode>().get("client").toString()
            val arr = node.replace("\"", "").split(" ")

            return Client7(arr[1], arr[0], arr[2])
        }
    }
}
