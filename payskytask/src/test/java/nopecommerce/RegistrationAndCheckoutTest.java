package nopecommerce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RegistrationAndCheckoutTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--ignore-ssl-errors");
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Initialize WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return new Object[][]{
            {"kareem", "mohamed", "kareem.m.ebrahim@outlook.com", "P@ssw0rd", "P@ssw0rd"}
        };
    }

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        return new Object[][]{
            {"kareem","Mohamed","kareem.m.ebrahim@outlook.com","cairo", "Shoubra", "002", "01111450613", "Visa", "4111111111111111", "123"}
        };
    }

    // Method for User Registration
    @Test(dataProvider = "registrationData")
    public void registerUser(String firstName, String lastName, String email, String password, String confirmPassword) {
        driver.get("https://demo.nopcommerce.com/register");

        // Locate the first name field and enter data
        WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FirstName")));
        firstNameField.sendKeys(firstName);
        
        // Locate the last name field and enter data
        WebElement lastNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("LastName")));
        lastNameField.sendKeys(lastName);
        
        // Locate the email field and enter data
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Email")));
        emailField.sendKeys(email);
        
        // Locate the password field and enter data
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Password")));
        passwordField.sendKeys(password);

        // Locate the confirm password field and enter data
        WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ConfirmPassword")));
        confirmPasswordField.sendKeys(confirmPassword);

        // Click the register button
        WebElement registerButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("register-button")));
        registerButton.click();

        // Confirm registration success
        WebElement registrationSuccessMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("result")));
        Assert.assertTrue(registrationSuccessMessage.getText().contains("Your registration completed"), "Registration failed");

        // Click the continue button
        WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#main > div > div > div > div.page-body > div.buttons > a")));
        continueButton.click();
    }
    
    
    // Method for Adding Product to Cart
    @Test(dependsOnMethods = "registerUser")
    public void addProductToCart() {
    	
    	// Add product to cart
        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#main > div > div > div > div > div.product-grid.home-page-product-grid > div.item-grid > div:nth-child(3) > div > div.details > div.add-info > div.buttons > button.button-2.product-box-add-to-cart-button")));
        addToCart.click();

        // open Shopping cart
        WebElement Shoppingcartbutton = wait.until(ExpectedConditions.elementToBeClickable(By.className("cart-label")));
        Shoppingcartbutton.click();
        
        // Accept terms and condition
        WebElement termsCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice")));
        termsCheckbox.click();
        
        // click on checkout button
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
        checkoutButton.click();
    }

    
    
    // Method for Add the Payment Details
    @Test(dependsOnMethods = "addProductToCart", dataProvider = "checkoutData")
    public void completePayment( String pfirstname,String plastname,String pemail, String city, String address, String zip, String phone, String cardType, String cardNumber, String cvv) {
        
    	
    	// Add details before selecting payment method
    	
    	//Add first Name
        WebElement billingFirstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_FirstName")));
        billingFirstNameField.clear();
        billingFirstNameField.sendKeys(pfirstname);
        
        // Add last Name
        WebElement billingLastNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_LastName")));
        billingLastNameField.sendKeys(plastname);

        // Add Email
        WebElement billingEmailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_Email")));
        billingEmailField.sendKeys(pemail);

        // Select country
        WebElement countryDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_CountryId")));
        Select selectCountry = new Select(countryDropdown);
        selectCountry.selectByVisibleText("Egypt");

        // Add city
        WebElement billingCityField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_City")));
        billingCityField.sendKeys(city);

        // Add Address 1
        WebElement billingAddressField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_Address1")));
        billingAddressField.sendKeys(address);

        // Add Zip code
        WebElement billingZipField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_ZipPostalCode")));
        billingZipField.sendKeys(zip);

        // Add phone Number
        WebElement billingPhoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_PhoneNumber")));
        billingPhoneField.sendKeys(phone);

        // Click on continue button
        WebElement billingContinueButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#billing-buttons-container > button.button-1.new-address-next-step-button")));
        billingContinueButton.click();

        
         //Add payment details
        // Select shipping method
        WebElement selectshippingMethod = wait.until(ExpectedConditions.elementToBeClickable(By.id("shippingoption_0")));
        selectshippingMethod.click();
        
        // clicking on continue button
        WebElement shippingMethodContinueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"payment-method-buttons-container\"]/button")));
        shippingMethodContinueButton.click();

        // Select payment method "credit card"
        WebElement selectpaymentMethod = wait.until(ExpectedConditions.elementToBeClickable(By.id("paymentmethod_1")));
        selectpaymentMethod.click();

        // clicking on continue button
        WebElement paymentMethodContinueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"payment-method-buttons-container\"]/button")));
        paymentMethodContinueButton.click();
        
        // select card type
        WebElement cardTypeDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CreditCardType")));
        cardTypeDropdown.sendKeys(cardType);

       // Add cardholder name
        WebElement cardholderNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CardholderName")));
        cardholderNameField.sendKeys("kareem mohamed ebrahim");

        // Add card number
        WebElement cardNumberField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CardNumber")));
        cardNumberField.sendKeys(cardNumber);

        // Select Expire date (month)
        WebElement expMonthDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ExpireMonth")));
        Select selectExpireMonth = new Select(expMonthDropdown);
        selectExpireMonth.selectByValue("12");

        // Select Expire date (year)
        WebElement expYearDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ExpireYear")));
        Select selectExpireYear = new Select(expYearDropdown);
        selectExpireYear.selectByValue("2025");

        // Add CVV
        WebElement cvvField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CardCode")));
        cvvField.sendKeys(cvv);

       // Click on continue button
        WebElement continueOrderButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"billing-buttons-container\"]/button[2]")));
        continueOrderButton.click();

        // Click on Confirm button
        WebElement confirmOrderButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"shipping-method-buttons-container\"]/button")));
        confirmOrderButton.click();
        
        // Assert the order was completed
        WebElement orderConfirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(),'Your order has been successfully processed!')]")));
        Assert.assertTrue(orderConfirmationMessage.isDisplayed(), "Order was not completed successfully");

        // Print a success message to the console
        System.out.println("Order completed successfully");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
