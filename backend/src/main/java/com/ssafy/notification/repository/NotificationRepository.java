package com.ssafy.notification.repository;

import com.ssafy.member.domain.Member;
import com.ssafy.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long id);

    Optional<List<Notification>> findAllByReceiver_Id(UUID memberId);
}
