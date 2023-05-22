package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.service.BlogPetService;
import com.example.api_petopia.service.CatePetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/admin/catepet")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CatePetApi {

    @Autowired
    CatePetService catePetService;

        @RequestMapping( method = RequestMethod.GET)
    public List<CatePet> findAll() {
        return catePetService.findAll();
    }
}
