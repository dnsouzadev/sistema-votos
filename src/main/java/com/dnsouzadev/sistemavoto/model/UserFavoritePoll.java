package com.dnsouzadev.sistemavoto.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_favorite_poll")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoritePoll {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    private LocalDateTime favoritedAt;

    @PrePersist
    public void prePersist() {
        favoritedAt = LocalDateTime.now();
    }
}