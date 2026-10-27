package cat.pratfp.gamevault.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * El magatzem de fitxers de GameVault: una carpeta base amb tres subcarpetes,
 * import, export i backup.
 */
public class Magatzem {

    private static final DateTimeFormatter MARCA = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path base;

    /**
     * Crea un magatzem sobre una carpeta base. No toca el disc.
     *
     * @param base carpeta arrel del magatzem (per exemple, Path.of("dades"))
     */
    public Magatzem(Path base) {
        this.base = base;
    }

    /** @return la carpeta d'importació (base/import) */
    public Path importacio() {
        return base.resolve("import");
    }

    /** @return la carpeta d'exportació (base/export) */
    public Path exportacio() {
        return base.resolve("export");
    }

    /** @return la carpeta de còpies de seguretat (base/backup) */
    public Path copies() {
        return base.resolve("backup");
    }

    /**
     * PR1-02: Crea les tres carpetes del magatzem.
     * Si ja existeixen, no produeix cap error.
     *
     * @throws IOException si no es poden crear les carpetes
     */
    public void preparaEstructura() throws IOException {
        Files.createDirectories(importacio());
        Files.createDirectories(exportacio());
        Files.createDirectories(copies());
    }

    /**
     * PR1-03: Crea una còpia de seguretat amb una marca de temps al nom.
     *
     * Exemple: catalog.csv -> backup/catalog_20261013-151002.csv
     *
     * @param fitxer fitxer original que es vol copiar
     * @return ruta de la còpia de seguretat
     * @throws IOException si no es pot copiar el fitxer
     */
    public Path copiaSeguretat(Path fitxer) throws IOException {
        preparaEstructura();

        String nomOriginal = fitxer.getFileName().toString();
        int punt = nomOriginal.lastIndexOf('.');

        String nom;
        String extensio;

        if (punt > 0) {
            nom = nomOriginal.substring(0, punt);
            extensio = nomOriginal.substring(punt);
        } else {
            nom = nomOriginal;
            extensio = "";
        }

        String nomCopia = nom + "_" + marcaDeTemps() + extensio;
        Path desti = copies().resolve(nomCopia);

        Files.copy(
                fitxer,
                desti,
                StandardCopyOption.REPLACE_EXISTING);

        return desti;
    }

    /**
     * PR1-04: Calcula la mida total de tots els fitxers regulars
     * de la carpeta base i les seves subcarpetes.
     *
     * @return mida total en bytes
     * @throws IOException si no es pot recórrer la carpeta
     */
    public long midaTotal() throws IOException {
        if (!Files.exists(base)) {
            return 0L;
        }

        try (Stream<Path> camins = Files.walk(base)) {
            long total = 0L;

            for (Path cami : camins.filter(Files::isRegularFile).toList()) {
                total += Files.size(cami);
            }

            return total;
        }
    }

    /**
     * Llista els fitxers regulars del magatzem amb la seva mida,
     * ordenats per ruta.
     *
     * @return una línia per fitxer, amb el format "ruta relativa (N bytes)"
     * @throws IOException si no es pot recórrer la carpeta
     */
    public List<String> inventari() throws IOException {
        if (!Files.exists(base)) {
            return List.of();
        }

        try (Stream<Path> camins = Files.walk(base)) {
            return camins
                    .filter(Files::isRegularFile)
                    .sorted()
                    .map(p -> base.relativize(p) + " (" + mida(p) + " bytes)")
                    .toList();
        }
    }

    /** Mida d'un fitxer sense excepció comprovada. */
    private static long mida(Path p) {
        try {
            return Files.size(p);
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Marca de temps per als noms de les còpies.
     *
     * @return data i hora actuals amb el format yyyyMMdd-HHmmss
     */
    public static String marcaDeTemps() {
        return LocalDateTime.now().format(MARCA);
    }
}
