package com.hoaxify.hoaxify.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByHoaxIdAndUsername(int id, String username);

    Set<Like> getByHoaxId(int id);

}
