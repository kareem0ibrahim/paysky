package rest_assured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.restassured.AllureRestAssured;
import org.json.JSONObject;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

@Listeners({AllureTestNg.class})
public class UserCreationExample {
    
    // DataProvider for user data
    @DataProvider(name = "userData")
    public Object[][] provideUserData() {
        return new Object[][]{
                {"Kareem Mohamed", "Software Engineer"},
                {"Mohamed Ahmed", "UX Designer"},
        };
    }

    @Test(dataProvider = "userData")
    public void createUserTest(String name, String job) {
        // Create JSON object with provided user data
        JSONObject userJson = new JSONObject();
        userJson.put("name", name);
        userJson.put("job", job);

        // Add Allure filter for Rest-Assured
        RestAssured.filters(new AllureRestAssured());

        // Send POST request using Rest Assured
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson.toString())
                .post("https://reqres.in/api/users");

        // Assert that the response status code is 201
        if (response.getStatusCode() == 201) {
            System.out.println("User created successfully. Status code: " + response.getStatusCode());
        } else {
            throw new RuntimeException("Failed to create user. Status code: " + response.getStatusCode());
        }

        // Parse the response body
        JSONObject responseBody = new JSONObject(response.getBody().asString());

        // Validate that the response contains the expected data
        if (responseBody.has("id") && responseBody.getString("name").equals(name)
                && responseBody.getString("job").equals(job)) {
            System.out.println("Response contains the expected data.");
            System.out.println("User ID: " + responseBody.getString("id"));
            System.out.println("User Name: " + responseBody.getString("name"));
            System.out.println("User Job: " + responseBody.getString("job"));
        } else {
            throw new RuntimeException("Response does not contain the expected data.");
        }
    }
}
