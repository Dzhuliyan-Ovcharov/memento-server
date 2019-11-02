package com.memento.repository;

import com.memento.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByUser_Email(String email);

    Optional<EmailVerificationToken> findByToken(String token);

    /**
     * Delete all expired email verification tokens for the current day
     */
    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE u, e FROM email_verification_tokens AS e " +
                    "JOIN users u  ON u.id = e.user_id " +
                    "WHERE e.is_email_verified = 0 AND CURRENT_TIMESTAMP  >= e.expiry_time")
    void deleteAllExpiredEVTForCurrentDay();
}
