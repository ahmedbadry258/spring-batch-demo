package com.batch.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.demo.model.Employee;
import com.batch.demo.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{

}
