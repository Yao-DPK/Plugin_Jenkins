package io.jenkins.plugins;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxDBData {

  static ValuesEnergetic<Double, Double> processInfluxDBData(
    String time,
    String host,
    String port,
    String database,
    String username,
    String password
  ) {
    return processInfluxDBData(
      time,
      host,
      port,
      database,
      username,
      password,
      null,
      null
    );
  }

  static ValuesEnergetic<Double, Double> processInfluxDBData(
    String time,
    String host,
    String port,
    String database,
    String username,
    String password,
    Long stage_start,
    Long stage_end
  ) {
    Instant specifiedTime;
    try {
      specifiedTime = Instant.parse(time);
    } catch (DateTimeParseException e) {
      System.err.println(
        "Invalid time format. Please use the format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
      );
      return new ValuesEnergetic<>(0.0, 0.0); // Return early if time format is invalid
    }

    // Connect to InfluxDB instance
    String serverURL = "http://" + host + ":" + port;
    InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

    // Use the database
    influxDB.setDatabase(database);

    // Define and execute the query
    String queryStr =
      "SELECT time, power FROM \"power_consumption\" GROUP BY target";
    Query query = new Query(queryStr, database);
    QueryResult queryResult = influxDB.query(query);

    if (queryResult.hasError()) {
      System.err.println("Error executing query: " + queryResult.getError());
      influxDB.close();
      return new ValuesEnergetic<>(0.0, 0.0);
    }

    // Process the results
    List<QueryResult.Result> results = queryResult.getResults();
    if (results.isEmpty()) {
      System.out.println("No results found for the query.");
      influxDB.close();
      return new ValuesEnergetic<>(0.0, 0.0);
    }

    Map<String, Long> targetFirstEntryTimes = new HashMap<>();
    Map<String, List<Map.Entry<Long, Double>>> targetPowerValues = new HashMap<>();

    for (QueryResult.Result result : results) {
      if (result.getSeries() != null) {
        for (QueryResult.Series series : result.getSeries()) {
          String target = series.getTags().get("target");
          for (List<Object> value : series.getValues()) {
            long currentTime = parseTime(value.get(0));
            if (!targetFirstEntryTimes.containsKey(target)) {
              targetFirstEntryTimes.put(target, currentTime);
              targetPowerValues.put(target, new ArrayList<>());
            }
            double power = parseDouble(value.get(1));
            targetPowerValues
              .get(target)
              .add(new AbstractMap.SimpleEntry<>(currentTime, power));
          }
        }
      }
    }

    // Print out the targets and their first entry times
    System.out.println("Targets and their first entry times:");
    for (Map.Entry<String, Long> entry : targetFirstEntryTimes.entrySet()) {
      System.out.println(
        "Target: " +
        entry.getKey() +
        ", First Entry Time: " +
        Instant.ofEpochMilli(entry.getValue()) +
        "\n"
      );
    }

    // Find the target with the first entry time closest after the specified time
    String closestTarget = null;
    long closestTime = Long.MAX_VALUE;

    for (Map.Entry<String, Long> entry : targetFirstEntryTimes.entrySet()) {
      long entryTime = entry.getValue();
      if (
        entryTime >= specifiedTime.toEpochMilli() && entryTime < closestTime
      ) {
        closestTime = entryTime;
        closestTarget = entry.getKey();
      }
    }

    // Print out the closest target
    if (closestTarget == null) {
      System.out.println(
        "No targets found with the first entry added after the specified time."
      );
      influxDB.close();
      return new ValuesEnergetic<>(0.0, 0.0);
    } else {
      System.out.println(
        "Target with the closest first entry added after the specified time: " +
        closestTarget
      );

      // Display the list of power values and their times for the closest target
      List<Map.Entry<Long, Double>> powerValues = targetPowerValues.get(
        closestTarget
      );
      System.out.println(
        "Power values and their times for the closest target (" +
        closestTarget +
        "):"
      );
      for (Map.Entry<Long, Double> entry : powerValues) {
        System.out.println(
          "Time: " +
          Instant.ofEpochMilli(entry.getKey()) +
          ", Power: " +
          entry.getValue() +
          " W"
        );
      }

      // Apply stage_start and stage_end if provided
      long firstEntryTime = powerValues.get(0).getKey();
      long startTime = (stage_start != null)
        ? firstEntryTime + stage_start * 1000
        : firstEntryTime;
      long endTime = (stage_end != null)
        ? firstEntryTime + stage_end * 1000
        : powerValues.get(powerValues.size() - 1).getKey();

      // Filter power values within the specified time range
      List<Map.Entry<Long, Double>> filteredPowerValues = new ArrayList<>();
      for (Map.Entry<Long, Double> entry : powerValues) {
        if (entry.getKey() >= startTime && entry.getKey() <= endTime) {
          filteredPowerValues.add(entry);
        }
      }

      if (filteredPowerValues.isEmpty()) {
        System.out.println(
          "No power values found within the specified stage time."
        );
        influxDB.close();
        return new ValuesEnergetic<>(0.0, 0.0);
      }

      // Calculate the sum of power and the duration for the filtered values
      double sumPower = 0.0;
      for (Map.Entry<Long, Double> entry : filteredPowerValues) {
        sumPower += entry.getValue();
      }
      double meanPower = sumPower / filteredPowerValues.size();
      long filteredDurationMillis = endTime - startTime;

      // Calculate energy in watt-seconds (Joules)
      double energy = meanPower * (filteredDurationMillis / 1000.0); // convert duration to seconds

      System.out.println(
        "Sum of power for the closest target (" +
        closestTarget +
        "): " +
        meanPower +
        " W"
      );
      System.out.println(
        "Energy for the closest target (" +
        closestTarget +
        "): " +
        energy +
        " J"
      );
      influxDB.close();

      return new ValuesEnergetic<>(energy, meanPower);
    }
  }

  private static long parseTime(Object value) {
    if (value instanceof String) {
      try {
        Instant instant = Instant.parse((String) value);
        return instant.toEpochMilli();
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException(
          "Unexpected time value format: " + value,
          e
        );
      }
    } else if (value instanceof Number) {
      return ((Number) value).longValue();
    } else {
      throw new IllegalArgumentException(
        "Unexpected time value type: " + value.getClass().getName()
      );
    }
  }

  private static double parseDouble(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    } else if (value instanceof String) {
      return Double.parseDouble((String) value);
    } else {
      throw new IllegalArgumentException(
        "Unexpected number value type: " + value.getClass().getName()
      );
    }
  }
}
