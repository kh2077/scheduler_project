import java.util.List;

public class SchedulerResult {
    public List<GanttEntry>  gantt;
    public List<Process>     processes;
    public double            avgWT;
    public double            avgTAT;
    public double            avgRT;

    public SchedulerResult(List<GanttEntry> gantt, List<Process> processes,
                        double avgWT, double avgTAT, double avgRT) {
        this.gantt     = gantt;
        this.processes = processes;
        this.avgWT     = avgWT;
        this.avgTAT    = avgTAT;
        this.avgRT     = avgRT;
    }
}
