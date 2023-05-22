package com.example.api_petopia.api.User;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.repository.BlogPetRepository;
import com.example.api_petopia.service.BlogPetService;
import com.example.api_petopia.service.ProfileService;
import com.example.api_petopia.spec.User.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BlogPetApi {

    @Autowired
    BlogPetService blogPetService;

    @Autowired
    BlogPetRepository blogPetRepository;

    @Autowired
    ProfileService profileService;

    @RequestMapping(value = "/blogs/filter", method = RequestMethod.POST)
    public List<BlogPet> searchBlog(
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "age", required = false) String age,
                                    @RequestParam(value = "breed", required = false) String breed,
                                    @RequestParam(value = "sex", required = false) String sex,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "catePet", required = false) CatePet catePet,
                                    @RequestParam(value = "typePet", required = false) TypePet typePet,
                                    @RequestParam(value = "user", required = false) User user,
                                    @RequestParam(value = "status", required = false) BlogPetStatus status){
        Specification<BlogPet> specification = Specifications.blogSpec(name, age, breed, sex, description, address, catePet, typePet,user,status);
        return blogPetService.searchAll(specification);
    }

    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public Page<BlogPet> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return blogPetService.findAllByActive(page, limit);
    }


    @RequestMapping(value = "/blogs/create", method = RequestMethod.POST)
    public ResponseEntity<BlogPet> createPost(@RequestBody BlogPet blogPet, Principal principal) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = profileService.findByUsername(principal.getName());
        blogPet.setUser(user);
        blogPet.setStatus(BlogPetStatus.DEACTIVE);
        blogPet.setCreatedBy(user.getUsername());
        blogPet.setCreatedAt(LocalDateTime.now());
        blogPet.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(blogPetService.createPost(blogPet));
    }


    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.GET)
    public ResponseEntity<BlogPet> getPostByID(@PathVariable Long id) {
        return ResponseEntity.ok(blogPetService.getPostById(id));
    }
//
//    @RequestMapping(value = "/blogs/myblog", method = RequestMethod.GET)
//    public Page<BlogPet> myBlog(@RequestParam(name = "page", defaultValue = "0") int page,
//                                 @RequestParam(name = "limit", defaultValue = "10") int limit,
//                                Principal principal) {
//        User user = profileService.findByUsername(principal.getName());
//        Optional<BlogPet> blogPetOptional = blogPetService.findById(user.getId());
//        if (!blogPetOptional.isPresent() && !user.getId().equals(blogPetOptional)) {
//            ResponseEntity.badRequest().build();
//        }
//        return blogPetService.findAllByActive(page, limit);
//    }

    @RequestMapping(value = "/blogs/myblog", method = RequestMethod.GET)
    public ResponseEntity<Iterable<BlogPet>> myBlog(Principal principal) {
        User user = profileService.findByUsername(principal.getName());
        List<BlogPet> blogPetOptional = blogPetService.findByUserId(user.getId());
//        List<BlogPet> blogPetOptional1 = blogPetService.findByCatePetId(id);
        if (!blogPetOptional.isEmpty()){
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(blogPetOptional);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/blogs/update/{id}")
    public ResponseEntity<BlogPet> update(@PathVariable Long id, @RequestBody BlogPet updateBlog, Principal principal) {
        Optional<BlogPet> blogPetOptional = blogPetService.findById(id);
        User user = profileService.findByUsername(principal.getName());
        if (!blogPetOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        BlogPet existBlog = blogPetOptional.get();

        existBlog.setName(updateBlog.getName());
        existBlog.setUpdatedAt(LocalDateTime.now());
        existBlog.setThumbnail(updateBlog.getThumbnail());
        existBlog.setBreed(updateBlog.getBreed());
        existBlog.setImage(updateBlog.getImage());
        existBlog.setDescription(updateBlog.getDescription());
        existBlog.setAge(updateBlog.getAge());
        existBlog.setSex(updateBlog.getSex());
        existBlog.setCatePet(updateBlog.getCatePet());
        existBlog.setTypePet(updateBlog.getTypePet());
        existBlog.setUser(user);
        existBlog.setUpdatedAt(LocalDateTime.now());
        existBlog.setStatus(BlogPetStatus.DEACTIVE);

        return ResponseEntity.ok(blogPetService.save(existBlog));
    }
//    @RequestMapping(method = RequestMethod.DELETE,value = "/blogs/delete/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable Long id ){
//        if (!blogPetService.findById(id).isPresent()){
//            ResponseEntity.badRequest().build();
//        }
//        blogPetService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }

    @RequestMapping(method = RequestMethod.PUT, value = "/blogs/delete/{id}")
    public ResponseEntity<BlogPet> deleteBlog(@PathVariable Long id) {
        Optional<BlogPet> blogPetOptional = blogPetService.findById(id);

        if (!blogPetOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        BlogPet existBlog = blogPetOptional.get();

        existBlog.setStatus(BlogPetStatus.DELETED);
        existBlog.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(blogPetService.save(existBlog));
    }

}
