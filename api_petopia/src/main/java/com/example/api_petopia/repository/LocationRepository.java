package com.example.api_petopia.repository;

import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location , Long>, JpaSpecificationExecutor<Location>{
    Page<Location> findAllByStatusEquals(LocationStatus status, Pageable pageable);

    List<Location> findAllByStatus(LocationStatus status);

//    List<Location> findAllListFilter(Specification<Location> specification, LocationStatus active);
}
