import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BusquedaExecute {
    private static final char[] VOCALES = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static char[] buscarVocales(char[] array) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<List<Character>>> futures = new ArrayList<>();

        // Dividir el array en chunks y enviar tareas al ExecutorService
        int chunkSize = (int) Math.ceil((double) array.length / NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, array.length);
            futures.add(executor.submit(new BuscarVocalesTask(array, start, end)));
        }

        // Recolectar resultados de las tareas
        List<Character> resultadoList = new ArrayList<>();
        for (Future<List<Character>> future : futures) {
            resultadoList.addAll(future.get());
        }

        // Apagar el ExecutorService
        executor.shutdown();

        // Convertir la lista de caracteres a un array de caracteres
        char[] resultado = new char[resultadoList.size()];
        for (int i = 0; i < resultadoList.size(); i++) {
            resultado[i] = resultadoList.get(i);
        }

        return resultado;
    }

    static class BuscarVocalesTask implements Callable<List<Character>> {
        private char[] array;
        private int start, end;

        public BuscarVocalesTask(char[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public List<Character> call() {
            List<Character> vocalesEncontradas = new ArrayList<>();
            for (int i = start; i < end; i++) {
                for (char vocal : VOCALES) {
                    if (array[i] == vocal) {
                        vocalesEncontradas.add(array[i]);
                        break;
                    }
                }
            }
            return vocalesEncontradas;
        }
    }
}
