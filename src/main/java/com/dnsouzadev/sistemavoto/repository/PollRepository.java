package com.dnsouzadev.sistemavoto.repository;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
    List<Poll> findByCreatedBy(User user);
    
    @Query("SELECT p FROM Poll p WHERE p.publicResults = true AND p.active = true AND p.expirationDate > :now ORDER BY p.featured DESC, p.createdAt DESC")
    Page<Poll> findPublicActivePolls(LocalDateTime now, Pageable pageable);
    
    @Query("SELECT p FROM Poll p WHERE p.publicResults = true ORDER BY p.featured DESC, p.createdAt DESC")
    Page<Poll> findAllPublicPolls(Pageable pageable);
    
    @Query("SELECT p FROM Poll p WHERE p.createdBy = :user AND p.active = true AND p.expirationDate > :now ORDER BY p.featured DESC, p.createdAt DESC")
    Page<Poll> findActiveByCreatedBy(User user, LocalDateTime now, Pageable pageable);
    
    @Query("SELECT p FROM Poll p WHERE p.createdBy = :user AND (p.active = false OR p.expirationDate <= :now) ORDER BY p.createdAt DESC")
    Page<Poll> findClosedByCreatedBy(User user, LocalDateTime now, Pageable pageable);
    
    Page<Poll> findByCreatedByOrderByCreatedAtDesc(User user, Pageable pageable);
}
