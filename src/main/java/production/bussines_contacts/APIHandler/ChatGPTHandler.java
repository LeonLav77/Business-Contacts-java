package production.bussines_contacts.APIHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;
import production.bussines_contacts.exceptions.ChatGPTAnswerException;
import production.bussines_contacts.exceptions.ChatGPTRequestException;

public class ChatGPTHandler {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;

    public ChatGPTHandler(String apiKey) {
        this.apiKey = apiKey;
        this.baseUrl = "https://api.openai.com/v1/";
        this.httpClient = HttpClient.newHttpClient();
    }

    public JSONObject simpleRequest(String inputJson) throws IOException, InterruptedException, ChatGPTRequestException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseJson = response.body();

        JSONObject responseObject = new JSONObject(responseJson);
        if (responseObject.has("error")) {
            throw new ChatGPTRequestException(responseObject.getString("error"));
//           return responseObj<ect.getString("error");
        }

        JSONArray choices = responseObject.getJSONArray("choices");
        if (choices.length() > 0) {
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String jsonString = message.getString("content");
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }

        throw new ChatGPTAnswerException("Bad response from API");
    }
}
