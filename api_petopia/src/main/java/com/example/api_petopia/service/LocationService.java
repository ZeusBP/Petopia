package com.example.api_petopia.service;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.Product;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {
    @Autowired LocationRepository locationRepository;


    public Page<Location> findAllByActive(int page, int limit) {
        return locationRepository.findAllByStatusEquals(LocationStatus.ACTIVE, PageRequest.of(page, limit));
    }
    public Page<Location> findAllByDeactive( int page, int limit) {
        return locationRepository.findAllByStatusEquals(LocationStatus.DEACTIVE,PageRequest.of(page, limit));
    }


    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }

    public Location getLocationById(Long id) {
        Location location = locationRepository.findById(id).get();
        return location;
    }

    //    public Page<Location> findAllLocation(int page, int limit) {
//        return locationRepository.findAll(PageRequest.of(page, limit));
//    }
    public Iterable<Location> findAll() {
        return locationRepository.findAll();
    }

    public List<Location> searchAll(Specification<Location> specification){
        return locationRepository.findAll(specification);
    }

    public Page<Location> searchAllForAdmin(Specification<Location> specification, int page, int limit){
        return locationRepository.findAll(specification,PageRequest.of(page, limit, Sort.by("updatedAt").descending()));
    }
    public int totalLocation(){
        return locationRepository.findAll().size();
    }

    public int totalLocationByStatus(int status){
        LocationStatus status1 = LocationStatus.ACTIVE;
        if (status == 0){
            status1 = LocationStatus.DEACTIVE;
        }if (status == 2){
            status1 = LocationStatus.DELETED;
        }
        return locationRepository.findAllByStatus(status1).size();
    }
}
