package org.elsys.ip.servlet.timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class TimerServlet extends HttpServlet {

    private final Map<String, String> startMap = new HashMap<>();
    private final Map<String, List<String>> lapMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (pathArgs.get(0).equals("stopwatch")) {
            if (startMap.containsKey(pathArgs.get(1))) {
                resp.getWriter().println(timer(pathArgs));
            } else {
                resp.setStatus(404);
            }
        }
    }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());
            if (pathArgs.get(0).equals("stopwatch") && pathArgs.get(1).equals("start") && pathArgs.size() == 2) {
                Format f = new SimpleDateFormat("HH:mm:ss");
                String startTime = f.format(new Date());

                String ID = UUID.randomUUID().toString();
                startMap.put(ID, startTime);
                resp.getWriter().println(ID);
                resp.setStatus(201);

            } else {
                resp.setStatus(400);
            }
        }

        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

            if (pathArgs.get(0).equals("stopwatch") && pathArgs.get(2).equals("lap") && pathArgs.size() == 3) {
                if (startMap.containsKey(pathArgs.get(1))) {
                    String curr_time = timer(pathArgs);

                    if (!lapMap.containsKey(pathArgs.get(1))) {
                        lapMap.put(pathArgs.get(1), new ArrayList<>());

                    } else {
                        for (String time : lapMap.get(pathArgs.get(1))) {
                            String[] splited = time.split(":");

                            if (!splited[0].equals("00"))
                                curr_time = stringTime(curr_time, 1, Integer.parseInt(splited[0]), '-');
                            if (!splited[1].equals("00"))
                                curr_time = stringTime(curr_time, 2, Integer.parseInt(splited[1]), '-');
                            if (!splited[2].equals("00"))
                                curr_time = stringTime(curr_time, 3, Integer.parseInt(splited[2]), '-');
                        }

                    }
                    resp.getWriter().println(curr_time);
                    lapMap.get(pathArgs.get(1)).add(curr_time);


                } else {
                    resp.setStatus(404);
                }
            } else {
                resp.setStatus(400);
            }

        }
        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());
            int i = 1;
            String sum = "00:00:00";
            if (pathArgs.get(0).equals("stopwatch") && pathArgs.size() == 2) {
                if (lapMap.containsKey(pathArgs.get(1))) {
                    String curr_time = timer(pathArgs);


                    for (Map.Entry<String, List<String>> entry : lapMap.entrySet()) {
                        if (entry.getKey().equals(pathArgs.get(1))) {
                            for (String time : lapMap.get(pathArgs.get(1))) {
                                // laps_time += time
                                String[] splited = time.split(":");

                                if (!splited[0].equals("00"))
                                    curr_time = stringTime(curr_time, 1, Integer.parseInt(splited[0]), '-');
                                if (!splited[1].equals("00"))
                                    curr_time = stringTime(curr_time, 2, Integer.parseInt(splited[1]), '-');
                                if (!splited[2].equals("00"))
                                    curr_time = stringTime(curr_time, 3, Integer.parseInt(splited[2]), '-');
                            }

                            lapMap.get(pathArgs.get(1)).add(curr_time);
                            for (String laps : entry.getValue()) {
                                String[] splited = laps.split(":");

                                if (!splited[0].equals("00"))
                                    sum = stringTime(sum, 1, Integer.parseInt(splited[0]), '+');
                                if (!splited[1].equals("00"))
                                    sum = stringTime(sum, 2, Integer.parseInt(splited[1]), '+');
                                if (!splited[2].equals("00"))
                                    sum = stringTime(sum, 3, Integer.parseInt(splited[2]), '+');

                                if (i < 9) resp.getWriter().print("0" + i + " ");
                                else resp.getWriter().print(i + " ");

                                resp.getWriter().println(laps + " / " + sum);
                                i++;

                            }
                        }
                    }
                    startMap.remove(pathArgs.get(1));
                    lapMap.remove(pathArgs.get(1));
                } else if (startMap.containsKey(pathArgs.get(1))) {
                    String time = timer(pathArgs);
                    resp.getWriter().println("01 " + time + " / " + time);
                    startMap.remove(pathArgs.get(1));
                } else {
                    resp.setStatus(404);
                }

            } else {
                resp.setStatus(400);
            }
        }


        private static String stringTime(String myTime, int addTo, int amount, char operation) {
            // addTo = 1 for Hours
            // addTo = 2 for Mins
            // addTo = 3 for Seconds

            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            Date d = null;
            try {
                d = df.parse(myTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();

            assert d != null;
            cal.setTime(d);
            if (operation == '+') {
                if (addTo == 1) {
                    cal.add(Calendar.HOUR, amount);
                } else if (addTo == 2) {
                    cal.add(Calendar.MINUTE, amount);
                } else if (addTo == 3) {
                    cal.add(Calendar.SECOND, amount);
                }
            } else if (operation == '-') {
                if (addTo == 1) {
                    cal.add(Calendar.HOUR, -amount);
                } else if (addTo == 2) {
                    cal.add(Calendar.MINUTE, -amount);
                } else if (addTo == 3) {
                    cal.add(Calendar.SECOND, -amount);
                }
            }
            Date testDate = cal.getTime();
            return df.format(testDate);

        }

        private String timer(List<String> pathArgs) {
            for (Map.Entry<String, String> entry : startMap.entrySet()) {
                if (entry.getKey().equals(pathArgs.get(1))) {
                    Format f = new SimpleDateFormat("HH:mm:ss");
                    String strResult = f.format(new Date());
                    // resp.getWriter().println("Time now = "+ strResult);
                    String pastTime = entry.getValue();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = format.parse(pastTime);
                        date2 = format.parse(strResult);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    long res = date2.getTime() - date1.getTime();

                    long diffSeconds = res / 1000 % 60;
                    long diffMinutes = res / (60 * 1000) % 60;
                    long diffHours = res / (60 * 60 * 1000) % 24;


                    String hoursString = "0";
                    String minutesString = "0";
                    String secsString = "0";
                    if (diffHours < 10) hoursString += diffHours;
                    else hoursString = Long.toString(diffHours);

                    if (diffMinutes < 10) minutesString += diffHours;
                    else minutesString = Long.toString(diffMinutes);

                    if (diffSeconds < 10) secsString += diffSeconds;
                    else secsString = Long.toString(diffSeconds);

                    return hoursString + ":" + minutesString + ":" + secsString;

                }
            }
            return null;
        }
}
