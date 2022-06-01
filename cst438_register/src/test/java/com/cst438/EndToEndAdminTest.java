package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndAdminTest {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000/admin";

	public static final String TEST_USER_EMAIL = "testStudent@csumb.edu";

	public static final String TEST_NAME = "Student Test";

	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudent() throws Exception {
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);
		
		// set the driver location and start driver
				//@formatter:off
				// browser	property name 				Java Driver Class
				// edge 	webdriver.edge.driver 		EdgeDriver
				// FireFox 	webdriver.firefox.driver 	FirefoxDriver
				// IE 		webdriver.ie.driver 		InternetExplorerDriver
				//@formatter:on

				System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
				WebDriver driver = new ChromeDriver();
				// Puts an Implicit wait for 10 seconds before throwing exception
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				try {
					//navigate to admin page of front end
					driver.get(URL);
					Thread.sleep(SLEEP_DURATION);
					
					//find the add student button
					driver.findElement(By.xpath("//button")).click();
					Thread.sleep(SLEEP_DURATION);
					
					// enter info into fields
					driver.findElement(By.xpath("//input[@name='studentName']")).sendKeys(TEST_NAME);
					driver.findElement(By.xpath("//input[@name='studentEmail']")).sendKeys(TEST_USER_EMAIL);
					Thread.sleep(SLEEP_DURATION);
					
					// add student to the database
					driver.findElement(By.xpath("//button[.='Add']")).click();
					Thread.sleep(SLEEP_DURATION);
					
					// check if student is added into the database
					Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
					assertNotNull(s, "Student not found in database.");
				} catch (Exception ex) {
					throw ex;
				} finally {
					// delete the student from the database
					Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
					if (s != null)
						studentRepository.delete(s);
					
					driver.quit();
				}

	}
}