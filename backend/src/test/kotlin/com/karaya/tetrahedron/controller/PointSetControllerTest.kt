package com.karaya.tetrahedron.controller

import com.karaya.tetrahedron.domain.Point
import com.karaya.tetrahedron.domain.PointSet
import com.karaya.tetrahedron.service.PointSetService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class PointSetControllerTest @Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val pointSetService: PointSetService
) {

    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setup() {
        pointSetService.clear()
    }

    @Test
    fun `upload points`() {
        val points = listOf(
            Point(3.00, 4.00, 5.00, 22),
            Point(2.00, 3.00, 3.00, 3),
            Point(1.00, 2.00, 2.00, 4),
            Point(3.50, 4.50, 5.50, 14),
            Point(2.50, 3.50, 3.50, 24),
            Point(6.70, 32.20, 93.0, 5),
            Point(2.50, 3.00, 7.00, 40)
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(points, headers)
        val postResponse: ResponseEntity<Void> = restTemplate.exchange(
            "http://localhost:$port/api/points",
            HttpMethod.POST,
            request,
            Void::class.java
        )

        assert(postResponse.statusCode == HttpStatus.CREATED)
        assert(postResponse.headers.location != null)
    }
}