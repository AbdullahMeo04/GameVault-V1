package cat.pratfp.gamevault.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registre de les operacions de GameVault en un fitxer de text
 * (operacions.log).
 */
public class RegistreOperacions {

    /** Format de la data i hora de cada línia: 2026-10-13 15:10:02 */
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path fitxer;

    /**
     * @param fitxer fitxer de registre; si no existeix, es crea en la primera
     *               escriptura
     */
    public RegistreOperacions(Path fitxer) {
        this.fitxer = fitxer;
    }

    /*
     * PR1-05 · Escriure al registre sense esborrar-lo (CA 1.4)
     *
     * Afegeix una línia al final del fitxer amb el format:
     * 2026-10-13 15:10:02 | <text>
     *
     * La data i hora es genera amb LocalDateTime.now().
     * El fitxer es codifica en UTF-8.
     * Si no existeix, es crea; si existeix, es conserven les línies anteriors.
     */
    public void registra(String text) throws IOException {
        String dataHora = LocalDateTime.now().format(FORMAT);

        try (BufferedWriter writer = Files.newBufferedWriter(
                fitxer,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(dataHora + " | " + text);
            writer.newLine();
        }
    }
}
