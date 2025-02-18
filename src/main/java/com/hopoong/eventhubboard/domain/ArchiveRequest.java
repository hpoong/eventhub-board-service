package com.hopoong.eventhubboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "archive_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchiveRequest { // 아카이빙 요청

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime archivedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ArchiveStatus status = ArchiveStatus.PENDING;
}

enum ArchiveStatus {
    PENDING, COMPLETED
}
