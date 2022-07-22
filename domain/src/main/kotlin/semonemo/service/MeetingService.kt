package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import semonemo.model.entity.Meeting
import semonemo.repository.MeetingRepository

@Service
class MeetingService(
    private val meetingRepository: MeetingRepository,
) {

    // TODO: 정렬 기능 추가
    @Transactional(readOnly = true)
    fun findMeetings(): Flux<Meeting> = meetingRepository.findAll();
}
