import java.util.*;

public class PriorityScheduler {

    public static SchedulerResult run(List<Process> original) {

        List<Process> procs = new ArrayList<>();
        for (Process p : original) procs.add(p.copy());

        int n = procs.size();
        int[] remaining = new int[n];
        for (int i = 0; i < n; i++) remaining[i] = procs.get(i).burstTime;

        List<GanttEntry> gantt = new ArrayList<>();
        int time = 0;
        int done = 0;

        while (done < n) {

            int chosen = -1;
            for (int i = 0; i < n; i++) {
                if (procs.get(i).arrivalTime <= time && remaining[i] > 0) {
                    if (chosen == -1) { chosen = i; continue; }
                    Process c = procs.get(chosen), ci = procs.get(i);
                    if (ci.priority < c.priority) { chosen = i; }
                    else if (ci.priority == c.priority) {
                        if (ci.arrivalTime < c.arrivalTime) { chosen = i; }
                        else if (ci.arrivalTime == c.arrivalTime && remaining[i] < remaining[chosen]) {
                            chosen = i;
                        }
                    }
                }
            }

            if (chosen == -1) { time++; continue; }

            if (!procs.get(chosen).responseSet) {
                procs.get(chosen).responseTime = time - procs.get(chosen).arrivalTime;
                procs.get(chosen).responseSet  = true;
            }

            remaining[chosen]--;
            time++;

            if (!gantt.isEmpty() && gantt.get(gantt.size()-1).pid.equals(procs.get(chosen).pid)
                    && gantt.get(gantt.size()-1).end == time - 1) {
                gantt.get(gantt.size()-1).end = time;
            } else {
                gantt.add(new GanttEntry(procs.get(chosen).pid, time-1, time));
            }

            if (remaining[chosen] == 0) {
                done++;
                procs.get(chosen).finishTime     = time;
                procs.get(chosen).turnaroundTime = time - procs.get(chosen).arrivalTime;
                procs.get(chosen).waitingTime    = procs.get(chosen).turnaroundTime
                                                - procs.get(chosen).burstTime;
            }
        }

        double avgWT = 0, avgTAT = 0, avgRT = 0;
        for (Process p : procs) { avgWT += p.waitingTime; avgTAT += p.turnaroundTime; avgRT += p.responseTime; }
        avgWT /= n; avgTAT /= n; avgRT /= n;

        return new SchedulerResult(gantt, procs, avgWT, avgTAT, avgRT);
    }
}
