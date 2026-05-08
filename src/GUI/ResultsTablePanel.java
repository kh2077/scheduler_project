import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ResultsTablePanel extends JPanel {

    private static final String[] COLS = {"PID","Arrival","Burst","Priority","Finish","WT","TAT","RT"};
    private static final Color BG  = new Color(0x1E2235);
    private static final Color TBL = new Color(0x252A3D);
    private static final Color FG  = new Color(0xDDE3F5);
    private static final Color HDR = new Color(0x4A7FC1);

    private final JTable table;
    private final JLabel avgLabel;

    public ResultsTablePanel() {
        setLayout(new BorderLayout(0, 6));
        setBackground(BG);

        DefaultTableModel model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setBackground(TBL); table.setForeground(FG);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26); table.setGridColor(new Color(0x2A2F40));
        table.setShowHorizontalLines(true); table.setShowVerticalLines(false);

        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(0x181C27)); h.setForeground(HDR);
        h.setFont(new Font("SansSerif", Font.BOLD, 13));
        h.setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLS.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(TBL);
        add(scroll, BorderLayout.CENTER);

        avgLabel = new JLabel(" ");
        avgLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        avgLabel.setForeground(new Color(0xF7C94F));
        avgLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 2, 4));
        add(avgLabel, BorderLayout.SOUTH);
    }

    public void populate(SchedulerResult res) {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        for (Process p : res.processes)
            m.addRow(new Object[]{p.pid, p.arrivalTime, p.burstTime, p.priority,
                                p.finishTime, p.waitingTime, p.turnaroundTime, p.responseTime});
        avgLabel.setText(String.format("Avg:  WT = %.2f   |   TAT = %.2f   |   RT = %.2f",
                                    res.avgWT, res.avgTAT, res.avgRT));
    }
}
