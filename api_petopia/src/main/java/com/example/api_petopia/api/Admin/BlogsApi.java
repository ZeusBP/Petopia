package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.service.BlogPetService;
import com.example.api_petopia.service.ProfileService;
import com.example.api_petopia.spec.Admin.Specifications;
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
@RequestMapping(path = "api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BlogsApi {
    @Autowired
    BlogPetService blogPetService;
    @Autowired
    ProfileService profileService;


    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public Page<BlogPet> searchBlog(
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "age", required = false) String age,
                                    @RequestParam(value = "breed", required = false) String breed,
                                    @RequestParam(value = "sex", required = false) String sex,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "catePet", required = false) CatePet catePet,
                                    @RequestParam(value = "typePet", required = false) TypePet typePet,
                                    @RequestParam(value = "user", required = false) User user,
                                    @RequestParam(value = "status", required = false) BlogPetStatus status,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "limit", defaultValue = "10") int limit
    ){
        Specification<BlogPet> specification = Specifications.blogSpec(name, age, breed, sex, description, address, catePet, typePet,user,status,page,limit);
        return blogPetService.searchAllForAdmin(specification,page,limit);
    }

//    @RequestMapping(value = "/blogs/active", method = RequestMethod.GET)
//    public Page<BlogPet> findAllActive(@RequestParam(name = "page", defaultValue = "0") int page,
//                                       @RequestParam(name = "limit", defaultValue = "10") int limit) {
//        return blogPetService.findAllByActive(page, limit);
//    }

//    @RequestMapping(value = "/blogs/deactive", method = RequestMethod.GET)
//    public Page<BlogPet> findAllDeactive(@RequestParam(name = "page", defaultValue = "0") int page,
//                                         @RequestParam(name = "limit", defaultValue = "10") int limit) {
//        return blogPetService.findAllByDeactive(page, limit);
//    }




//    @RequestMapping(value = "/blogs/create", method = RequestMethod.POST)
//    public void createPost(@RequestBody BlogPet blogPet, Principal principal) {
//        User user = profileService.findByUsername(principal.getName());
//        blogPet.setUser(user);
//        blogPet.setStatus(BlogPetStatus.DEACTIVE);
//        blogPet.setCreatedBy(user.getUsername());
//        blogPet.setCreatedAt(LocalDateTime.now());
//        blogPetService.createPost(blogPet);
//    }


    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.GET)
    public ResponseEntity<BlogPet> getPostByID(@PathVariable Long id) {
        return ResponseEntity.ok(blogPetService.getPostById(id));
    }

    //    @RequestMapping(method = RequestMethod.DELETE,value = "/blogs/delete/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable Long id ){
//        if (!blogPetService.findById(id).isPresent()){
//            ResponseEntity.badRequest().build();
//        }
//        blogPetService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
    @RequestMapping(method = RequestMethod.PUT, path = "/blogs/delete/{id}")
    public ResponseEntity<BlogPet> update(@PathVariable Long id) {
        Optional<BlogPet> optionalBlogPet = blogPetService.findById(id);
        if (!optionalBlogPet.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        BlogPet existBlogPet = optionalBlogPet.get();
        existBlogPet.setStatus(BlogPetStatus.DELETED);
        return ResponseEntity.ok(blogPetService.save(existBlogPet));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/blogs/change/{id}")
    public ResponseEntity<BlogPet> change(@PathVariable Long id) {
        Optional<BlogPet> optionalBlogPet = blogPetService.findById(id);
        if (!optionalBlogPet.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        BlogPet existBlogPet = optionalBlogPet.get();
        existBlogPet.setStatus(BlogPetStatus.ACTIVE);
        existBlogPet.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(blogPetService.save(existBlogPet));
    }
    @RequestMapping(method = RequestMethod.GET, path = "/blogs/total")
    public int totalBlog(){
        return blogPetService.totalBlog();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/blogs/totalByStatus/{status}")
    public int totalBlogByStatus(@PathVariable int status){
        return blogPetService.totalBlogByStatus(status);
    }
}
