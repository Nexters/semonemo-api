package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import semonemo.model.entity.Stamp

interface StampRepository : ReactiveCrudRepository<Stamp, Long>
