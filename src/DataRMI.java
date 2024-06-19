import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataRMI extends Remote {
    void recibirArray(char[] array) throws RemoteException;
    char[] regresarArray() throws RemoteException;
    void limpiar() throws RemoteException;
}
