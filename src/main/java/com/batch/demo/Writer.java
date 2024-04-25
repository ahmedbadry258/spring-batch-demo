package com.batch.demo;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.batch.demo.model.Employee;


public class Writer implements ItemWriter<Employee> {

	@Override
	public void write(List<? extends Employee> nResult) throws Exception {
		for (Employee empId : nResult) {
			System.out.println(" Writing the data " + empId.getFirstName()+System.lineSeparator());
		}
	}

}