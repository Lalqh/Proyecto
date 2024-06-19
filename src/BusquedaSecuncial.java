import java.util.ArrayList;
import java.util.List;

public class BusquedaSecuncial {

    public char[] buscarVocal(char[] array) {
        char[] vocales = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};
        List<Character> ordenamiento = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {

            for (int k = 0; k < vocales.length; k++) {
                if (array[i] == vocales[k]) {
                    boolean added = false;
                    for (int j = 0; j < ordenamiento.size(); j++) {
                        if (array[i] < ordenamiento.get(j)) {
                            ordenamiento.add(j, array[i]);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        ordenamiento.add(array[i]);
                    }
                    break;
                }
            }
        }


        char[] resultado = new char[ordenamiento.size()];
        for (int i = 0; i < ordenamiento.size(); i++) {
            resultado[i] = ordenamiento.get(i);
        }

        return resultado;
    }
}
