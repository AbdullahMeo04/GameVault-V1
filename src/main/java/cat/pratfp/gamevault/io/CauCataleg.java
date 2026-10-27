package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Cau binària del catàleg: desa la llista sencera amb serialització Java per
 * recuperar-la ràpidament.
 * Només la pot llegir Java: no és un format d'intercanvi.
 */
public class CauCataleg {

    /**
     * Desa la llista de jocs en un fitxer binari mitjançant serialització Java.
     *
     * @param p    ruta del fitxer de destinació
     * @param jocs llista de jocs que s'han de desar
     * @throws IOException si no es pot escriure el fitxer
     */
    public void desa(Path p, List<Joc> jocs) throws IOException {
        try (ObjectOutputStream sortida = new ObjectOutputStream(
                Files.newOutputStream(p))) {
            sortida.writeObject(new ArrayList<>(jocs));
        }
    }

    /**
     * Recupera el catàleg desat amb {@link #desa(Path, List)}.
     *
     * @param p fitxer .ser
     * @return la llista de jocs recuperada
     * @throws IOException si el fitxer no existeix, està corromput o la classe no
     *                     es troba
     */
    @SuppressWarnings("unchecked")
    public List<Joc> carrega(Path p) throws IOException {
        try (ObjectInputStream entrada = new ObjectInputStream(
                Files.newInputStream(p))) {

            Object objecte = entrada.readObject();

            if (!(objecte instanceof List<?> llista)) {
                throw new IOException(
                        "El fitxer no conté una llista de jocs: " + p);
            }

            for (Object element : llista) {
                if (!(element instanceof Joc)) {
                    throw new IOException(
                            "La llista conté un objecte que no és un joc: " + p);
                }
            }

            return (List<Joc>) llista;

        } catch (ClassNotFoundException e) {
            throw new IOException(
                    "No s'ha pogut trobar una classe necessària per llegir el catàleg: "
                            + p,
                    e);
        }
    }
}
