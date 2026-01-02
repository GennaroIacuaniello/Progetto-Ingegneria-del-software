package frontend.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ControllerTMP {

    private static List<String> developers = new ArrayList<>();
    private static List<Integer> openIssues = new ArrayList<>();
    private static List<Integer> resolvedIssues = new ArrayList<>();
    private static List<Duration> averageResolvingDurations = new ArrayList<>();
    private static Duration totalAverageResolvingDuration = Duration.ZERO;

    public static void createReport(String month, String year) {

        /*
            todo:
            query che cerca email dei developer del tema in TeamController
            e per questi developers tenendo conto di mese e anno passati come parametri calcoli:
                - numero di issue aperte,
                - numero di issue risolte
                - tempo medio di risoluzione
            e li metta nelle rispettive list di questa classe. Inoltre calcolare il tempo medio di risoluzione aggregato.
            Se la ricerca non produce risultati lasciare le liste vuote ma non null, per totalAverageResolvingDuration impostare la minima durata possibile
         */
    }

    public static void createDashBoard() {

        /*
            todo:
            query che cerca email dei developer di tutto il sistema e per ognuno di essi calcoli:
                - numero di issue aperte,
                - numero di issue risolte
                - tempo medio di risoluzione
            e li metta nelle rispettive list di questa classe. Inoltre calcolare il tempo medio di risoluzione aggregato.
            Se la ricerca non produce risultati lasciare le liste vuote ma non null
         */

        developers = new ArrayList<>();
        openIssues = new ArrayList<>();
        resolvedIssues = new ArrayList<>();
        averageResolvingDurations = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            developers.add("Developer " + i);
            openIssues.add(i);
            resolvedIssues.add(i);
            averageResolvingDurations.add(Duration.ofDays(i).ofHours(i).ofMinutes(i));
        }

        totalAverageResolvingDuration = Duration.ofDays(0).ofHours(4).ofMinutes(2);
    }

    public static List<String> getDevelopers() {
        return developers;
    }

    public static List<Integer> getOpenIssues() {
        return openIssues;
    }

    public static List<Integer> getResolvedIssues() {
        return resolvedIssues;
    }

    public static List<Duration> getAverageResolvingDurations() {
        return averageResolvingDurations;
    }

    public static int getTotalOpenIssues() {

        int total = 0;

        for (Integer i : openIssues)
            total += i;

        return total;
    }

    public static int getTotalResolvedIssues() {

        int total = 0;

        for (Integer i : resolvedIssues)
            total += i;

        return total;
    }

    public static Duration getTotalAverageResolvingDuration() {
        return totalAverageResolvingDuration;
    }
}
