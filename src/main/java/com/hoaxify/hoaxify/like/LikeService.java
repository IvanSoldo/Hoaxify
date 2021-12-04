package com.hoaxify.hoaxify.like;

import com.hoaxify.hoaxify.error.NotFoundException;
import com.hoaxify.hoaxify.hoax.Hoax;
import com.hoaxify.hoaxify.hoax.HoaxRepository;
import com.hoaxify.hoaxify.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class LikeService {

    private LikeRepository likeRepository;

    private HoaxRepository hoaxRepository;

    public LikeService(LikeRepository likeRepository, HoaxRepository hoaxRepository) {
        this.likeRepository = likeRepository;
        this.hoaxRepository = hoaxRepository;
    }

    public Set<Like> findByHoaxId(int id) {
        return this.likeRepository.getByHoaxId(id);
    }

    public void handleLike(int id, User user) {
        if (!this.hoaxRepository.existsById(id)) {
            throw new NotFoundException("The hoax with the id of " + id + " does not exist!");
        }

        Hoax hoax = this.hoaxRepository.getById(id);
        Optional<Like> inDB = this.likeRepository.findByHoaxIdAndUsername(id, user.getUsername());

        if (inDB.isPresent()) {
            this.likeRepository.delete(inDB.get());
            return;
        }

        Like like = new Like();
        like.setHoax(hoax);
        like.setUsername(user.getUsername());

        this.likeRepository.save(like);
    }
}
