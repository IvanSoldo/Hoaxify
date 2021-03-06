package com.hoaxify.hoaxify.user;

import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.shared.GenericResponse;
import com.hoaxify.hoaxify.user.vm.UserUpdateVM;
import com.hoaxify.hoaxify.user.vm.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public GenericResponse createUser(@Valid @RequestBody User user) {
        this.userService.save(user);
        return new GenericResponse("User saved");
    }

    @GetMapping("/users")
    public Page<UserVM> getUsers(@CurrentUser User loggedInUser, Pageable pageable) {
        return userService.getUsers(loggedInUser, pageable).map(UserVM::new);
    }

    @GetMapping("/users/{username}")
    public UserVM getUserByName(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return new UserVM(user);
    }

    @PutMapping("/users/{id:[0-9]+}")
    @PreAuthorize("#id == principal.id")
    public UserVM updateUser(@PathVariable int id, @Valid @RequestBody(required = false) UserUpdateVM userUpdate) {
        User updated = userService.update(id, userUpdate);
        return new UserVM(updated);
    }
}
