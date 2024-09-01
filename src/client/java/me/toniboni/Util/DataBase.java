package me.toniboni.Util;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static Connection connection = null;

    public static void init() throws SQLException {
        if (connection == null){
            String path = new File("").getAbsolutePath();
            path += "\\ElytraHelper\\times.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        }
    }

    private static void createTimeTable() throws SQLException {
        if (connection == null) init();
        StringBuilder command = new StringBuilder("Create Table if not exists times (date timestamp");

        for (int i = 1; i < 17; i++) {
            command.append(",").append("ring").append(i).append(" smallint");
        }

        command.append(",total int);");

        Statement statement = connection.createStatement();
        statement.execute(command.toString());
    }

    public static void addTimes(List<Long> times) throws SQLException {
        if (times.size() == 17){
            createTimeTable();
            StringBuilder command = new StringBuilder("Insert into Times values (\"" + Timestamp.from(Instant.now()) + "\"");
            for (Long time : times) {
                command.append(",").append(time);
            }
            command.append(");");
            Statement statement = connection.createStatement();
            statement.execute(command.toString());
        }
        System.out.println("added");
    }

    public static List<Time> getTimes(SortType sort, boolean asc, int iteration) throws SQLException {
        createTimeTable();
        List<Time> times = new ArrayList<>(List.of());

        StringBuilder command = new StringBuilder("Select * from times order by ");

        command.append(sort.toString());
        if (asc) command.append(" asc limit ");
        else command.append(" desc limit ");
        command.append(iteration * 10).append(",10;");

        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(command.toString());

        while (set.next()){
            Time time = new Time();
            time.timestamp = set.getTimestamp("date");
            time.total = set.getLong("total");

            List<Long> rings = new ArrayList<>(List.of());
            for (int i = 1; i < 17; i++) {
                rings.add(set.getLong("ring" + i));
            }
            time.rings = rings;

            times.add(time);
        }

        return times;
    }

    public static Time getSumOfBest() throws SQLException {
        createTimeTable();
        Time time = new Time();
        List<Long> rings = new ArrayList<>(List.of());
        long total = 0L;
        for (int i = 1; i < 17; i++) {
            long best = getBestRing("ring" + i);
            total += best;
            rings.add(best);

        }
        time.rings = rings;
        time.total = total;
        time.timestamp = Timestamp.from(Instant.now());

        return time;
    }

    private static long getBestRing(String ring) throws SQLException {
        String command = "Select Min(" + ring + ") as min from times;";

        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(command);

        if (set.next()){
            return set.getLong("min");
        }else return -1L;
    }

    public static int getSize() throws SQLException {
        createTimeTable();
        String command = "Select * from times;";
        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(command);

        int size = 0;

        while (set.next()){
            size++;
        }

        return size;
    }
}
