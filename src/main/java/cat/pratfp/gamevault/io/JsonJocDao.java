package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Lectura i escriptura del catàleg en JSON amb Jackson.
 */
public class JsonJocDao {

    private final ObjectMapper mapper = creaMapper();

    /*
     * PR1-08 · Configuració de l'ObjectMapper.
     * Registra el suport per a LocalDate, escriu les dates com a text
     * ISO-8601 i ignora els camps desconeguts durant la lectura.
     */
    private static ObjectMapper creaMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Permet llegir i escriure tipus java.time, com LocalDate.
        mapper.registerModule(new JavaTimeModule());

        // Escriu les dates com "2017-02-24" i no com [2017, 2, 24].
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // No falla si el JSON conté camps desconeguts.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }

    /*
     * PR1-08 · Escriure la llista completa en JSON indentat.
     */
    public void escriu(Path p, List<Joc> jocs) throws IOException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(p.toFile(), jocs);
    }

    /*
     * PR1-09 · Llegir la llista de jocs des d'un fitxer JSON.
     * TypeReference permet a Jackson conèixer el tipus dels elements.
     */
    public List<Joc> llegeix(Path p) throws IOException {
        return mapper.readValue(
                p.toFile(),
                new TypeReference<List<Joc>>() {
                });
    }
}
