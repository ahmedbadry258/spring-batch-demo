package com.batch.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.demo.model.Employee;
import com.batch.demo.model.Person;
@Repository
public interface PersonRepository extends JpaRepository<Person, String>{ 

}
