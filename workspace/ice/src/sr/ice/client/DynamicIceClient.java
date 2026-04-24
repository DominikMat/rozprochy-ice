package sr.ice.client;

import com.zeroc.Ice.*;

public class DynamicIceClient {
    public static void main(String[] args) {
        // 1. Inicjalizacja komunikatora
        try (Communicator communicator = Util.initialize(args)) {
            
            // 2. Uzyskanie bazowego proxy (dokładnie tak jak w Twoim kodzie)
            ObjectPrx base = communicator.stringToProxy("birthdayCheck/birthdayCheck1:tcp -h 127.0.0.2 -p 10000");

            // 3. Przygotowanie strumienia wyjściowego (pakowanie argumentów)
            OutputStream out = new OutputStream(communicator);
            out.startEncapsulation();
            out.writeString("Michał"); // Argument dla getBirthdayWishes
            out.endEncapsulation();
            byte[] inParams = out.finished();

            // 4. Wywołanie dynamiczne
            System.out.println("Wysyłam żądanie getBirthdayWishes (Dynamic)...");
            
            // ice_invoke zwraca obiekt InvokeResult, który zawiera status (returnValue) i bajty odpowiedzi
            ObjectPrx.Ice_invokeResult result = base.ice_invoke("getBirthdayWishes", OperationMode.Normal, inParams);

            // 5. Rozpakowanie wyniku
            if (result.returnValue) {
                // result.returnValue == true oznacza sukces (brak wyjątku)
                InputStream in = new InputStream(communicator, result.outParams);
                in.startEncapsulation();
                String response = in.readString(); // Odczytujemy wynik typu string
                in.endEncapsulation();
                
                System.out.println("Wynik z serwera: " + response);
            } else {
                // Serwer rzucił wyjątek użytkownika (UserException)
                System.err.println("Serwer zwrócił błąd (UserException).");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}