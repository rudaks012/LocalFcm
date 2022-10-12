package co.whalesoft;

import java.text.NumberFormat;
import org.springframework.util.StopWatch;

public class YongjuStopWatch extends StopWatch {
    @Override
    public String shortSummary() {
        return "StopWatch '" + this.getId() + "': running time = " +String.format("%.2f",(double)this.getTotalTimeNanos() / 1_000_000_000)  + " s";
    }

    @Override
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');

        sb.append("---------------------------------------------\n");
        sb.append(" s         %     Task name\n");
        sb.append("---------------------------------------------\n");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(9);
        nf.setGroupingUsed(false);
        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);
        TaskInfo[] var4 = this.getTaskInfo();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            TaskInfo task = var4[var6];
            double seconds = (double) task.getTimeNanos() / 1_000_000_000;
            sb.append(String.format("%.2f", seconds)).append("  ");
            sb.append(pf.format((double) task.getTimeNanos() / (double) this.getTotalTimeNanos()))
              .append("  ");
            sb.append(task.getTaskName()).append('\n');
        }

        return sb.toString();
    }
}
