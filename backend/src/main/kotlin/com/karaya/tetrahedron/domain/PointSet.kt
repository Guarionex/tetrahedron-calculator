package com.karaya.tetrahedron.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "pointSets")
data class PointSet(
    @Id val id: String? = null,
    val points: List<Point>
)

data class Point(
    val x: Double,
    val y: Double,
    val z: Double,
    val n: Int
)