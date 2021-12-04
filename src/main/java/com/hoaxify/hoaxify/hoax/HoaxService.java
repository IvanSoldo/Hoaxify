package com.hoaxify.hoaxify.hoax;

import java.util.Date;
import java.util.List;

import com.hoaxify.hoaxify.file.FileAttachment;
import com.hoaxify.hoaxify.file.FileAttachmentRepository;
import com.hoaxify.hoaxify.hoax.vm.HoaxUpdateVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hoaxify.hoaxify.file.FileService;
import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserService;

@Service
public class HoaxService {

    private HoaxRepository hoaxRepository;

    private UserService userService;

    private FileService fileService;

    private FileAttachmentRepository fileAttachmentRepository;


    public HoaxService(HoaxRepository hoaxRepository, UserService userService,
                       FileService fileService, FileAttachmentRepository fileAttachmentRepository
    ) {
        super();
        this.hoaxRepository = hoaxRepository;
        this.userService = userService;
        this.fileService = fileService;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    public Hoax save(User user, Hoax hoax) {
        hoax.setTimestamp(new Date());
        hoax.setUser(user);

        if(hoax.getAttachment() != null) {
            FileAttachment inDB = this.fileAttachmentRepository.findById(hoax.getAttachment().getId()).get();
            inDB.setHoax(hoax);
            hoax.setAttachment(inDB);
        }
        return hoaxRepository.save(hoax);
    }

    public Page<Hoax> getAllHoaxes(Pageable pageable) {
        return hoaxRepository.findAll(pageable);
    }

    public Page<Hoax> getHoaxesOfUser(String username, Pageable pageable) {
        User inDB = userService.getByUsername(username);
        return hoaxRepository.findByUser(inDB, pageable);
    }

    public Page<Hoax> getOldHoaxes(int id, String username, Pageable pageable) {
        Specification<Hoax> spec = Specification.where(idLessThan(id));
        if(username != null) {
            User inDB = userService.getByUsername(username);
            spec = spec.and(userIs(inDB));
        }
        return hoaxRepository.findAll(spec, pageable);
    }


    public List<Hoax> getNewHoaxes(int id, String username, Pageable pageable) {
        Specification<Hoax> spec = Specification.where(idGreaterThan(id));
        if(username != null) {
            User inDB = userService.getByUsername(username);
            spec = spec.and(userIs(inDB));
        }
        return hoaxRepository.findAll(spec, pageable.getSort());
    }

    public int getNewHoaxesCount(int id, String username) {
        Specification<Hoax> spec = Specification.where(idGreaterThan(id));
        if(username != null) {
            User inDB = userService.getByUsername(username);
            spec = spec.and(userIs(inDB));
        }
        return (int) hoaxRepository.count(spec);
    }

    private Specification<Hoax> userIs(User user){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user"), user);
        };
    }

    private Specification<Hoax> idLessThan(int id){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("id"), id);
        };
    }

    private Specification<Hoax> idGreaterThan(int id){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("id"), id);
        };
    }

    public void deleteHoax(int id) {
        Hoax hoax = this.hoaxRepository.getById(id);
        if (hoax.getAttachment() != null) {
            fileService.deleteAttachmentImage(hoax.getAttachment().getName());
        }
        this.hoaxRepository.deleteById(id);
    }

    public Hoax editHoax(int id, HoaxUpdateVM hoaxUpdateVM) {
        Hoax hoax = this.hoaxRepository.getById(id);
        hoax.setContent(hoaxUpdateVM.getContent());
        hoax.setEdited(true);

        return this.hoaxRepository.save(hoax);
    }
}