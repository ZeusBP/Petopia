package com.example.api_petopia.api.User;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.repository.LocationRepository;
import com.example.api_petopia.service.LocationService;
import com.example.api_petopia.service.ProfileService;
import com.example.api_petopia.spec.User.Specifications;
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
@CrossOrigin("*")
public class LocationApi {
    @Autowired
    LocationService locationService;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ProfileService profileService;

    @RequestMapping(value = "/location/filter", method = RequestMethod.POST)
    public List<Location> SearchLocation(@RequestParam(value = "nameLocation", required = false) String nameLocation,
                                         @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "phone", required = false) String phone,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "description", required = false) String description,
                                         @RequestParam(value = "typeLocation", required = false) TypeLocation typeLocation,
                                         @RequestParam(value = "user", required = false) User user,
                                         @RequestParam(value = "status", required = false) LocationStatus status
    ) {
        Specification<Location> specification = Specifications.locationSpec(nameLocation, address, phone, email, description, typeLocation,user,status);
        return locationService.searchAll(specification);
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public Page<Location> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return locationService.findAllByActive(page, limit);
    }

    @RequestMapping(value = "/location/create", method = RequestMethod.POST)
    public ResponseEntity<Location> createLocation(@RequestBody Location location, Principal principal) {
        User user = profileService.findByUsername(principal.getName());
        location.setUser(user);
        location.setStatus(LocationStatus.DEACTIVE);
        location.setCreatedBy(user.getUsername());
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());
        return  ResponseEntity.ok(locationService.save(location));
    }
//    @RequestMapping(value = "/blogs",method = RequestMethod.GET)
//    public ResponseEntity<Iterable<BlogPet>> getAllPosts(){
//        return ResponseEntity.ok(blogPetService.listPosts());
//    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationByID(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/location/update/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody Location locationUpdate, Principal principal) {
        Optional<Location> locationOptional = locationService.findById(id);
        User user = profileService.findByUsername(principal.getName());
        if (!locationOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Location existLocation = locationOptional.get();

        existLocation.setNameLocation(locationUpdate.getNameLocation());
        existLocation.setAddress(locationUpdate.getAddress());
        existLocation.setPhone(locationUpdate.getPhone());
        existLocation.setEmail(locationUpdate.getEmail());
        existLocation.setDescription(locationUpdate.getDescription());
        existLocation.setTypeLocation(locationUpdate.getTypeLocation());
        existLocation.setUser(user);
        existLocation.setUpdatedAt(LocalDateTime.now());
        existLocation.setStatus(LocationStatus.DEACTIVE);


        return ResponseEntity.ok(locationService.save(existLocation));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/location/delete/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable Long id) {
        Optional<Location> locationOptional = locationService.findById(id);
        if (!locationOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Location existLocation = locationOptional.get();

        existLocation.setStatus(LocationStatus.DELETED);
        existLocation.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(locationService.save(existLocation));
    }
}
