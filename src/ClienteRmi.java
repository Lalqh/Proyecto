import java.rmi.registry.Registry;

public class ClienteRmi {
    Registry rmii;
    DataRMI obj;

    public ClienteRmi() {
        try {
            rmii = java.rmi.registry.LocateRegistry.getRegistry
                    ("localhost", 5000);
            obj = (DataRMI) rmii.lookup("MergeSort");
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void enviar(char[] array) {
        try {
             obj.recibirArray(array);
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public char[] obtener() {
        try {
            return obj.regresarArray();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void limpiar() {
        try {
            obj.limpiar();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}