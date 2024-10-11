import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {
    // OpenWeatherMap API Key (Replace this with your API key)
    private static final String API_KEY = "your_openweathermap_api_key";

    // OpenWeatherMap URL for fetching current weather
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        try {
            // Take the city name from user input
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the city name: ");
            String city = reader.readLine();

            // Build the complete API request URL
            String urlString = BASE_URL + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Check the response code
            int status = connection.getResponseCode();
            if (status == 200) {
                // Read the API response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Close the connections
                in.close();
                connection.disconnect();

                // Parse the JSON response
                JSONObject weatherData = new JSONObject(content.toString());
                displayWeatherInfo(weatherData);
            } else {
                System.out.println("City not found or unable to retrieve weather data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayWeatherInfo(JSONObject weatherData) {
        // Extracting the necessary data from the JSON response
        String cityName = weatherData.getString("name");
        JSONObject main = weatherData.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");

        JSONObject weather = weatherData.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("Weather in " + cityName + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Feels Like: " + feelsLike + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }
}
