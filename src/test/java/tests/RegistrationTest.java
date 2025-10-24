package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import data.Dataprovider;
import pages.RegistrationPage;
import utils.BaseTest;

import java.io.IOException;

@Test
public class RegistrationTest extends BaseTest {
    
    private RegistrationPage registrationPage;
    private final String url = "https://demoqa.com/automation-practice-form";
    
    @BeforeMethod
    public void setUp() {
        initializeDriver();
        registrationPage = new RegistrationPage(driver);
    }
    
    // Happy path
    @Test(dataProvider = "validRegistrationData", dataProviderClass = Dataprovider.class,enabled = true)
    public void testValidRegistration(String testType, String firstName, String lastName, 
                                    String email, String gender, String mobile,
                                    String dateOfBirth, String subjects, String address, 
                                    String expectedEmptyField) {
        
        String testName = "Valid Registration - " + firstName + " " + lastName;
        test = extent.createTest(testName);
        
        try {
            test.log(Status.INFO, "Starting valid registration test for: " + firstName + " " + lastName);
            
            // Launch browser and hit URL
            registrationPage.navigateToRegistrationPage(url);
            test.log(Status.INFO, "Navigated to: " + url);
            
            // Enter all required fields
            registrationPage.fillRegistrationForm(firstName, lastName, email, gender, mobile, dateOfBirth, subjects, address);
            test.log(Status.INFO, "Form filled with test data");
            
            // Capture form after filling
            captureFormState("Form_After_Filling");
            
            // Click on Submit
            registrationPage.clickSubmit();
            test.log(Status.INFO, "Submit button clicked");
            
            // Verify success modal is displayed
            boolean isModalDisplayed = registrationPage.isSuccessModalDisplayed();
            if (isModalDisplayed) {
                test.log(Status.PASS, "Success modal displayed after valid registration");
                
                // Capture success modal
                captureSuccessModal();
                
                // Verify individual fields
                verifySubmittedValues(firstName, lastName, email, gender, mobile, dateOfBirth, subjects, address);
                
            } else {
                test.log(Status.FAIL, "Success modal NOT displayed after valid registration");
                captureFormState("Modal_Not_Displayed");
                Assert.fail("Success modal should be displayed after valid registration");
            }
            
            // Assert all soft assertions
            registrationPage.assertAll();
            test.log(Status.PASS, "All soft assertions passed");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
            captureFormState("Test_Failure");
            throw e;
        }
    }
    
    // Negative flow
    @Test(dataProvider = "invalidRegistrationData", dataProviderClass = Dataprovider.class,enabled = true)
    public void testInvalidRegistration(String testType, String firstName, String lastName, 
                                      String email, String gender, String mobile,
                                      String dateOfBirth, String subjects, String address, 
                                      String expectedEmptyField) {
        
        String testName = "Invalid Registration - Missing: " + expectedEmptyField;
        test = extent.createTest(testName);
        
        try {
            test.log(Status.INFO, "Starting invalid registration test for missing field: " + expectedEmptyField);
            test.log(Status.INFO, "Test data - Name: " + firstName + " " + lastName + ", Email: " + email);
            
            // Launch browser and hit URL
            registrationPage.navigateToRegistrationPage(url);
            test.log(Status.INFO, "Navigated to: " + url);
            
            // Enter some fields (with missing data)
            registrationPage.fillRegistrationForm(firstName, lastName, email, gender, mobile, dateOfBirth, subjects, address);
            test.log(Status.INFO, "Form partially filled with missing: " + expectedEmptyField);
            
            // Capture form before submission
            captureFormState("Form_Before_Invalid_Submit");
            
            // Click on Submit
            registrationPage.clickSubmit();
            test.log(Status.INFO, "Submit button clicked for invalid data");
            
            registrationPage.scrollToFirstNameField();
            // Verify error is displayed
            boolean isFieldHighlighted = registrationPage.isFieldHighlightedRed(expectedEmptyField);
            if (isFieldHighlighted) {
                test.log(Status.PASS, "Field '" + expectedEmptyField + "' correctly highlighted in red for validation error");
                
                // Capture validation error
                captureValidationError(expectedEmptyField);
                
            } else {
                test.log(Status.FAIL, "Field '" + expectedEmptyField + "' was NOT highlighted in red as expected");
                captureFormState("Missing_Validation_Error");
                Assert.fail("Empty field '" + expectedEmptyField + "' should be highlighted in red");
            }
            
            // Assert all soft assertions
            registrationPage.assertAll();
            test.log(Status.PASS, "All soft assertions passed for invalid registration test");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Invalid registration test failed with exception: " + e.getMessage());
            captureFormState("Invalid_Test_Failure");
            throw e;
        }
    }
    
    // Helper methods for capturing screenshots
    private void captureFormState(String stateName) {
        try {
            String screenshotPath = takeFullPageScreenshot(stateName + ".png");
            if (screenshotPath != null) {
                attachFileScreenshot(screenshotPath, "Form State: " + stateName);
            }
        } catch (IOException e) {
            test.log(Status.WARNING, "Failed to capture form state screenshot: " + e.getMessage());
        }
    }
    
    private void captureSuccessModal() {
        try {
            String screenshotPath = takeFullPageScreenshot("Success_Modal.png");
            if (screenshotPath != null) {
                attachFileScreenshot(screenshotPath, "Success modal with submitted values");
                
                // Also log the submitted values
                logSubmittedValues();
            }
        } catch (IOException e) {
            test.log(Status.WARNING, "Failed to capture success modal screenshot: " + e.getMessage());
        }
    }
    
    private void captureValidationError(String fieldName) {
        try {
            String screenshotPath = takeFullPageScreenshot("Validation_Error_" + fieldName.replace(" ", "_") + ".png");
            if (screenshotPath != null) {
                attachFileScreenshot(screenshotPath, "Validation error for field: " + fieldName);
            }
        } catch (IOException e) {
            test.log(Status.WARNING, "Failed to capture validation error screenshot: " + e.getMessage());
        }
    }
    
    private void logSubmittedValues() {
        try {
            StringBuilder submittedData = new StringBuilder();
            submittedData.append("Submitted Form Data:\n");
            
            String[] fields = {"Student Name", "Student Email", "Gender", "Mobile", "Date of Birth", "Subjects", "Address"};
            
            for (String field : fields) {
                String value = registrationPage.getSubmittedValue(field);
                submittedData.append(field).append(": ").append(value).append("\n");
            }
            
            test.log(Status.INFO, submittedData.toString());
        } catch (Exception e) {
            test.log(Status.WARNING, "Failed to log submitted values: " + e.getMessage());
        }
    }
    
    // Helper method to verify submitted values
    private void verifySubmittedValues(String firstName, String lastName, String email, 
                                     String gender, String mobile, String dateOfBirth, 
                                     String subjects, String address) {
        
        test.log(Status.INFO, "Verifying submitted values...");
        
        try {
            String expectedName = firstName + " " + lastName;
            String actualName = registrationPage.getSubmittedValue("Student Name");
            Assert.assertEquals(actualName, expectedName, "Name should match");
            test.log(Status.PASS, "Name verification passed: " + actualName);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Name verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String actualEmail = registrationPage.getSubmittedValue("Student Email");
            Assert.assertEquals(actualEmail, email, "Email should match");
            test.log(Status.PASS, "Email verification passed: " + actualEmail);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Email verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String actualGender = registrationPage.getSubmittedValue("Gender");
            Assert.assertEquals(actualGender, gender, "Gender should match");
            test.log(Status.PASS, "Gender verification passed: " + actualGender);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Gender verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String actualMobile = registrationPage.getSubmittedValue("Mobile");
            Assert.assertEquals(actualMobile, mobile, "Mobile should match");
            test.log(Status.PASS, "Mobile verification passed: " + actualMobile);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Mobile verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String expectedDate = dateOfBirth.replace(" ", ",");
            String actualDate = registrationPage.getSubmittedValue("Date of Birth").replace(" ", ",");
            Assert.assertEquals(actualDate, expectedDate, "Date of Birth should match");
            test.log(Status.PASS, "Date of Birth verification passed: " + actualDate);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Date of Birth verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String expectedSub = subjects;
            String actualSub = registrationPage.getSubmittedValue("Subjects");
            Assert.assertTrue(actualSub.toLowerCase().contains(expectedSub.toLowerCase()),
                "Subjects should match. Expected: " + expectedSub + ", Actual: " + actualSub);
            test.log(Status.PASS, "Subjects verification passed: " + actualSub);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Subjects verification failed: " + e.getMessage());
            throw e;
        }
        
        try {
            String actualAddress = registrationPage.getSubmittedValue("Address");
            Assert.assertEquals(actualAddress, address, "Address should match");
            test.log(Status.PASS, "Address verification passed: " + actualAddress);
            
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Address verification failed: " + e.getMessage());
            throw e;
        }
    }
    
    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        super.tearDownTest(result);
    }
}