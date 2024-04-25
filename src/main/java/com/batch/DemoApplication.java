package com.batch;


import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.batch.demo.repositories.EmployeeRepository;
import com.batch.demo.repositories.RegionRepository;




@SpringBootApplication
@EnableScheduling
public class DemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);		
	}
	
	 @Bean
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }

	 @Autowired
	 private RegionRepository regionRepository;
	 @Autowired
	 private EmployeeRepository emRepo;
	@Override
	public void run(String... args) throws Exception {
		System.out.println("start");
//		Region r= new Region();
//		r.setRegionId(10l);
//		r.setRegionName("Test");
//		regionRepository.save(r);
		
		// TODO Auto-generated method stub
//		Employees  e =new Employees();
//		 e.setId(101);
//		e.setFirstName("Ahmed ");
//		e.setLastName("Ali ");
//		e.setStartDate(new Date());
//		e.setEndDate(new Date());
//		repositery.save(e);
//		for(int i=0 ;i<20 ;i++) {
//			
//			AtomicInteger ai= 	new AtomicInteger(20);
//			System.out.println(ai.get());
//		 e =new Employees();
//		 e.setId(ai.get());
//		e.setFirstName("Ahmed "+i);
//		e.setLastName("Ali "+i);
//		e.setStartDate(new Date());
//		e.setEndDate(new Date());
//		repositery.save(e);
//		}
//		Employee emp=new Employee();
//		emp.setFirstName("x2");
//		emp.setLastName("x2");
//		emp.setEmail("x2@email.com");
//		emp.setHireDate(LocalDate.now());
//		emp.setJobId(100l);
//		emp.setSalary("2000");
//		emp.setEmployeeId(403l);
//		emp.setStartDate(new Date());
//		emp.setEndDate(new Date());
//		emRepo.save(emp);
		System.out.println("********saved************");
	}
	

}
