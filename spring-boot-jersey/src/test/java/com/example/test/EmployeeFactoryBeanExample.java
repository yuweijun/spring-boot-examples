package com.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

public class EmployeeFactoryBeanExample {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeFactoryBeanExample.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(EmployeeDTOConfiguration.class);

		EmployeeDTO manager = (EmployeeDTO) context.getBean("manager");
		LOGGER.info("{}", manager);

		EmployeeDTO director = (EmployeeDTO) context.getBean("director");
		LOGGER.info("{}", director);
	}
}

/**
 * 
 * As you can see that EmployeeFactoryBean created two different employee objects using same factory method.
 *
 */
@Configuration
class EmployeeDTOConfiguration {

	@Bean(name = "manager")
	public EmployeeFactoryBean manager() {
		EmployeeFactoryBean manager = new EmployeeFactoryBean();
		manager.setDesignation("manager");
		return manager;
	}

	@Bean(name = "director")
	public EmployeeFactoryBean director() {
		EmployeeFactoryBean director = new EmployeeFactoryBean();
		director.setDesignation("director");
		return director;
	}

}

class EmployeeFactoryBean extends AbstractFactoryBean<EmployeeDTO> {
	private String designation;

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	// This method will be called by container to create new instances
	@Override
	protected EmployeeDTO createInstance() throws Exception {
		EmployeeDTO employee = new EmployeeDTO();
		employee.setId(0);
		employee.setFirstName("dummy");
		employee.setLastName("dummy");
		employee.setDesignation(designation);
		return employee;
	}

	// This method is required for autowiring to work correctly
	@Override
	public Class<EmployeeDTO> getObjectType() {
		return EmployeeDTO.class;
	}

}

class EmployeeDTO {

	private Integer id;
	private String firstName;
	private String lastName;
	private String designation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", type=" + designation
				+ "]";
	}
}