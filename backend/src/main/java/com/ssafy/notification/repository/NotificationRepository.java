package com.ssafy.notification.repository;

import com.ssafy.member.domain.Member;
import com.ssafy.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findAllByReceiver(Member member);
}
