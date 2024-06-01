package com.karaya.tetrahedron.controller

import com.karaya.tetrahedron.domain.Point
import com.karaya.tetrahedron.domain.PointSet
import com.karaya.tetrahedron.service.PointSetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class PointController @Autowired constructor(
    private val pointSetService: PointSetService
) {
    @PostMapping("/points")
    fun uploadPoints(@RequestBody points: List<Point>): ResponseEntity<Void> {
        val savedPointSet = pointSetService.savePointSet(points)
        val location: URI =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(savedPointSet.id)
                .toUri()
        return ResponseEntity.created(location).build()
    }
}