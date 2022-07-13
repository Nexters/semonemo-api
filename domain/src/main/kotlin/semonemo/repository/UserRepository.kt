package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import semonemo.model.entity.User

interface UserRepository: ReactiveCrudRepository<User, String>
