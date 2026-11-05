import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

//PokeApieServer.java

public class PokeApiService {
    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<Pokemon> buscarPokemon(String nome)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/pokemon/" + nome.toLowerCase().trim()))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            return Optional.empty();
        }
        if (response.statusCode() != 200) {
            throw new IOException("Erro na PokeAPI: HTTP" + response.statusCode());
        }
        return Optional.of(mapper.readValue(response.body(), Pokemon.class));
    }
}
