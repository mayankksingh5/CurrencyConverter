import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "YOUR_APP_ID";
    private static final String API_URL = "https://open.er-api.com/v6/latest/";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter the source currency code (e.g., USD): ");
            String sourceCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Enter the target currency code (e.g., EUR): ");
            String targetCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Enter the amount to convert: ");
            double amount = scanner.nextDouble();

            double result = convertCurrency(sourceCurrency, targetCurrency, amount);

            System.out.printf("%s %.2f is equivalent to %s %.2f%n", sourceCurrency, amount, targetCurrency, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double convertCurrency(String sourceCurrency, String targetCurrency, double amount)
            throws Exception {
        String apiUrl = String.format("%s%s?apikey=%s", API_URL, sourceCurrency, API_KEY);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        double exchangeRate = parseExchangeRate(response.toString(), targetCurrency);

        return amount * exchangeRate;
    }

    private static double parseExchangeRate(String jsonResponse, String targetCurrency) {

        int startIndex = jsonResponse.indexOf(targetCurrency) + 5;
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String exchangeRateStr = jsonResponse.substring(startIndex, endIndex);

        return Double.parseDouble(exchangeRateStr);
    }
}
