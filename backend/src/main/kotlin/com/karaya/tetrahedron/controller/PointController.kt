package com.karaya.tetrahedron.controller

import com.karaya.tetrahedron.domain.Point
import com.karaya.tetrahedron.service.PointSetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class PointController @Autowired constructor(
    private val pointSetService: PointSetService
) {
    @PostMapping("/points")
    fun uploadPoints(@RequestBody points: List<Point>, uriBuilder: UriComponentsBuilder): ResponseEntity<Void> {
        val savedPointSet = pointSetService.savePointSet(points)
        val location: URI =
            uriBuilder
                .path("/api/points/{id}")
                .buildAndExpand(savedPointSet.id)
                .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("points/{id}/tetrahedron")
    fun getSmallestTetrahedron(@PathVariable id: String): ResponseEntity<List<Int>> {
        val pointSet = pointSetService.findPointSetById(id)

        return if(pointSet != null) {
            val result = pointSetService.calculateSmallestTetrahedron(pointSet.points)
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}