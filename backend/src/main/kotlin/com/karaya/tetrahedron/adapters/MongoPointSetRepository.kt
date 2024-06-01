package com.karaya.tetrahedron.adapters

import com.karaya.tetrahedron.domain.PointSet
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoPointSetRepository: MongoRepository<PointSet, String>