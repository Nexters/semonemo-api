package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import semonemo.model.entity.Invitation

interface InvitationRepository : ReactiveCrudRepository<Invitation, Long>
