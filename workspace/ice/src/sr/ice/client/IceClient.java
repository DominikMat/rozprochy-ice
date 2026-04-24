package sr.ice.client;

import DynamicApp.BirthdayCheckPrx;
import DynamicApp.Employee;
import com.zeroc.Ice.*;

import java.io.IOException;
import java.lang.Exception;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class IceClient {
	public static void main(String[] args) {
		int status = 0;
		Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE
			communicator = Util.initialize(args);

			// 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym (wówczas aplikację należy uruchomić z argumentem --Ice.config=config.client)
			ObjectPrx base1 = communicator.propertyToProxy("BirthdayCheck1.Proxy");
            if(base1 == null) { //powyższa opcja się nie uda, gdy nie był wskazany plik konfiguracyjny (--Ice.Config=client.config)
                // 2. Uzyskanie referencji obiektu - to samo co powyżej, ale mniej ładnie
                System.out.println("(using a hard-coded configuration)");
                base1 = communicator.stringToProxy("birthdayCheck/birthdayCheck1:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
            }

			// 3. Rzutowanie, zawężanie (do typu BirthdayCheck)
			BirthdayCheckPrx obj1 = BirthdayCheckPrx.checkedCast(base1);
			if (obj1 == null) throw new Error("Invalid proxy");

			CompletableFuture<Long> cfl = null;
			String line = null;
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

			// test employee data
			Employee testEmployee = new Employee();
			testEmployee.name = "Michał Kowalski";
			testEmployee.birthday = "2000-01-01";
			testEmployee.age = 26;

			do {
				try {
					System.out.print("==> ");
					line = in.readLine();
					switch (line) {
						case "a": {
							boolean result = obj1.isTodayEmployeeBirthday(testEmployee);
							System.out.println("RESULT = " + result);
							break;
						}
						case "b": {
							int result = obj1.predictNextBirthdayAge(new int[] {20,21,22,23,24,25,26} );
							System.out.println("RESULT = " + result);
							break;
						}
						case "c": {
							String result = obj1.getBirthdayWishes(testEmployee.name);
							System.out.println("RESULT = " + result);
							break;
						}
						default:
							System.out.println("???");
					}
				} catch (IOException | TwowayOnlyException ex) {
					ex.printStackTrace(System.err);
				}
			}
			while (!Objects.equals(line, "x"));


		} catch (LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (communicator != null) { //clean
			try {
				communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}

}