package com.hoaxify.hoaxify.like;

import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.shared.GenericResponse;
import com.hoaxify.hoaxify.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/1.0")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/hoaxes/{id:[1-9]+}/likes")
    public Set<Like> getLikesByHoaxId(@PathVariable int id) {
        return this.likeService.findByHoaxId(id);
    }

    @PostMapping("/hoaxes/{id:[1-9]+}/likes")
    public GenericResponse handleLike(@PathVariable int id, @CurrentUser User user) {
        this.likeService.handleLike(id, user);
        return new GenericResponse("Like handled!");
    }
}
