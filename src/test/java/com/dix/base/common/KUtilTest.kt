package com.dix.base.common

import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class KUtilTest {
    private val logger = LoggerFactory.getLogger(KUtilTest::class.java)

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun jsonPath() {
        val json = "{\"id\":\"1\",\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null},{\"id\":null,\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null}"
        val ctx = JsonPath.parse(json)
        val id = ctx.read<String>("$.id")
        val idx = Util.readValue(ctx,"$.id", Int::class.java)
        val idy = Util.readValue(ctx,"$.id", String::class.java)
        val idz = Util.readValue(ctx,"$.idx", Int::class.java, 1)
        val ida = Util.readValue(ctx,"$.idx", String::class.java, "d")
        logger.info("{} {} {} {} {}", id, idx, idy, idz, ida)

        Assertions.assertEquals(ctx.read<String>("$.id"), "1")
        Assertions.assertEquals(Util.readValue(ctx,"$.id", Int::class.java), null)
        Assertions.assertEquals(Util.readValue(ctx,"$.id", String::class.java), "1")
        Assertions.assertEquals(Util.readValue(ctx,"$.idx", Int::class.java, 1), 1)
        Assertions.assertEquals(Util.readValue(ctx,"$.idx", String::class.java, "d"), "d")
    }

}