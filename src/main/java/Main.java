import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

//Mian.java

public class Main {

    private static final PokeApiService service = new PokeApiService();

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", Main::arquivosEstaticos);
        server.createContext("/api/pokemon", Main::buscarPokemon);
        server.start();
        System.out.println("servidor rodando: http://localhost:8080");
        System.out.println("fechamos com ctrl+c");

    }

    private static void arquivosEstaticos(HttpExchange troca) {
        try {
           String path=troca.getRequestURI().getPath();
           if(path.equals(("/"))){
            path= "/html/index.html";
            }

            if (path.contains((".."))) {
                enviar(troca, 404, "text/plain; charset=utf-8", "Arquivo não encontrado");
                return ;
            }

            try (InputStream arquivo = Main.class.getResourceAsStream(path)) {
                if (arquivo == null) {
                    enviar(troca, 404, "text/plain; charset=utf-8", "não achei o index.html");
                    return;
                }
                String html = new String(arquivo.readAllBytes(), StandardCharsets.UTF_8);
                enviar(troca, 200, tipoDoArquivo(path), html);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  static String tipoDoArquivo(String path){
        if(path.endsWith(".html"))return "text/html; charset=utf-8";
        if(path.endsWith(".css"))return "text/css; charset=utf-8";
        if(path.endsWith(".js")) return "text/javascript; charset=utf-8";
        return "text/plain; charset=utf-8";
    }

    private static void buscarPokemon(HttpExchange troca){
        try{
            String pergunta = troca.getRequestURI().getQuery();
            if( pergunta == null || !pergunta.startsWith("nome=") || pergunta.length()<=5){
                enviar(troca, 404, "application/json; charset=utf-8;", "{\"erro\":\"Digite o nome de um Pokémon\"}");
                return;
            }

            String nome = pergunta.substring(5);
            Optional<Pokemon> resultado = service.buscarPokemon(nome);
            if(resultado.isEmpty()){
                enviar(troca, 404, "application/json; charset=utf-8;", "{\"erro\":\"Pokémon não encontrado\"}");
                return;
            }
            String json = mapper.writeValueAsString(resultado.get());
            enviar(troca,200,"application/json; charset=utf-8", json);
        } catch(Exception e){
            e.printStackTrace();
            try{
                enviar(troca, 500, "application/json; charset=utf-8",
                    "{\"erro\":\"Deu ruim ao tentar falar com PokéAPI chefe\"}");
            } catch(Exception ignorar){

            }
        }
    }

    private  static  void enviar(HttpExchange troca, int codigo, String tipo, String texto) throws Exception{
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", tipo);
        troca.sendResponseHeaders(codigo, bytes.length);
        try(var saida = troca.getResponseBody()){
            saida.write((bytes));
        }
    }
}