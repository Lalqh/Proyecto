import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class BusquedaForkJoin  extends RecursiveTask<List<Character>> {
    private static final int THRESHOLD = 1000;
    private char[] array;
    private int start, end;
    private static final char[] VOCALES = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

    public BusquedaForkJoin(char[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<Character> compute() {
        if (end - start <= THRESHOLD) {
            return buscarVocalesSecuencialmente(array, start, end);
        } else {
            int mid = (start + end) / 2;
            BusquedaForkJoin leftTask = new BusquedaForkJoin(array, start, mid);
            BusquedaForkJoin rightTask = new BusquedaForkJoin(array, mid, end);
            leftTask.fork();
            List<Character> rightResult = rightTask.compute();
            List<Character> leftResult = leftTask.join();
            leftResult.addAll(rightResult);
            return leftResult;
        }
    }

    private List<Character> buscarVocalesSecuencialmente(char[] array, int start, int end) {
        List<Character> vocalesEncontradas = new ArrayList<>();
        for (int i = start; i < end; i++) {
            for (char vocal : VOCALES) {
                if (array[i] == vocal) {
                    int pos = 0;
                    while (pos < vocalesEncontradas.size() && vocalesEncontradas.get(pos) < array[i]) {
                        pos++;
                    }
                    vocalesEncontradas.add(pos, array[i]);
                    break;
                }
            }
        }
        return vocalesEncontradas;
    }

    public  char[] buscarVocalesParalelamente(char[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        BusquedaForkJoin tareaPrincipal = new BusquedaForkJoin(array, 0, array.length);
        List<Character> resultadoList = pool.invoke(tareaPrincipal);

        char[] resultado = new char[resultadoList.size()];
        for (int i = 0; i < resultadoList.size(); i++) {
            resultado[i] = resultadoList.get(i);
        }
        return resultado;
    }

}
