package com.example.api_petopia.api.Admin;


import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.TypeLocation;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.service.LocationService;
import com.example.api_petopia.spec.Admin.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LocationsApi {
    @Autowired
    LocationService locationService;

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public Page<Location> SearchLocation(@RequestParam(value = "nameLocation", required = false) String nameLocation,
                                         @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "phone", required = false) String phone,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "description", required = false) String description,
                                         @RequestParam(value = "typeLocation", required = false) TypeLocation typeLocation,
                                         @RequestParam(value = "user", required = false) User user,
                                         @RequestParam(value = "status", required = false) LocationStatus status,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Specification<Location> specification = Specifications.locationSpec(nameLocation, address, phone, email, description, typeLocation,user,status,page,limit);
        return locationService.searchAllForAdmin(specification,page,limit);
    }

    @RequestMapping(value = "/location/active", method = RequestMethod.GET)
    public Page<Location> findAllActive(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return locationService.findAllByActive(page, limit);
    }

    @RequestMapping(value = "/location/deactive", method = RequestMethod.GET)
    public Page<Location> findAllDeactive(@RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return locationService.findAllByDeactive(page, limit);
    }


    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationByID(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/location/delete/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id) {
        Optional<Location> optionalLocation = locationService.findById(id);
        if (!optionalLocation.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Location existLocation = optionalLocation.get();
        existLocation.setStatus(LocationStatus.DELETED);
        return ResponseEntity.ok(locationService.save(existLocation));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/location/change/{id}")
    public ResponseEntity<Location> change(@PathVariable Long id) {
        Optional<Location> optionalLocation = locationService.findById(id);
        if (!optionalLocation.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Location existLocation = optionalLocation.get();
        existLocation.setUpdatedAt(LocalDateTime.now());
        existLocation.setStatus(LocationStatus.ACTIVE);
        return ResponseEntity.ok(locationService.save(existLocation));
    }
    @RequestMapping(method = RequestMethod.GET, path = "/location/total")
    public int totalUser(){
        return locationService.totalLocation();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/location/totalByStatus/{status}")
    public int totalUserByStatus(@PathVariable int status){
        return locationService.totalLocationByStatus(status);
    }
}
