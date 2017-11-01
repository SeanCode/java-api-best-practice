package com.dix.base.common

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.TypeRef
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class KUtilTest {
    private val logger = LoggerFactory.getLogger(KUtilTest::class.java)

    @BeforeEach
    fun setUp() {
        Util.init()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun jsonPathBase() {
        val json = "{\"id\":\"1\",\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null},{\"id\":null,\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null}"
        val ctx = JsonPath.parse(json)
        val id = ctx.read<String>("$.id")
        val idx = Util.readValue(ctx,"$.id", object: TypeRef<Int>(){})
        val idy = Util.readValue(ctx,"$.id", object: TypeRef<String>(){})
        val idz = Util.readValue(ctx,"$.idx", object: TypeRef<Int>(){}, 1)
        val ida = Util.readValue(ctx,"$.idx", object: TypeRef<String>(){}, "d")
        logger.info("{} {} {} {} {}", id, idx, idy, idz, ida)

        Assertions.assertEquals(ctx.read<String>("$.id"), "1")
        Assertions.assertEquals(Util.readValue(ctx,"$.id", object: TypeRef<Int>(){}), 1)
        Assertions.assertEquals(Util.readValue(ctx,"$.id", object: TypeRef<String>(){}), "1")
        Assertions.assertEquals(Util.readValue(ctx,"$.idx", object: TypeRef<Int>(){}, 1), 1)
        Assertions.assertEquals(Util.readValue(ctx,"$.idx", object: TypeRef<String>(){}, "d"), "d")
    }

    @Test
    fun jsonPathList() {
        val json = "{\"appID\":\"android354359083251324\",\"cmd\":\"500\",\"devID\":\"A952CB0A004B1200\",\"endpoints\":[{\"clusters\":[{\"attributes\":[{\"attributeId\":0,\"attributeValue\":\"0\"}],\"clusterId\":6}],\"endpointName\":\"\",\"endpointNumber\":1,\"endpointStatus\":\"1\",\"endpointType\":2},{\"clusters\":[{\"attributes\":[{\"attributeId\":0,\"attributeValue\":\"0\"}],\"clusterId\":6}],\"endpointName\":\"\",\"endpointNumber\":2,\"endpointStatus\":\"1\",\"endpointType\":2}],\"extData\":\"WwogICB7CiAgICAgICJiaW5kRGV2RVAiIDogIiIsCiAgICAgICJiaW5kRGV2SUQiIDogIiIsCiAgICAgICJiaW5kRGV2dHlwZSIgOiAiIiwKICAgICAgImVuZHBvaW50TnVtYmVyIiA6ICIxIiwKICAgICAgImVwTmFtZSIgOiAiIiwKICAgICAgIm1vZGUiIDogIjEiLAogICAgICAibmFtZSIgOiAiIiwKICAgICAgInNjZW5lSUQiIDogIiIsCiAgICAgICJzY2VuZUljb24iIDogIiIsCiAgICAgICJzY2VuZU5hbWUiIDogIiIKICAgfSwKICAgewogICAgICAiYmluZERldkVQIiA6ICIiLAogICAgICAiYmluZERldklEIiA6ICIiLAogICAgICAiYmluZERldnR5cGUiIDogIiIsCiAgICAgICJlbmRwb2ludE51bWJlciIgOiAiMiIsCiAgICAgICJlcE5hbWUiIDogIiIsCiAgICAgICJtb2RlIiA6ICIxIiwKICAgICAgIm5hbWUiIDogIiIsCiAgICAgICJzY2VuZUlEIiA6ICIiLAogICAgICAic2NlbmVJY29uIiA6ICIiLAogICAgICAic2NlbmVOYW1lIiA6ICIiCiAgIH0KXQo=\",\"gwID\":\"50294D104DAB\",\"gwName\":\"W1体验网关\",\"mode\":1,\"name\":\"金属二路开关\",\"roomID\":\"2\",\"roomName\":\"大展板\",\"time\":\"1509440603763\",\"type\":\"An\",\"userID\":\"fe3e18e0f143468e8a789bad4a00f7fe\",\"zVer\":\"11\"}"
        val ctx = JsonPath.parse(json)
        val endpoints = ctx.read("$.endpoints", object: TypeRef<List<Map<String, Any>>>(){})
//        logger.info("{}", endpoints)
//        logger.info("{}", ctx.read("$.appID", object: TypeRef<String>(){}))
//        logger.info("{}", Util.readValue(ctx,"$.appID", object: TypeRef<Int>(){}))
//        logger.info("{}", ctx.read("$.appID", object: TypeRef<Int>(){}))
//        logger.info("{}", ctx.read<String>("$.appID"))
//        logger.info("{}", ctx.read<Int>("$.appID"))
    }

}