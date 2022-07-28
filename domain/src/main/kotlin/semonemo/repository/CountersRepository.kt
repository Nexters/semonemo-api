package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import semonemo.model.entity.Counters

interface CountersRepository : ReactiveCrudRepository<Counters, String>
