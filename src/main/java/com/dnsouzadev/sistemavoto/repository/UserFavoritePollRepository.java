package com.dnsouzadev.sistemavoto.repository;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.UserFavoritePoll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFavoritePollRepository extends JpaRepository<UserFavoritePoll, UUID> {
    
    Optional<UserFavoritePoll> findByUserAndPoll(User user, Poll poll);
    
    @Query("SELECT ufp.poll FROM UserFavoritePoll ufp WHERE ufp.user = :user ORDER BY ufp.favoritedAt DESC")
    Page<Poll> findFavoritesPollsByUser(User user, Pageable pageable);
    
    boolean existsByUserAndPoll(User user, Poll poll);
    
    void deleteByUserAndPoll(User user, Poll poll);
    
    long countByUser(User user);
}