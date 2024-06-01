package com.karaya.tetrahedron.service

import com.karaya.tetrahedron.adapters.MongoPointSetRepository
import com.karaya.tetrahedron.domain.Point
import com.karaya.tetrahedron.domain.PointSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class PointSetService @Autowired constructor(
    private val mongoPointSetRepository: MongoPointSetRepository
) {

    fun savePointSet(points: List<Point>): PointSet {
        val pointSet = PointSet(points = points)
        return mongoPointSetRepository.save(pointSet)
    }

    fun findPointSetById(id: String): PointSet? = mongoPointSetRepository.findByIdOrNull(id)


    fun clear() = mongoPointSetRepository.deleteAll()

    fun calculateSmallestTetrahedron(points: List<Point>): List<Int> {
        var minVolume = Double.MAX_VALUE
        var result = listOf<Int>()

        val combinations = points.indices.toList().combinationsOf4()

        for (combination in combinations) {
            val p1 = points[combination[0]]
            val p2 = points[combination[1]]
            val p3 = points[combination[2]]
            val p4 = points[combination[3]]

            if (p1.n + p2.n + p3.n + p4.n == 100) {
                val volume = calculateTetrahedronVolume(p1, p2, p3, p4)
                if (volume < minVolume) {
                    minVolume = volume
                    result = combination
                }

            }
        }

        return result
    }

    private fun calculateTetrahedronVolume(p1: Point, p2: Point, p3: Point, p4: Point): Double {
        val vectorAB = arrayOf(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
        val vectorAC = arrayOf(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z)
        val vectorAD = arrayOf(p4.x - p1.x, p4.y - p1.y, p4.z - p1.z)

        val crossX = vectorAB[1] * vectorAC[2] - vectorAB[2] * vectorAC[1]
        val crossY = vectorAB[2] * vectorAC[0] - vectorAB[0] * vectorAC[2]
        val crossZ = vectorAB[0] * vectorAC[1] - vectorAB[1] * vectorAC[0]

        val dotProduct = vectorAD[0] * crossX + vectorAD[1] * crossY + vectorAD[2] * crossZ

        return abs(dotProduct) / 6.0
    }

    fun List<Int>.combinationsOf4(): Sequence<List<Int>> = sequence {
        val pool = this@combinationsOf4.toList()
        val n = pool.size
        val elementsToCombine = 4
        val indices = IntArray(elementsToCombine) { it }

        while (true) {
            yield(indices.map { pool[it] })
            var i = elementsToCombine - 1
            while (i >= 0 && indices[i] == i + n - elementsToCombine) {
                i--
            }
            if (i < 0) {
                break
            }
            indices[i]++
            for (j in i + 1 until elementsToCombine) {
                indices[j] = indices[j - 1] + 1
            }
        }
    }
}