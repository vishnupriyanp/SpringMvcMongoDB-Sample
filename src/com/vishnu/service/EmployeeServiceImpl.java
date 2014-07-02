package com.vishnu.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.vishnu.model.Employee;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired(required=true)
	private MongoTemplate mongoTemplate;
//	
//	@Autowired
//	private MongoOperations mongoOperations;
	
	protected static Logger logger = Logger.getLogger("service");
	  
	 /**
	  * Retrieves all Employees
	  */
	 public List<Employee> listEmployeess() {
		 logger.debug("Retrieving all Employees");
		 List<Employee> employees=null;
		 try{
			 Query query = new Query(where("empId").exists(true));

			employees = mongoTemplate.findAll(Employee.class);
			 
		 }catch(Exception e){						
		 }
	 
		 return employees;
	 }
	  
	 /**
	  * Retrieves a single Employee
	  */
	 public Employee getEmployee(String empid ) {
		 logger.debug("Retrieving an existing Employee");
		 Employee employee = new Employee();
		 // Find an entry where empid matches the id
		 DBObject query = new BasicDBObject();
		 query.put("empId", empid);
		 DBObject cursor = mongoTemplate.getDb().getCollection("employee").findOne(query);
		 employee.setEmpId(cursor.get("empId").toString());
		 employee.setEmpName(cursor.get("empName").toString());
		 employee.setEmpAge(cursor.get("empAge").toString());
	     employee.setSalary(cursor.get("salary").toString());
		 employee.setEmpAddress(cursor.get("empAddress").toString());
	      
		 return employee;
	 }
	  
	 /**
	  * Adds a new Employee
	  */
	 public Boolean addEmployee(Employee employee) {
		 logger.debug("Adding a new employee");
	   
		 try {
			 if(employee.getEmpId() != null && employee.getEmpId() != ""){
				 // Find an entry where empid matches the id
				 DBObject query1 = new BasicDBObject();
	        	 query1.put("empId", employee.getEmpId());
	        	 DBObject query = new BasicDBObject();
		   		 query.put("empId", employee.getEmpId());
		   		 query.put("empName", employee.getEmpName());
		   		 query.put("empAge", employee.getEmpAge());
		   		 query.put("salary", employee.getSalary());
		   		 query.put("empAddress", employee.getEmpAddress());
	   		     mongoTemplate.getDb().getCollection("employee").update(query1, query);
	        }else{
	        	 // Set a new value to the empid property first since it's blank
				 employee.setEmpId(UUID.randomUUID().toString());
				 // Insert to db
				 mongoTemplate.save(employee);
	        }
			
	 	   return true;
		 } 
		 catch (Exception e) {
			 logger.error("An error has occurred while trying to add new employee", e);
			 return false;
		 }
	 }
	  
	 /**
	  * Deletes an existing employee
	  */
	 public Boolean deleteEmployee(String empid) {
		 logger.debug("Deleting existing employee");
	   
		 try {
			// Find an entry where pid matches the id
	         Query query = new Query(where("empId").is(empid));
	         // Run the query and delete the entry
	         mongoTemplate.remove(query); 
	         return true;
		 } catch (Exception e) {
			 logger.error("An error has occurred while trying to delete new employee", e);
			 return false;
		 }
	 }
	  
	 /**
	  * Edits an existing employee
	  */
	 public Boolean edit(Employee employee) {
		 logger.debug("Editing existing employee");
	   
		 try {
	    
			 // Find an entry where empid matches the id
	         Query query = new Query(where("empId").is(employee.getEmpId()));
	          
	         // Declare an Update object. 
	         // This matches the update modifiers available in MongoDB
	         Update update = new Update();
	          
	         update.set("Employee Name", employee.getEmpName());
	        mongoTemplate.updateMulti(query, update, "collectionNam");
	          
	         update.set("empAddress", employee.getEmpAddress());
	         //mongoTemplate.updateMulti(query, update, "collectionNam");
	          
	         update.set("salary", employee.getSalary());
	         //mongoTemplate.updateMulti(query, update, "collectionNam");
	          
	         update.set("Employee Age", employee.getEmpAge());
	         //mongoTemplate.updateMulti(query, update, "collectionNam");
	         
	         return true;
	    
		 } catch (Exception e) {
			 logger.error("An error has occurred while trying to edit existing employee", e);
			 return false;
		 }
	   
	 }
}
