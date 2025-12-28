package frontend.controller;

public class PriorityConverter {

    public static String intToString(int priority) {

        return switch (priority) {
            case 0 -> "Molto Bassa";
            case 1 -> "Bassa";
            case 2 -> "Media";
            case 3 -> "Alta";
            case 4 -> "Molto Alta";
            default -> "Errore";
        };
    }

    public static int stringToInt(String priority) {

        return switch (priority) {
            case "Molto Bassa" -> 0;
            case "Bassa" -> 1;
            case "Media" -> 2;
            case "Alta" -> 3;
            case "Molto Alta" -> 4;
            default -> -1;
        };
    }

}
