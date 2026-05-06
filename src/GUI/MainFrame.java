import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private static final Color BG      = new Color(0x12151F);
    private static final Color SURFACE = new Color(0x1A1E2E);
    private static final Color BORDER  = new Color(0x2C3050);
    private static final Color ACCENT  = new Color(0x4A7FC1);
    private static final Color FG      = new Color(0x708090);
    private static final Color GOLD    = new Color(0xD4AA50);
    private static final Color ERR     = new Color(0xC05050);

    private final JTextField fPid      = field(6);
    private final JTextField fArrival  = field(5);
    private final JTextField fBurst    = field(5);
    private final JTextField fPriority = field(5);
    private final JLabel     errorLbl  = new JLabel(" ");

    private final List<Process>          processList = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    private final GanttPanel        ganttPri  = new GanttPanel();
    private final GanttPanel        ganttSRTF = new GanttPanel();
    private final ResultsTablePanel resPri    = new ResultsTablePanel();
    private final ResultsTablePanel resSRTF   = new ResultsTablePanel();
    private final JTextArea         txtComp   = textArea();
    private final JTextArea         txtConc   = textArea();

    private JTabbedPane tabs;

    public MainFrame() {
        super("Priority vs SRTF — Scheduling Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 780);
        setMinimumSize(new Dimension(900, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        tabs = buildTabs();
        add(tabs, BorderLayout.CENTER);
    }

    private JTabbedPane buildTabs() {
        JTabbedPane t = new JTabbedPane();
        t.setBackground(SURFACE); t.setForeground(FG);
        t.setFont(new Font("SansSerif", Font.BOLD, 13));
        t.addTab("① Input",       inputTab());
        t.addTab("② Gantt Charts", ganttTab());
        t.addTab("③ Results",     resultsTab());
        t.addTab("④ Analysis",    analysisTab());
        return t;
    }

    private JPanel inputTab() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = card("Add Process  (Priority: 1 = highest)");
        form.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        form.add(lbl("PID:"));      form.add(fPid);
        form.add(lbl("Arrival:"));  form.add(fArrival);
        form.add(lbl("Burst:"));    form.add(fBurst);
        form.add(lbl("Priority:")); form.add(fPriority);
        JButton btnAdd = btn("Add", ACCENT);
        btnAdd.addActionListener(e -> addProcess());
        form.add(btnAdd);
        errorLbl.setForeground(ERR);
        errorLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        form.add(errorLbl);

        JPanel scen = card("Load Scenario");
        scen.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));
        scen.add(muted("Quick load:"));
        for (char c : new char[]{'A','B','C','D'}) {
            JButton b = btn("Scenario " + c, SURFACE);
            b.setForeground(GOLD); final char fc = c;
            b.addActionListener(e -> loadScenario(fc));
            scen.add(b);
        }

        JPanel top = new JPanel(); top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false); top.add(form); top.add(Box.createVerticalStrut(8)); top.add(scen);

        JList<String> list = new JList<>(listModel);
        list.setBackground(new Color(0x20253A)); list.setForeground(FG);
        list.setFont(new Font("Courier New", Font.PLAIN, 13));
        list.setFixedCellHeight(26);
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { int i = list.getSelectedIndex(); if (i >= 0) { processList.remove(i); listModel.remove(i); } }
            }
        });
        JPanel listCard = card("Process Table  (double-click row to remove)");
        listCard.setLayout(new BorderLayout());
        listCard.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton btnRun   = btn("▶  Run Simulation", new Color(0x2A5030));
        JButton btnClear = btn("✕  Clear All",      new Color(0x502A2A));
        btnRun.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRun.setPreferredSize(new Dimension(190, 38));
        btnRun.addActionListener(e -> runSimulation());
        btnClear.addActionListener(e -> clearAll());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        bot.setOpaque(false); bot.add(btnRun); bot.add(btnClear);

        root.add(top,      BorderLayout.NORTH);
        root.add(listCard, BorderLayout.CENTER);
        root.add(bot,      BorderLayout.SOUTH);
        return root;
    }
    
    private JPanel ganttTab() {
        JPanel root = new JPanel(new GridLayout(2, 1, 0, 12));
        root.setBackground(BG); root.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        root.add(scrollCard("Gantt — Priority Scheduling", ganttPri));
        root.add(scrollCard("Gantt — SRTF",                ganttSRTF));
        return root;
    }

    private JPanel resultsTab() {
        JPanel root = new JPanel(new GridLayout(2, 1, 0, 12));
        root.setBackground(BG); root.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        JPanel c1 = card("Results — Priority Scheduling  (WT / TAT / RT)");
        c1.setLayout(new BorderLayout()); c1.add(resPri, BorderLayout.CENTER);
        JPanel c2 = card("Results — SRTF  (WT / TAT / RT)");
        c2.setLayout(new BorderLayout()); c2.add(resSRTF, BorderLayout.CENTER);
        root.add(c1); root.add(c2);
        return root;
    }

    private JPanel analysisTab() {
        JPanel root = new JPanel(new GridLayout(2, 1, 0, 12));
        root.setBackground(BG); root.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        JPanel c1 = card("Comparison Summary");
        c1.setLayout(new BorderLayout()); c1.add(new JScrollPane(txtComp), BorderLayout.CENTER);
        JPanel c2 = card("Final Conclusion");
        c2.setLayout(new BorderLayout()); c2.add(new JScrollPane(txtConc), BorderLayout.CENTER);
        root.add(c1); root.add(c2);
        return root;
    }

    private void addProcess() {
        errorLbl.setText(" ");
        String pid = fPid.getText().trim(), arr = fArrival.getText().trim(),
            bst = fBurst.getText().trim(), pri = fPriority.getText().trim();
        if (pid.isEmpty())                        { error("PID cannot be empty.");              return; }
        if (pid.length() > 8)                     { error("PID too long (max 8 chars).");       return; }
        if (pid.matches("\\d+"))           {error("PID cannot be number.");              return; }
        for (Process p : processList)
            if (p.pid.equalsIgnoreCase(pid))      { error("PID '" + pid + "' already exists."); return; }
        if (!isNonNeg(arr))                        { error("Arrival must be a non-negative integer."); return; }
        if (!isPos(bst))                           { error("Burst must be a positive integer.");       return; }
        if (!isPos(pri))                           { error("Priority must be a positive integer.");    return; }

        processList.add(new Process(pid, Integer.parseInt(arr), Integer.parseInt(bst), Integer.parseInt(pri)));
        listModel.addElement(String.format("%-6s  arr=%-3s  burst=%-3s  priority=%s", pid, arr, bst, pri));
        fPid.setText(""); fArrival.setText(""); fBurst.setText(""); fPriority.setText("");
        fPid.requestFocus();
    }

    private void clearAll() {
        processList.clear(); listModel.clear();
        ganttPri.setEntries(new ArrayList<>(), new ArrayList<>());
        ganttSRTF.setEntries(new ArrayList<>(), new ArrayList<>());
        txtComp.setText(""); txtConc.setText(""); errorLbl.setText(" ");
    }

    private void runSimulation() {
        if (processList.size() < 2) {
            JOptionPane.showMessageDialog(this, "Add at least 2 processes first.",
                "Not enough processes", JOptionPane.WARNING_MESSAGE); return;
        }
        SchedulerResult rPri  = PriorityScheduler.run(processList);
        SchedulerResult rSRTF = SRTFScheduler.run(processList);
        ganttPri.setEntries(rPri.gantt,   rPri.processes);
        ganttSRTF.setEntries(rSRTF.gantt, rSRTF.processes);
        resPri.populate(rPri);
        resSRTF.populate(rSRTF);
        txtComp.setText(buildComparison(rPri, rSRTF));
        txtConc.setText(buildConclusion(rPri, rSRTF));
        txtComp.setCaretPosition(0); txtConc.setCaretPosition(0);
        tabs.setSelectedIndex(1);
    }

    private void loadScenario(char s) {
        clearAll();
        switch (s) {
            case 'A':
                add("P1",0,8,2); add("P2",1,4,1); add("P3",2,9,3); add("P4",3,5,2); break;
            case 'B':
                add("P1",0,12,1); add("P2",2,3,3); add("P3",4,2,3); add("P4",6,5,2); break;
            case 'C':
                add("P1",0,1,1); add("P2",0,1,1); add("P3",0,1,1);
                add("P4",0,10,4); add("P5",2,1,1); break;
            case 'D':
                add("P1",0,5,2); add("P2",1,3,1);
                JOptionPane.showMessageDialog(this,
                    "Scenario D — Validation Demo\n\n" +
                    "The simulator rejects these invalid inputs:\n\n" +
                    "  Empty PID           → PID cannot be empty.\n" +
                    "  Duplicate PID       → PID already exists.\n" +
                    "  Arrival = -1        → Must be non-negative integer.\n" +
                    "  Burst = 0           → Must be a positive integer.\n" +
                    "  Priority = 'abc'    → Must be a positive integer.\n\n" +
                    "Two valid processes have been loaded.",
                    "Validation Demo", JOptionPane.INFORMATION_MESSAGE); break;
        }
    }

    private void add(String pid, int arr, int bst, int pri) {
        processList.add(new Process(pid, arr, bst, pri));
        listModel.addElement(String.format("%-6s  arr=%-3d  burst=%-3d  priority=%d", pid, arr, bst, pri));
    }

    // ── text builders ────────────────────────────────────────────
    private String buildComparison(SchedulerResult pri, SchedulerResult srtf) {
        return String.format(
            "COMPARISON SUMMARY\n" +
            "─────────────────────────────────────────────────\n" +
            "  Metric                     Priority    SRTF\n" +
            "  Average Waiting Time       %-10.2f  %.2f\n" +
            "  Average Turnaround Time    %-10.2f  %.2f\n" +
            "  Average Response Time      %-10.2f  %.2f\n\n" +
            "Analysis\n" +
            "─────────────────────────────────────────────────\n" +
            "  Q1. Lower avg WT?          → %s\n" +
            "  Q2. Lower avg RT?          → %s\n" +
            "  Q3. Priority favours urgent processes?  → YES\n" +
            "      (lower priority number always gets CPU first)\n" +
            "  Q4. SRTF favours short jobs aggressively? → YES\n" +
            "      (preempts immediately when shorter job arrives)\n" +
            "  Q5. Recommended algorithm? → %s  (lower avg TAT)\n",
            pri.avgWT,  srtf.avgWT,
            pri.avgTAT, srtf.avgTAT,
            pri.avgRT,  srtf.avgRT,
            pri.avgWT  <= srtf.avgWT  ? "Priority" : "SRTF",
            pri.avgRT  <= srtf.avgRT  ? "Priority" : "SRTF",
            pri.avgTAT <= srtf.avgTAT ? "Priority" : "SRTF");
    }

    private String buildConclusion(SchedulerResult pri, SchedulerResult srtf) {
        return String.format(
            "FINAL CONCLUSION\n" +
            "─────────────────────────────────────────────────\n" +
            "  1. Overall performance: %s performed better on avg TAT.\n\n" +
            "  2. Metric winners:\n" +
            "       WT  → %s\n" +
            "       TAT → %s\n" +
            "       RT  → %s\n\n" +
            "  3. Trade-off:\n" +
            "       Priority honours policy (urgent jobs first) but\n" +
            "       low-priority jobs may starve.\n" +
            "       SRTF minimises wait times but ignores urgency —\n" +
            "       a long high-priority job can be starved.\n\n" +
            "  4. Fairness:\n" +
            "       Priority is fairer in policy-driven environments.\n" +
            "       SRTF is fairer where all jobs have equal importance.\n",
            pri.avgTAT <= srtf.avgTAT ? "Priority" : "SRTF",
            pri.avgWT  <= srtf.avgWT  ? "Priority" : "SRTF",
            pri.avgTAT <= srtf.avgTAT ? "Priority" : "SRTF",
            pri.avgRT  <= srtf.avgRT  ? "Priority" : "SRTF");
    }

    private boolean isNonNeg(String s) { try { return Integer.parseInt(s) >= 0; } catch (Exception e) { return false; } }
    private boolean isPos(String s)    { try { return Integer.parseInt(s) >= 1; } catch (Exception e) { return false; } }
    private void error(String msg)     { errorLbl.setText("⚠ " + msg); }

    private JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(2,8,8,8), title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), ACCENT)));
        return p;
    }

    private JPanel scrollCard(String title, JComponent inner) {
        JPanel c = card(title); c.setLayout(new BorderLayout());
        JScrollPane sc = new JScrollPane(inner,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sc.setBorder(BorderFactory.createEmptyBorder());
        c.add(sc, BorderLayout.CENTER); return c;
    }

    private static JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(new Color(0x20253A)); f.setForeground(new Color(0xDDE3F5));
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x2C3050)),
            BorderFactory.createEmptyBorder(3,5,3,5)));
        return f;
    }

    private static JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(new Color(0xDDE3F5));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(5,12,5,12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static JTextArea textArea() {
        JTextArea a = new JTextArea();
        a.setBackground(new Color(0x161A28)); a.setForeground(new Color(0xDDE3F5));
        a.setFont(new Font("Courier New", Font.PLAIN, 13));
        a.setEditable(false); a.setLineWrap(false);
        a.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        return a;
    }

    private static JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(new Color(0x7880A0)); return l;
    }

    private static JLabel muted(String t) { return lbl(t); }
}
