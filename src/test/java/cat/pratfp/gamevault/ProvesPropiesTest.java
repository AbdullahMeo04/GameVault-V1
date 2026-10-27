package cat.pratfp.gamevault;

import cat.pratfp.gamevault.io.IndexHores;
import cat.pratfp.gamevault.model.Joc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProvesPropiesTest {

    @TempDir
    Path tmp;

    @Test
    @DisplayName("PR1-14 · l'índex conserva un títol de més de 40 caràcters amb mida fixa")
    void indexAmbTitolLlarg() throws Exception {
        Path fitxer = tmp.resolve("index-llarg.dat");
        IndexHores index = new IndexHores(fitxer);

        String titolLlarg = "A".repeat(50);
        Joc joc = new Joc(
                1,
                titolLlarg,
                "Accio",
                "PC",
                LocalDate.of(2024, 1, 1),
                12.5,
                9.0);

        index.escriu(0, joc);

        assertEquals(1, index.nombreRegistres());
        assertEquals(IndexHores.MIDA_REGISTRE, Files.size(fitxer));
        assertEquals(12.5, index.llegeixHores(0), 0.001);
    }

    @Test
    @DisplayName("PR1-14 · sumar hores al segon registre no modifica el primer ni la mida del fitxer")
    void sumaHoresAlSegonRegistre() throws Exception {
        Path fitxer = tmp.resolve("index-dos-registres.dat");
        IndexHores index = new IndexHores(fitxer);

        Joc primer = new Joc(
                1,
                "Minecraft",
                "Sandbox",
                "PC",
                LocalDate.of(2024, 1, 1),
                10.0,
                9.0);

        Joc segon = new Joc(
                2,
                "Stardew Valley",
                "Simulacio",
                "PC",
                LocalDate.of(2024, 2, 1),
                20.0,
                8.5);

        index.escriu(0, primer);
        index.escriu(1, segon);

        long midaInicial = Files.size(fitxer);

        double resultat = index.sumaHores(1, 5.0);

        assertEquals(25.0, resultat, 0.001);
        assertEquals(10.0, index.llegeixHores(0), 0.001);
        assertEquals(25.0, index.llegeixHores(1), 0.001);
        assertEquals(midaInicial, Files.size(fitxer));
        assertEquals(2, index.nombreRegistres());
    }
}