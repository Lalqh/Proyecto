import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class Ventana extends JFrame {

    JTextArea textArea1, textArea2;
    JButton btnAgregar, btnLimpiar, btnOrdenar, btnFork, btnExecute;
    JLabel labelTiempo1, labelTiempo2, labelTiempo3, labelTotal;
    private char[] arreglo;
    private BusquedaSecuncial busquedaSecuencial;
    private BusquedaForkJoin busquedaFork;
    private BusquedaExecute busquedaExcute;
    private ClienteRmi clienteRmi;

    public Ventana() {
        this.setSize(600, 400);
        setTitle("Algoritmo MergeSort");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        busquedaSecuencial = new BusquedaSecuncial();
        busquedaExcute = new BusquedaExecute();
        clienteRmi = new ClienteRmi();

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        textArea1 = new JTextArea(10, 20);
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        textArea2 = new JTextArea(10, 20);
        JScrollPane scrollPane2 = new JScrollPane(textArea2);

        btnAgregar = new JButton("Agregar");
        btnLimpiar = new JButton("Limpiar");
        btnOrdenar = new JButton("Ordenar");
        btnFork = new JButton("Fork");
        btnExecute = new JButton("Execute");
        labelTiempo1 = new JLabel("Tiempo secuencial: ");
        labelTiempo2 = new JLabel("Tiempo forkJoin: ");
        labelTiempo3 = new JLabel("Tiempo execute: ");
        labelTotal = new JLabel("Total: 0");

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane1)
                        .addComponent(btnOrdenar)
                        .addComponent(labelTiempo1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane2)
                        .addComponent(btnFork)
                        .addComponent(labelTiempo2))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(btnAgregar)
                        .addComponent(btnExecute)
                        .addComponent(labelTiempo3))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(labelTotal))
                .addComponent(btnLimpiar));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(scrollPane1)
                        .addComponent(scrollPane2)
                        .addComponent(btnAgregar)
                        .addComponent(btnLimpiar))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(btnOrdenar)
                        .addComponent(btnFork)
                        .addComponent(btnExecute)
                        .addComponent(labelTotal))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelTiempo1)
                        .addComponent(labelTiempo2)
                        .addComponent(labelTiempo3)));

        getContentPane().add(panel);
        pack();

        textArea1.setEditable(false);
        textArea2.setEditable(false);

        agregarEventos();
    }

    private void agregarEventos() {
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num = Integer.parseInt(
                        JOptionPane.showInputDialog("Ingrese el numero de carecteres que desea generar"));

                arreglo = new char[num];

                for (int i = 0; i < num; i++) {
                    arreglo[i] = (char) (Math.random() * 26 + 'a');
                }

                clienteRmi.enviar(arreglo);

                for (char c : arreglo) {
                    textArea1.append(c + "\n");
                }
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
                textArea2.setText("");
                labelTiempo1.setText("Tiempo secuencial: ");
                labelTiempo2.setText("Tiempo forkJoin: ");
                labelTiempo3.setText("Tiempo execute: ");
                labelTotal.setText("Total: 0");
                arreglo = null;
            }
        });

        btnOrdenar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.setText("");
                char[] arreglo2 = clienteRmi.obtener();
                double startTime = System.nanoTime();
                char[] resultado = busquedaSecuencial.buscarVocal(arreglo2);
                labelTotal.setText("Total: " + resultado.length);
                double endTime = System.nanoTime();
                labelTiempo1.setText("Tiempo secuencial: " + (endTime - startTime) / 1_000_000 + " ms");

                for (char c : resultado) {
                    textArea2.append(c + "\n");
                }
            }
        });

        btnFork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.setText("");
                char[] arreglo2 = clienteRmi.obtener();
                busquedaFork = new BusquedaForkJoin(arreglo2, 0, arreglo2.length);
                long startTime = System.nanoTime();
                char[] resultado = busquedaFork.buscarVocalesParalelamente(arreglo2);
                long endTime = System.nanoTime();
                labelTotal.setText("Total : " + resultado.length);
                labelTiempo2.setText("Tiempo forkJoin: " + (endTime - startTime) / 1_000_000 + " ms");

                for (char c : resultado) {
                    textArea2.append(c + "\n");
                }


            }
        });

        btnExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                textArea2.setText("");

                char[] resultado;

                try {
                    char[] arregloRMI = clienteRmi.obtener();
                    long startTime = System.nanoTime();
                    resultado = busquedaExcute.buscarVocales(arregloRMI);
                    long endTime = System.nanoTime();
                    labelTotal.setText("Total: " + resultado.length);
                    labelTiempo3.setText("Tiempo execute: " + (endTime - startTime) / 1_000_000 + " ms");
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                }


                for (char c : resultado) {
                    textArea2.append(c + "\n");
                }

            }
        });

    }
}
