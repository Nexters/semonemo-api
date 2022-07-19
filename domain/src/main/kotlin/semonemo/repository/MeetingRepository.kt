package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import semonemo.model.entity.Meeting

interface MeetingRepository: ReactiveCrudRepository<Meeting, String>
