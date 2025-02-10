import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class Result {

    public static List<String> getRelevantFoodOutlets(String city, int maxCost) {
        List<String> relevantOutlets = new ArrayList<>();
        String baseUrl = "https://jsonmock.hackerrank.com/api/food_outlets?city=" + city + "&page=";
        HttpClient client = HttpClient.newHttpClient();
        JSONParser parser = new JSONParser();
        
        int page = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            try {
                // Build the request for the current page
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + page))
                        .GET()
                        .build();

                // Send the request and parse the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

                // Extract pagination details
                long totalPages = (long) jsonResponse.get("total_pages");
                
                // Extract food outlet data
                JSONArray data = (JSONArray) jsonResponse.get("data");

                for (Object obj : data) {
                    JSONObject outlet = (JSONObject) obj;
                    long estimatedCost = (long) outlet.get("estimated_cost");
                    if (estimatedCost <= maxCost) {
                        relevantOutlets.add((String) outlet.get("name"));
                    }
                }

                // Move to the next page if available
                hasMorePages = page < totalPages;
                page++;
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
                break;
            }
        }
        return relevantOutlets;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String city = bufferedReader.readLine();
        int maxCost = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> result = Result.getRelevantFoodOutlets(city, maxCost);

        for (int i = 0; i < result.size(); i++) {
            bufferedWriter.write(result.get(i));
            if (i != result.size() - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}


// Questions

// REST API: Relevant Food Outlets

// A REST API contains information about food outlets across multiple cities. Given the city name and the maximum cost for 2 persons, use the API to get the list of food outlets in this city and have an estimated cost less than or equal to the given cost. The API returns paginated data.

// To access the information, perform an HTTP GET request

// to https://jsonmock.hackerrank.com/api/food outlets?city=<city>&page=<pageNumber> where <city> is the city to get the food outlets for, and <pageNumber>is an integer that denotes the page of the results to return.

// For example, a GET request to

// https://jsonmock.hackerrank.com/api/food_outlets?city=Seattle&page=2 returns data associated with the second page of results for Seattle.

// Similarly, a GET request to

// https://jsonmock.hackerrank.com/api/food_outlets?city=Houston&page=1 returns the first page of results for Houston.

// The response to such a request is a JSON with the following 5 fields:

// page: the current page of the results.

// per_page: the maximum number of records returned per page.

// total: the total number of records in the database.

// total_pages: the total number of pages with results.

// data: either an empty array or an array of outlet objects; each object has the following schema:

// * city: queried city where the outlet is located (STRING)

// name: name of the outlet [STRING]

// estimated_cost: estimated cost for 2 persons [INTEGER)

// user_rating:

// average_rating: average rating of the outlet [FLOAT]

// votes: total votes for the outlet [INTEGER]

// * id: unique identifier of the outlet [INTEGER]

// Below is an example of an outlet object:

// {

// "city": "Houston",

// "name": "Cocoa Tree",

// "estimated_cost": 10,

// "user_rating": {

// "average_rating": 4.5,

// "votes": 969

// },

// "id": 938

// Given a string city, the numerical maximum cost for 2 persons maxCost, return the list of food outlet names that are located in this city and have an estimated cost less than or equal to the given maxCost.

// Function Description

// Complete the function getRelevant FoodOutlets in the editor below. Please perform pagination in order to get all the results.