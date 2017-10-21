package com.dix.app.controller

import com.dix.base.common.DataResponse
import com.dix.base.common.Util
import com.jayway.jsonpath.JsonPath
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class KestController {
    private val logger = LoggerFactory.getLogger(KestController::class.java)

    @RequestMapping("/json-path")
    fun jsonPath(): DataResponse {

        val json = "{\"id\":null,\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null},{\"id\":null,\"uid\":null,\"username\":\"\",\"phone\":\"\",\"password\":\"\",\"email\":\"\",\"name\":\"\",\"tel\":\"\",\"gender\":0,\"birthday\":0,\"avatar\":\"\",\"source_id\":0,\"source_type\":0,\"source_client\":0,\"weight\":0,\"create_time\":null,\"update_time\":null}"
        val ctx = JsonPath.parse(json)
        val id = ctx.read<String>("$.id")
        logger.info("{}", id)

        return DataResponse.create()
    }
}