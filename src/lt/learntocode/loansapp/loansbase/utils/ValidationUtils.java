package lt.learntocode.loansapp.loansbase.utils;

public class ValidationUtils {

    public static boolean intInputRange(int input, int from, int to) {
        boolean inputIsValid = input >= from && input <= to;
        if (inputIsValid) {
            return true;
        } else {
            System.err.printf("Neteisingai įvesta reikšmė, galimos reikšmės yra nuo %d iki %d\n", from, to);
            return false;
        }
    }

    public static boolean doubleInputRange(double input, double from, double to) {
        boolean inputIsValid = input >= from && input <= to;
        if (inputIsValid) {
            return true;
        } else {
            System.err.printf("Neteisingai įvesta reikšmė, galimos reikšmės yra nuo %.2f iki %.2f\n", from, to);
            return false;
        }
    }

    public static boolean stringInputLength(String input, int minWords, int maxLength) {
        int wordsCounter = wordsCounter(input);
        if (input.contains(",")) {
            System.err.print("Įvesti duomenis negali turėti skirybos simbolių\n");
            return false;
        } else if (wordsCounter < minWords) {
            System.err.printf("Įvesti duomenis turi būti bent iš %d žodžių\n", minWords);
            return false;
        } else if (input.length() > maxLength) {
            System.err.printf("Įvesti duomenis negali turėti daugiau nei %d simbolių\n", maxLength);
            return false;
        } else {
            return true;
        }
    }

    private static int wordsCounter(String input) {
        int wordsCounter = 1;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                wordsCounter++;
            }
        }
        return wordsCounter;
    }

    public static boolean charKeyInput(char key, char[] validKeyOptions) {
        for (int i = 0; i < validKeyOptions.length; i++) {
            if (key == validKeyOptions[i]){
                return true;
            }
        }
        System.err.printf("%s - Nėra tokio pasirinkimo, pabandykite dar kartą\n", ((Character) key).toString());
        return false;
    }
}
