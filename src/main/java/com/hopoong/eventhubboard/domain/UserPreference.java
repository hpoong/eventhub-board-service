package com.hopoong.eventhubboard.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference { // 사용자 설정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false)
    private Boolean notificationEnabled = true;

    @Column(nullable = false, length = 50)
    private String theme = "light";
}
