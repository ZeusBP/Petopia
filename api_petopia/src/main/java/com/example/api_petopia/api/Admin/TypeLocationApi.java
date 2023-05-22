package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.TypeLocation;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.service.TypeLocationService;
import com.example.api_petopia.service.TypePetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/admin/typelocation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TypeLocationApi{
    @Autowired
    TypeLocationService typeLocationService;

    @RequestMapping( method = RequestMethod.GET)
    public List<TypeLocation> findAll() {
        return typeLocationService.findAll();
    }
}
