package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class RegistrationPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private SoftAssert softAssert;
    
    // Locators for required fields
    @FindBy(id = "firstName")
    private WebElement firstNameField;
    
    @FindBy(id = "lastName")
    private WebElement lastNameField;
    
    @FindBy(id = "userEmail")
    private WebElement emailField;
    
    // Gender locators - update to target labels instead of radio buttons
    @FindBy(xpath = "//label[contains(@for,'gender-radio-1')]")
    private WebElement maleGenderLabel;
    
    @FindBy(xpath = "//label[contains(@for,'gender-radio-2')]")
    private WebElement femaleGenderLabel;
    
    @FindBy(xpath = "//label[contains(@for,'gender-radio-3')]")
    private WebElement otherGenderLabel;
    
    // Gender radio buttons for verification
    @FindBy(id = "gender-radio-1")
    private WebElement maleGenderRadio;
    
    @FindBy(id = "gender-radio-2")
    private WebElement femaleGenderRadio;
    
    @FindBy(id = "gender-radio-3")
    private WebElement otherGenderRadio;
    
    @FindBy(id = "userNumber")
    private WebElement mobileField;
    
    @FindBy(id = "dateOfBirthInput")
    private WebElement dateOfBirthField;
    
    @FindBy(className = "react-datepicker__month-select")
    private WebElement monthDropdown;
    
    @FindBy(className = "react-datepicker__year-select")
    private WebElement yearDropdown;
    
    @FindBy(className = "react-datepicker__day")
    private List<WebElement> dayElements;
    
    @FindBy(id = "subjectsInput")
    private WebElement subjectsField;
    
    @FindBy(id = "currentAddress")
    private WebElement addressField;
    
    @FindBy(id = "submit")
    private WebElement submitButton;
    
    @FindBy(className = "modal-content")
    private WebElement successModal;
    
    // Subjects auto-suggestion
    @FindBy(className = "subjects-auto-complete__menu-list")
    private WebElement subjectsSuggestionList;
    
    // Constructor
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.softAssert = new SoftAssert();
        PageFactory.initElements(driver, this);
    }
    
    // Navigate to registration page
    public void navigateToRegistrationPage(String url) {
        driver.get(url);
    }
    
    // Scroll to first name field
    public void scrollToFirstNameField() {
        try {
            wait.until(ExpectedConditions.visibilityOf(firstNameField));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstNameField);
            // Small delay to ensure scroll completes
//            Thread.sleep(500);
        } catch (Exception e) {
        	
            System.out.println("Error scrolling to first name field: " + e.getMessage());
        }
    }
    
    // Fill registration form with all fields
    public void fillRegistrationForm(String firstName, String lastName, 
                                   String email, String gender, String mobile, 
                                   String dateOfBirth, String subjects, String address) {
        // Scroll to first name field first
        scrollToFirstNameField();
        
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setGender(gender);
        setMobileNumber(mobile);
        setDateOfBirth(dateOfBirth);
        setSubjects(subjects);
        setCurrentAddress(address);
    }
    
    // Individual field methods
    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(firstNameField));
            firstNameField.clear();
            firstNameField.sendKeys(firstName);
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null && !lastName.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(lastNameField));
            lastNameField.clear();
            lastNameField.sendKeys(lastName);
        }
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(emailField));
            emailField.clear();
            emailField.sendKeys(email);
        }
    }

    // Set gender method - updated to click labels with soft assertion
    public void setGender(String gender) {
        if (gender != null && !gender.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(maleGenderLabel));
            
            switch (gender.toLowerCase()) {
                case "male":
                    wait.until(ExpectedConditions.elementToBeClickable(maleGenderLabel));
                    maleGenderLabel.click();
                    // Verify gender is selected using soft assertion
                    verifyGenderSelected("male");
                    break;
                case "female":
                    wait.until(ExpectedConditions.elementToBeClickable(femaleGenderLabel));
                    femaleGenderLabel.click();
                    // Verify gender is selected using soft assertion
                    verifyGenderSelected("female");
                    break;
                case "other":
                    wait.until(ExpectedConditions.elementToBeClickable(otherGenderLabel));
                    otherGenderLabel.click();
                    // Verify gender is selected using soft assertion
                    verifyGenderSelected("other");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid gender: " + gender);
            }
        }
    }

    // Verify gender selection using soft assertion
    private void verifyGenderSelected(String gender) {
        try {
            boolean isSelected = false;
            
            switch (gender.toLowerCase()) {
                case "male":
                    isSelected = Boolean.parseBoolean(maleGenderRadio.getAttribute("checked"));
                    softAssert.assertTrue(isSelected, "Male gender should be selected");
                    break;
                case "female":
                    isSelected = Boolean.parseBoolean(femaleGenderRadio.getAttribute("checked"));
                    softAssert.assertTrue(isSelected, "Female gender should be selected");
                    break;
                case "other":
                    isSelected = Boolean.parseBoolean(otherGenderRadio.getAttribute("checked"));
                    softAssert.assertTrue(isSelected, "Other gender should be selected");
                    break;
            }
            
            
        } catch (Exception e) {
            System.out.println("Error verifying gender selection: " + e.getMessage());
            softAssert.fail("Error verifying " + gender + " gender selection: " + e.getMessage());
        }
    }

    // Method to assert all soft assertions
    public void assertAll() {
        softAssert.assertAll();
    }

    public void setMobileNumber(String mobile) {
        if (mobile != null && !mobile.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(mobileField));
            mobileField.clear();
            mobileField.sendKeys(mobile);
        }
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
        	wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthField));
            
            // Scroll to the date field first
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dateOfBirthField);
//            Thread.sleep(500);
            
            // Use JavaScript click to avoid element interception
            js.executeScript("arguments[0].click();", dateOfBirthField);
            // Wait for date picker to appear
            wait.until(ExpectedConditions.visibilityOf(monthDropdown));
            
            // Parse date string (format: "15 January 1990")
            String[] dateParts = dateOfBirth.split(" ");
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];
            
            // Select month
            wait.until(ExpectedConditions.elementToBeClickable(monthDropdown));
            monthDropdown.sendKeys(month);
            
            // Select year
            wait.until(ExpectedConditions.elementToBeClickable(yearDropdown));
            yearDropdown.sendKeys(year);
            
            // Wait for day elements to be available and select day
            wait.until(ExpectedConditions.visibilityOfAllElements(dayElements));
            for (WebElement dayElement : dayElements) {
                if (dayElement.getText().equals(day) && 
                    !dayElement.getAttribute("class").contains("outside-month")) {
                    wait.until(ExpectedConditions.elementToBeClickable(dayElement));
                    dayElement.click();
                    break;
                }
            }
        }
    }

    public void setSubjects(String subjects) {
        if (subjects != null && !subjects.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(subjectsField));
            subjectsField.clear();
            subjectsField.sendKeys(subjects);
            // Wait for suggestions and select first one
            wait.until(ExpectedConditions.visibilityOf(subjectsSuggestionList));
            subjectsField.sendKeys(org.openqa.selenium.Keys.ENTER);
        }
    }

    public void setCurrentAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(addressField));
            addressField.clear();
            addressField.sendKeys(address);
        }
    }
    
    // Click submit 
    public void clickSubmit() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            
            // Scroll the button into view
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", submitButton);
            
            // Small delay to ensure scroll completes
            Thread.sleep(500);
            
            submitButton.click();
        } catch (Exception e) {
            System.out.println("Regular click failed, using JavaScript click...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", submitButton);
        }
    }
    
    // Verify success modal is displayed
    public boolean isSuccessModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successModal));
            return successModal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Get values from success modal
    public String getSubmittedValue(String fieldName) {
        try {
            WebElement valueElement = driver.findElement(
                By.xpath("//td[text()='" + fieldName + "']/following-sibling::td"));
            return valueElement.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    // Check if field is highlighted in red (validation error)
    public boolean isFieldHighlightedRed(String fieldName) {
        try {
        	
//        	Thread.sleep(500);
            WebElement field = getFieldElement(fieldName);
            wait.until(ExpectedConditions.elementToBeClickable(field));
            
            
            // Get all border color properties
            String borderTopColor = field.getCssValue("border-top-color");
            String borderRightColor = field.getCssValue("border-right-color");
            String borderBottomColor = field.getCssValue("border-bottom-color");
            String borderLeftColor = field.getCssValue("border-left-color");
            
            // Check if any border contains the red color values (220, 53, 69)
            boolean hasRedBorder = 
                borderTopColor.contains("220, 53, 69") ||
                borderRightColor.contains("220, 53, 69") ||
                borderBottomColor.contains("220, 53, 69") ||
                borderLeftColor.contains("220, 53, 69");

            return hasRedBorder;
            
        } catch (Exception e) {
            System.out.println("Error checking field highlight for '" + fieldName + "': " + e.getMessage());
            return false;
        }
    }
    
    // Helper method to get field element
    private WebElement getFieldElement(String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "first name":
                return firstNameField;
            case "last name":
                return lastNameField;
            case "email":
                return emailField;
            case "mobile":
                return mobileField;
            case "date of birth":
                return dateOfBirthField;
            case "subjects":
                return subjectsField;
            case "address":
                return addressField;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
    
    // Method to check if gender radio is selected
    public boolean isGenderSelected(String gender) {
        try {
            switch (gender.toLowerCase()) {
                case "male":
                    return Boolean.parseBoolean(maleGenderRadio.getAttribute("checked"));
                case "female":
                    return Boolean.parseBoolean(femaleGenderRadio.getAttribute("checked"));
                case "other":
                    return Boolean.parseBoolean(otherGenderRadio.getAttribute("checked"));
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}