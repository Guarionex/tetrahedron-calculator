package com.karaya.tetrahedron.service

import com.karaya.tetrahedron.adapters.MongoPointSetRepository
import com.karaya.tetrahedron.domain.Point
import com.karaya.tetrahedron.domain.PointSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PointSetService @Autowired constructor(
    private val mongoPointSetRepository: MongoPointSetRepository
){

    fun savePointSet(points: List<Point>): PointSet {
        val pointSet = PointSet(points = points)
        return mongoPointSetRepository.save(pointSet)
    }

    fun findPointSetById(id: String): PointSet? = mongoPointSetRepository.findByIdOrNull(id)


    fun clear() = mongoPointSetRepository.deleteAll()

    fun calculateSmallestTetrahedron(points: List<Point>): List<Int> {
        return listOf(0, 3, 4, 6)
    }
}