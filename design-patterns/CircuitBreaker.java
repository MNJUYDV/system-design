import java.util.Random;

public class CircuitBreaker {
    private static final int FAILURE_THRESHOLD = 3; // Number of consecutive failures to trigger the circuit breaker
    private static final int TIMEOUT = 500; // Timeout in milliseconds for each request

    private int consecutiveFailures = 0;
    private boolean circuitOpen = false;

    public String fetchData() {
        if (circuitOpen) {
            return "Circuit is open. Request denied.";
        }

        try {
            if (fetchDataFromServer()) {
                // Reset consecutive failures on successful response
                consecutiveFailures = 0;
                return "Data fetched successfully.";
            } else {
                // Handle the case where the request fails
                consecutiveFailures++;
                if (consecutiveFailures >= FAILURE_THRESHOLD) {
                    // If consecutive failures exceed the threshold, open the circuit
                    circuitOpen = true;
                    return "Circuit is open. Request denied.";
                }
                return "Request failed. Retrying...";
            }
        } catch (Exception e) {
            consecutiveFailures++;
            if (consecutiveFailures >= FAILURE_THRESHOLD) {
                // If consecutive failures exceed the threshold, open the circuit
                circuitOpen = true;
                return "Circuit is open. Request denied.";
            }
            return "Request failed. Retrying...";
        }
    }

    private boolean fetchDataFromServer() {
        // Simulate a successful or failed response
        Random random = new Random();
        return random.nextBoolean();
    }

    public static void main(String[] args) {
        CircuitBreaker circuitBreaker = new CircuitBreaker();

        // Simulate making multiple requests
        for (int i = 0; i < 10; i++) {
            String response = circuitBreaker.fetchData();
            System.out.println("Request " + (i + 1) + ": " + response);

            // Sleep between requests to simulate intermittent failures and retries
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
