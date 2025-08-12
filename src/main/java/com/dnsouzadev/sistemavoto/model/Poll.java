package com.dnsouzadev.sistemavoto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean publicResults = true; // se true, qualquer um pode ver

    @Column(nullable = false)
    private boolean featured = false; // se true, aparece em destaque

    @Column(nullable = false)
    private boolean active = true; // se false, foi encerrada manualmente

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options;

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDateTime.now());
    }
    
    public boolean isActiveAndNotExpired() {
        return active && !isExpired();
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (!publicResults) publicResults = true;
    }
}
