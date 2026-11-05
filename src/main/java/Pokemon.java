import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

//pokemon.java

@JsonIgnoreProperties (ignoreUnknown = true)
public  record Pokemon(
        int id,
        String name,
        int height,
        int weight,
        List<TypeSlot> types,
        List<StatSlot> stats,
        List<MivesSlot> moves,
        Sprites sprites){
}

@JsonIgnoreProperties (ignoreUnknown = true)
record TypeSlot(int slot, TypeInfo type){      
}

@JsonIgnoreProperties (ignoreUnknown = true)
record TypeInfo(String name, String url){ 
}

@JsonIgnoreProperties(ignoreUnknown = true)
record StatSlot(
        @JsonProperty("base_stat") int baseStat,StatInfo stat){

}

@JsonIgnoreProperties (ignoreUnknown = true)
record StatInfo (String name){

}

@JsonIgnoreProperties (ignoreUnknown = true)
record MivesSlot(MoveInfo move){

}

@JsonIgnoreProperties (ignoreUnknown = true)
record MoveInfo(String name){

}

@JsonIgnoreProperties(ignoreUnknown = true)
record Sprites(
        @JsonProperty("front_default") String frontDefault,
        Other other) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Other(
        @JsonProperty("official-artwork") Artwork officialArtwork) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Artwork(
        @JsonProperty("front_default") String frontDefault) {
}