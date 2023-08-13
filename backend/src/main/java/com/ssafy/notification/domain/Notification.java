package com.ssafy.notification.domain;

import com.ssafy.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Embedded
    @NotNull
    private NotificationContent content;

    @Embedded
    @NotNull
    private RelatedUrl url;

    @NotNull
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    @Builder
    public Notification(Member receiver, NotificationType notificationType, String content, String url, Boolean isRead) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = new NotificationContent(content);
        this.url = new RelatedUrl(url);
        this.isRead = isRead;
    }

    public String getContent() {
        return content.getContent();
    }

    public String getUrl() {
        return url.getUrl();
    }

//    public void read(){
//        isRead = true;
//    }
}
