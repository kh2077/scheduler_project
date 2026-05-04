// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

public class GanttPanel extends JPanel {
   private List<GanttEntry> entries = new ArrayList();
   private List<Process> processes = new ArrayList();
   private static final Color BAR_A = new Color(4882369);
   private static final Color BAR_B = new Color(5937784);
   private static final Color PANEL_BG = new Color(1974837);
   private static final Color TEXT_COL = new Color(14541813);
   private static final Color TICK_COL = new Color(8949930);
   private static final int BAR_H = 40;
   private static final int PAD = 14;

   public GanttPanel() {
      this.setBackground(PANEL_BG);
      this.setPreferredSize(new Dimension(800, 76));
   }

   public void setEntries(List<GanttEntry> e, List<Process> p) {
      this.entries = e;
      this.processes = p;
      int total = e.isEmpty() ? 1 : ((GanttEntry)e.get(e.size() - 1)).end;
      this.setPreferredSize(new Dimension(14 + Math.max(total * 56, 500) + 14, 76));
      this.revalidate();
      this.repaint();
   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (this.entries.isEmpty()) {
         g.setColor(TICK_COL);
         g.setFont(new Font("SansSerif", 2, 12));
         g.drawString("Run the simulation to see the chart.", 14, 28);
      } else {
         Graphics2D g2 = (Graphics2D)g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         int total = ((GanttEntry)this.entries.get(this.entries.size() - 1)).end;
         double scale = (double)(this.getWidth() - 28) / (double)total;
         Map<String, Color> cm = new LinkedHashMap();
         int idx = 0;

         for(Process p : this.processes) {
            if (!cm.containsKey(p.pid)) {
               cm.put(p.pid, idx++ % 2 == 0 ? BAR_A : BAR_B);
            }
         }

         for(GanttEntry e : this.entries) {
            int x = 14 + (int)((double)e.start * scale);
            int w = Math.max((int)((double)(e.end - e.start) * scale) - 1, 2);
            Color c = (Color)cm.getOrDefault(e.pid, BAR_A);
            g2.setColor(c);
            g2.fillRect(x, 4, w, 40);
            g2.setColor(c.darker());
            g2.drawRect(x, 4, w, 40);
            if (w > 18) {
               g2.setColor(TEXT_COL);
               g2.setFont(new Font("SansSerif", 1, 12));
               FontMetrics fm = g2.getFontMetrics();
               g2.drawString(e.pid, x + (w - fm.stringWidth(e.pid)) / 2, 4 + (40 + fm.getAscent()) / 2 - 2);
            }
         }

         g2.setFont(new Font("SansSerif", 0, 11));
         g2.setColor(TICK_COL);
         Set<Integer> drawn = new HashSet();

         for(GanttEntry e : this.entries) {
            int[] var14;
            for(int t : var14 = new int[]{e.start, e.end}) {
               if (drawn.add(t)) {
                  int tx = 14 + (int)((double)t * scale);
                  String s = String.valueOf(t);
                  g2.drawLine(tx, 44, tx, 49);
                  g2.drawString(s, tx - g2.getFontMetrics().stringWidth(s) / 2, 62);
               }
            }
         }

      }
   }
}
