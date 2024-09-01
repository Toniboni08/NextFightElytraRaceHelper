package me.toniboni.Util;


import java.util.ArrayList;
import java.util.List;

public class Timer {
    private static List<Long> times = List.of(0L);
    private static List<Long> timesRaw = List.of(0L);

    public static void startTimer(){
        times = List.of(0L);
        timesRaw = List.of(System.currentTimeMillis());
        System.out.println("startTimer");
    }

    public static void addTime(){
        List<Long> time = new ArrayList<>(times);
        List<Long> timeRaw = new ArrayList<>(timesRaw);
        time.add((System.currentTimeMillis() - timeRaw.get(timeRaw.size() - 1)));

        timeRaw.add(System.currentTimeMillis());
        timesRaw = timeRaw;
        times = time;
        System.out.println("addTime");
    }

    public static List<Long> getTimes(){
        List<Long> time = new ArrayList<>();
        Long total = 0L;
        for (int i = 1; i < times.size(); i++) {
            time.add(times.get(i));
            total += times.get(i);
        }

        System.out.println("getTimes");

        time.add(total);

        return time;
    }
}
