package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *  In SpringBootTest environment, the test program may use Spring repositories to 
 *  setup the database for the test and to verify the result.
 */

@SpringBootTest
public class EndToEndTestNewAssignment {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/jasminemoua/Desktop/chromedriver";

	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_COURSE_TITLE = "Test Course";
	public static final String TEST_STUDENT_NAME = "Test";
	public static final int TEST_COURSE_ID = 40443;

	public static final String TEST_DUE_DATE = "2022-12-26";

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void newAssignmentTest() throws Exception {
		// deletes any existing assignments in the database (this makes the test repeatable) 
		Assignment x = null;
		do {			
//			List<WebElement> elements  = driver.findElements(By.xpath("//div[@data-field='assignmentName']/div"));
//			boolean found = false;
//			for (WebElement we : elements) {
//				System.out.println(we.getText()); // for debug
//				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
//					found=true;
//					we.findElement(By.xpath("descendant::input")).click();
//					break;
//				}
//			}
//			assertTrue( found, "Unable to locate TEST ASSIGNMENT in list of assignments to be graded.");
			
			// find assignments in repository
            for (Assignment a : assignmentRepository.findAll()) {
            	System.out.println(a.getName()); // for debug
            	// check if assignment exists
            	if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
        			x = a;
        			break;
        		}
            }
            		
            // if exists, delete to make test repeatable
            if (x != null)
                assignmentRepository.delete(x);
            
        } while (x != null);
		
		// browser    property name                 Java Driver Class
        // edge       webdriver.edge.driver         EdgeDriver
        // FireFox    webdriver.firefox.driver      FirefoxDriver
        // IE         webdriver.ie.driver           InternetExplorerDriver
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        // Puts an Implicit wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		try {
			// load the page for the url (front end app)
	        driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// locate and click add assignment button
//			driver.findElement(By.xpath("//button[@id='AddAssignment']")).click();
//			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("AddAssignment")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// user inputs name, due date, and course id of assignment
			driver.findElement(By.id("assignmentName")).sendKeys(TEST_ASSIGNMENT_NAME);
			driver.findElement(By.id("dueDate")).sendKeys(TEST_DUE_DATE);
			driver.findElement(By.id("courseId")).sendKeys(Integer.toString(TEST_COURSE_ID));
			
			// locate and click submit button
			driver.findElement(By.xpath("//button[@id='Submit']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// verify toast
			String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			Thread.sleep(SLEEP_DURATION);
			
			assertEquals(toast_text, "Assignment successfully added");
			
			// verify assignment is in database
			boolean found = false;
			for (Assignment a : assignmentRepository.findAll()) {
				System.out.println(a.getName()); // for debug
				// check if assignment exists
				if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
			        found = true;
			        break;
			    }
			}
						
			assertTrue(found, "Assignment was not added.");
			
		} catch (Exception ex) {
			
			throw ex;
			
		} finally { // deletes any existing assignments in the database (this makes the test repeatable) 
			
			Assignment assignment = null;
						
			// find assignments in repository
			for (Assignment a : assignmentRepository.findAll()) {
				System.out.println(a.getName()); // for debug
				// check if assignment exists
            	if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
            		assignment = a;
        			break;
        		}
            }
            		
			// if exists, delete to make test repeatable
            if (assignment != null)
                assignmentRepository.delete(assignment);
            
            driver.quit();
		}
	}
}
