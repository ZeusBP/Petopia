package com.example.api_petopia.service;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.BlogPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class BlogPetService {
    @Autowired BlogPetRepository blogPetRepository;

    public Iterable<BlogPet> findAll() {
        return blogPetRepository.findAll();
    }
    public Page<BlogPet> findAllByActive( int page, int limit) {

        return blogPetRepository.findAllByStatusEquals(BlogPetStatus.ACTIVE, PageRequest.of(page, limit));
    }

    public Page<BlogPet> findAllByDeactive( int page, int limit) {
        return blogPetRepository.findAllByStatusEquals(BlogPetStatus.DEACTIVE,PageRequest.of(page, limit));
    }

    public Optional<BlogPet> findById(Long id) {
        return blogPetRepository.findById(id);
    }

    public List<BlogPet> findByUserId(Long userId){
        return blogPetRepository.findAllByUserId(userId);
    }

    public BlogPet save(BlogPet blogPet) {
        return blogPetRepository.save(blogPet);
    }


    public BlogPet createPost(BlogPet blogPet) {
        return blogPetRepository.save(blogPet);
    }

    public BlogPet getPostById(Long id) {
        BlogPet blogPet = blogPetRepository.findById(id).get();
        return blogPet;
    }

    public List<BlogPet> searchAll(Specification<BlogPet> specification){
        return blogPetRepository.findAll(specification);
    }

    public Page<BlogPet> searchAllForAdmin(Specification<BlogPet> specification,int page, int limit){
        return blogPetRepository.findAll(specification,PageRequest.of(page, limit,Sort.by("updatedAt").descending()));
    }

    public int totalBlog(){
        return blogPetRepository.findAll().size();
    }

    public int totalBlogByStatus(int status){
        BlogPetStatus status1 = BlogPetStatus.ACTIVE;
        if (status == 0){
            status1 = BlogPetStatus.DEACTIVE;
        }if (status == 2){
            status1 = BlogPetStatus.DELETED;
        }
        return blogPetRepository.findAllByStatus(status1).size();
    }

}
