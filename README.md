# 🧪 DemoQA Test Automation Framework

A comprehensive Selenium WebDriver automation framework for testing the DemoQA practice form, built with Java and TestNG.

## 📋 Project Overview

This automation framework tests the DemoQA Automation Practice Form with both positive and negative test scenarios, featuring robust reporting, screenshot capture, and detailed logging.

## 🏗️ Project Structure

```
src/test/java/
├── pages/
│   └── RegistrationPage.java          # Page Object Model for registration form
├── tests/
│   └── RegistrationTest.java          # Test cases with logging & screenshots
├── datadriven/
│   └── Dataprovider.java              # Excel-based data provider
└── resources/
    └── RegistrationData.xlsx          # Test data in Excel format

test-output/
├── screenshots/                       # Automatic screenshot capture
├── traces/                            # Performance traces (if enabled)
└── testng-reports/                    # TestNG HTML reports
```

## 🚀 Features

- **✅ Page Object Model** - Clean separation of test logic and page interactions
- **✅ Data-Driven Testing** - Excel-based test data management
- **✅ Comprehensive Logging** - Detailed step-by-step execution logs
- **✅ Screenshot Capture** - Automatic screenshots on failure and key steps
- **✅ Cross-Browser Ready** - Configurable browser support
- **✅ Robust Waits** - Explicit waits for stable test execution
- **✅ Negative Testing** - Comprehensive error scenario coverage
- **✅ Trace Viewer Support** - Ready for performance monitoring

## 🛠️ Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome Browser
- ChromeDriver

## 📦 Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd demoqa-automation
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Set up ChromeDriver**
   - Download ChromeDriver from https://chromedriver.chromium.org/
   - Ensure it's in your system PATH or update the path in `RegistrationTest.java`

## ⚙️ Configuration

### Test Data Setup

1. **Excel File Location**: `src/test/resources/RegistrationData.xlsx`
2. **Sheet Names**:
   - `Happy Path` - Valid test scenarios
   - `Negative Flow` - Invalid test scenarios

### Excel Columns Format

**Happy Path Sheet:**
| FirstName | LastName | Email | Gender | Mobile | DateOfBirth | Subjects | Address |

**Negative Flow Sheet:**
| FirstName | LastName | Email | Gender | Mobile | DateOfBirth | Subjects | Address | ExpectedEmptyField |

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox
```

### Run with Specific Test Group
```bash
mvn test -Dgroups="valid-registration"
```

## 📊 Test Scenarios

### ✅ Positive Test Cases
- Complete form submission with valid data
- Verification of submitted values in success modal
- All mandatory field validations

### ❌ Negative Test Cases  
- Empty required field validation
- Field highlighting for errors
- Success modal suppression on invalid submissions

## 📁 Output Reports

### Automatic Outputs
- **Screenshots**: `test-output/screenshots/`
- **TestNG Reports**: `test-output/testng-reports/`
- **Execution Logs**: Console output with emojis and timestamps

### Sample Log Output
```
🚀 ===== STARTING TEST: testValidRegistration =====
📝 Description: Testing valid registration with: John Doe
⏰ Start Time: Fri Dec 01 14:30:22 IST 2023
  ↪️ STEP: Navigating to registration page
  ℹ️ INFO: Screenshot saved: test-output/screenshots/01_navigation_20231201_143022.png
  ↪️ STEP: Filling registration form
✅ ===== TEST PASSED: testValidRegistration =====
```

## 🛠️ Framework Components

### RegistrationPage.java
- Page Object Model for DemoQA form
- Element locators and interaction methods
- Form validation and submission logic

### RegistrationTest.java  
- TestNG test cases with data providers
- Comprehensive logging and screenshot capture
- Assertion and verification logic

### Dataprovider.java
- Excel data reader using Apache POI
- Support for multiple test data sheets
- Data formatting and validation

## 🔧 Customization

### Adding New Test Cases
1. Add test data to Excel sheets
2. Update `RegistrationPage.java` for new elements
3. Add test methods in `RegistrationTest.java`

### Modifying Wait Times
```java
private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
```

### Adding New Browsers
Update `setUp()` method in `RegistrationTest.java`:
```java
// For Firefox
driver = new FirefoxDriver();

// For Edge  
driver = new EdgeDriver();
```

## 🐛 Troubleshooting

### Common Issues

1. **ChromeDriver Version Mismatch**
   - Ensure ChromeDriver version matches your Chrome browser
   - Download from: https://chromedriver.chromium.org/

2. **Excel File Not Found**
   - Verify file path: `src/test/resources/RegistrationData.xlsx`
   - Check file permissions

3. **Element Not Found Errors**
   - Check DemoQA website updates
   - Update element locators in `RegistrationPage.java`

4. **Data Provider Issues**
   - Verify Excel sheet names match exactly
   - Check for extra spaces in column headers

### Debug Mode
Enable detailed logging by adding:
```java
System.setProperty("webdriver.chrome.logfile", "test-output/chrome-driver.log");
```

## 📈 Best Practices

1. **Keep Test Data Separate** - Maintain test data in Excel files
2. **Use Explicit Waits** - Avoid thread.sleep() for better performance
3. **Regular Maintenance** - Update locators when website changes
4. **Meaningful Screenshot Names** - Use descriptive names for easy debugging
5. **Comprehensive Logging** - Log key steps for better traceability

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For issues and questions:
1. Check troubleshooting section
2. Review test output logs
3. Verify environment setup
4. Contact the development team

---

**Happy Testing!** 🎯
