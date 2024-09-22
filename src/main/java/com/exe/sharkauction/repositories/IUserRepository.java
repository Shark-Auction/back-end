package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u FROM UserEntity u WHERE u.user_name = :userName")
    Optional<UserEntity> findByUserName(@Param("userName") String userName);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserEntity u WHERE u.user_name = :userName")
    boolean existsByUserName(@Param("userName") String userName);
    boolean existsByEmail(String email);

    Optional<UserEntity> findById(Long id);

}
