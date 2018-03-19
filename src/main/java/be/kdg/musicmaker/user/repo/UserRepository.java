package be.kdg.musicmaker.user.repo;

import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u where u.email = ?1")
    void delete(String email);
}
