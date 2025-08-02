package com.dnsouzadev.sistemavoto.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "poll")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poll {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    private boolean anonymous;

    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean publicResults = true; // se true, qualquer um pode ver

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (!publicResults) publicResults = true;
    }
}
