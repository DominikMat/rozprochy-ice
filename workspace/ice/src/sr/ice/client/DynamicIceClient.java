package sr.ice.client;

import DynamicApp.Employee;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Exception;
import com.zeroc.Ice.Object;

import java.io.IOException;
import java.util.Objects;

public class DynamicIceClient {

    /* pakowanie argumentów do strumienia */
    static byte[] encapsulateStringData(String str, Communicator communicator) {
        OutputStream out = new OutputStream(communicator);
        out.startEncapsulation();
        out.writeString(str);
        out.endEncapsulation();
        return out.finished();
    }
    static byte[] encapsulateEmployeeData(Employee emp, Communicator communicator) {
        OutputStream out = new OutputStream(communicator);
        out.startEncapsulation();
        out.writeString(emp.name);
        out.writeString(emp.birthday);
        out.writeInt(emp.age);
        out.endEncapsulation();
        return out.finished();
    }
    static byte[] encapsulateIntSequenceData(int[] numbers, Communicator communicator) {
        OutputStream out = new OutputStream(communicator);
        out.startEncapsulation();
        out.writeIntSeq(numbers);
        out.endEncapsulation();
        return out.finished();
    }

    /* rozpakowanie wyniku ze strumienia */
    static void decapsulateStringData(Object.Ice_invokeResult  result, Communicator communicator) {
        if (result.returnValue) {
            InputStream in = new InputStream(communicator, result.outParams);
            in.startEncapsulation();
            String response = in.readString();
            in.endEncapsulation();

            System.out.println("Wynik: " + response);
        } else {
            System.err.println("Blad serwera");
        }
    }
    static void decapsulateBooleanData(Object.Ice_invokeResult result, Communicator communicator) {
        if (result.returnValue) {
            InputStream in = new InputStream(communicator, result.outParams);
            in.startEncapsulation();
            boolean response = in.readBool();
            in.endEncapsulation();

            System.out.println("Wynik: " + response);
        } else {
            System.err.println("Blad serwera");
        }
    }
    static void decapsulateIntData(Object.Ice_invokeResult result, Communicator communicator) {
        if (result.returnValue) {
            InputStream in = new InputStream(communicator, result.outParams);
            in.startEncapsulation();
            int response = in.readInt();
            in.endEncapsulation();
            System.out.println("Wynik: " + response);
        } else {
            System.err.println("Blad serwera");
        }
    }

    public static void main(String[] args) {
        // 1. Inicjalizacja komunikatora
        try (Communicator communicator = Util.initialize(args)) {
            
            // Uzyskanie bazowego proxy
            ObjectPrx base = communicator.stringToProxy("birthdayCheck/birthdayCheck1:tcp -h 127.0.0.2 -p 10000");

            // reader konsoli
            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

            // test employee data
            Employee testEmployee1 = new Employee();
            testEmployee1.name = "Michał Kowalski";
            testEmployee1.birthday = "2000-01-01";
            testEmployee1.age = 26;

            Employee testEmployee2 = new Employee();
            testEmployee2.name = "Julia Nowak";
            java.time.LocalDate today = java.time.LocalDate.now();
            testEmployee2.birthday = today.toString();
            testEmployee2.age = 24;

            System.out.println("type 'help' for available commands");
            do {
                try {
                    System.out.print("==> ");
                    line = in.readLine();

                    switch (line) {

                        /* HELP */
                        case "help": {
                            System.out.println("AVAILABLE COMMANDS:");
                            System.out.println("- 'isTodayBirthdayA' - check if today is the birthday of person A (expected NO)");
                            System.out.println("- 'isTodayBirthdayB' - check if today is the birthday of person B (expected YES)");
                            System.out.println("- 'getBirthdayWishesA' - get birthday wishes for person A");
                            System.out.println("- 'getBirthdayWishesB' - get birthday wishes for person A");
                            System.out.println("- 'predictNextAgeA' - predict the next age of person A");
                            System.out.println("- 'predictNextAgeB' - predict the next age of person B");
                            System.out.println();
                            break;
                        }

                        /* IS TODAY EMPLOYEE BIRTHDAY CHECK */
                        case "isTodayBirthdayA": {
                            System.out.println("Wysyłam dynamiczne żądanie isTodayEmployeeBirthday (osoba A, expected NO) ...");
                            var inParams = encapsulateEmployeeData(testEmployee1, communicator);
                            var result = base.ice_invoke("isTodayEmployeeBirthday", OperationMode.Normal, inParams);
                            System.out.println("Osoba " + testEmployee1.name + ", ma urodziny: " + testEmployee1.birthday);
                            decapsulateBooleanData(result, communicator);
                            break;
                        }
                        case "isTodayBirthdayB": {
                            System.out.println("Wysyłam dynamiczne żądanie isTodayEmployeeBirthday (osoba B, expected YES) ...");
                            var inParams = encapsulateEmployeeData(testEmployee2, communicator);
                            var result = base.ice_invoke("isTodayEmployeeBirthday", OperationMode.Normal, inParams);
                            System.out.println("Osoba " + testEmployee2.name + ", ma urodziny: " + testEmployee2.birthday);
                            decapsulateBooleanData(result, communicator);
                            break;
                        }

                        /* BIRTHDAY WISHES CHECK */
                        case "getBirthdayWishesA": {
                            System.out.println("Wysyłam dynamiczne żądanie getBirthdayWishes (osoba A) ...");
                            var inParams = encapsulateStringData(testEmployee1.name, communicator);
                            var result = base.ice_invoke("getBirthdayWishes", OperationMode.Normal, inParams);
                            decapsulateStringData(result, communicator);
                            break;
                        }
                        case "getBirthdayWishesB": {
                            System.out.println("Wysyłam dynamiczne żądanie getBirthdayWishes (osoba B) ...");
                            var inParams = encapsulateStringData(testEmployee2.name, communicator);
                            var result = base.ice_invoke("getBirthdayWishes", OperationMode.Normal, inParams);
                            decapsulateStringData(result, communicator);
                            break;
                        }

                        /* AGE PREDICTION CHECK */
                        case "predictNextAgeA": {
                            System.out.println("Wysyłam dynamiczne żądanie predictNextBirthdayAge (osoba A - 24,25,26, ???) ...");
                            var inParams = encapsulateIntSequenceData(new int[]{24,25,26}, communicator);
                            var result = base.ice_invoke("predictNextBirthdayAge", OperationMode.Normal, inParams);
                            decapsulateIntData(result, communicator);
                            break;
                        }
                        case "predictNextAgeB": {
                            System.out.println("Wysyłam dynamiczne żądanie predictNextBirthdayAge (osoba B - 18,19,20,21,22, ??? ) ...");
                            var inParams = encapsulateIntSequenceData(new int[]{18,19,20,21,22}, communicator);
                            var result = base.ice_invoke("predictNextBirthdayAge", OperationMode.Normal, inParams);
                            decapsulateIntData(result, communicator);
                            break;
                        }

                        default:
                            System.out.println("Nieznana komenda");
                    }
                } catch (IOException | TwowayOnlyException ex) {
                    ex.printStackTrace(System.err);
                }
            }
            while (!Objects.equals(line, "x"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}