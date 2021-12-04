package com.hoaxify.hoaxify.hoax;

import com.hoaxify.hoaxify.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HoaxSecurityService {

    private HoaxRepository hoaxRepository;

    public HoaxSecurityService(HoaxRepository hoaxRepository) {
        this.hoaxRepository = hoaxRepository;
    }

    public boolean isAllowedToManipulate(int hoaxId, User loggedInUser) {
        Optional<Hoax> optionalHoax = this.hoaxRepository.findById(hoaxId);
        if (optionalHoax.isPresent()) {
            Hoax inDB = optionalHoax.get();
            return inDB.getUser().getId() == loggedInUser.getId();
        }
        return false;
    }

}
