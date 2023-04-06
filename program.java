import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient.Version;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiClient {

    private final HttpClient _httpClient;
    private final ObjectMapper _jsonMapper;
    private String _token;
    private static final String CLIENT_ID = "b7cd6404-dfb4-4d55-8a3a-879d04d72268:0651d719-28da-4433-a1c4-1d4a2e264ff2";
    private static final String SECRET = "a152d18e-29c5-4223-8945-382c6569487a";
    private static final String JWT_BASE_URL = "https://localhost:44381/api/v1/connect/token";
    private static final String VERSION = "2.5.1";

    public ApiClient() {
        _httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        _jsonMapper = new ObjectMapper();
    }

    private String getToken() throws Exception {
        try {
            List<String> parms = new ArrayList<>();
            parms.add("client_id=" + CLIENT_ID);
            parms.add("client_secret=" + SECRET);
            parms.add("grant_type=client_credentials");

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(JWT_BASE_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(String.join("&", parms)));

            HttpResponse<String> response = _httpClient.send(builder.build(), BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("API returned " + response.statusCode());
            }
            return response.body();
        } catch (Exception ex) {
            throw new IOException("API returned " + ex.getMessage());
        }
    }

    public <T> T postAsync(String url, BiometricPayload postContent, Class<T> responseType) throws Exception {
        if (_token == null)
            _token = _jsonMapper.readTree(getToken()).get("accessToken").asText();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + _token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(_jsonMapper.writeValueAsString(postContent)));

        HttpResponse<String> response = _httpClient.send(builder.build(), BodyHandlers.ofString());
        if (response.statusCode() != 200)
            throw new IOException("API returned " + response.statusCode());

        return _jsonMapper.readValue(response.body(), responseType);
    }
}

public class AuthenticateUserService {

    private final ApiClient _apiClient;

    public AuthenticateUserService(ApiClient apiClient) {
        _apiClient = apiClient;
    }

    public UserAccount authenticateAsync(String authUrl, BiometricPayload payload) throws Exception {
        return _apiClient.postAsync(authUrl, payload, UserAccount.class);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount {
    private String accountUuid;

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }
}

public class BiometricPayload {
    private String faceBytes;
    private String palm
}


// Most of it Still C# convert to Java
public class Program() {
 
        public Main()
        {
            private final ApiClient _apiClient;
            private final AuthenticateUserService authService(apiClient);
            const string USER_AUTHENTICATION_BASE_URL = "https://localhost:44361/api/v1/authenticate/";
            
            private final BiometricPayload payload; 
          
            payload.FaceBytes = "";  // Paste Face Base64 string here or read from file
            payload.PalmBytes = "";  // Paste Palm Base64 string here or read from file

            var accountUuid = await authService.AuthenticateAsync(USER_AUTHENTICATION_BASE_URL, payload);
            Console.WriteLine($"Account Uuid - {accountUuid}");
            Console.ReadLine();
        }
}
