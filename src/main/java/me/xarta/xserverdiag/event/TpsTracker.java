package me.xarta.xserverdiag.event;

import me.xarta.xserverdiag.XServerDiag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = XServerDiag.MODID)
public final class TpsTracker {
    private static final int CURRENT_WINDOW = 20;
    private static final int ONE_MIN_WINDOW = 20 * 60;
    private static final int FIVE_MIN_WINDOW = 20 * 60 * 5;
    private static final int FIFTEEN_MIN_WINDOW = 20 * 60 * 15;

    private static final double[] BUF_CURR  = new double[CURRENT_WINDOW];
    private static final double[] BUF_1M    = new double[ONE_MIN_WINDOW];
    private static final double[] BUF_5M    = new double[FIVE_MIN_WINDOW];
    private static final double[] BUF_15M   = new double[FIFTEEN_MIN_WINDOW];

    private static int idxCurr = 0, idx1m = 0, idx5m = 0, idx15m = 0;
    private static int countCurr = 0, count1m = 0, count5m = 0, count15m = 0;
    private static double sumCurr = 0, sum1m = 0, sum5m = 0, sum15m = 0;

    private static long lastTickNanos = -1L;

    private TpsTracker() {}

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post e) {
        long now = System.nanoTime();
        if (lastTickNanos != -1L) {
            double mspt = (now - lastTickNanos) / 1_000_000.0;
            push(mspt, BUF_CURR, CURRENT_WINDOW);
            push(mspt, BUF_1M, ONE_MIN_WINDOW);
            push(mspt, BUF_5M, FIVE_MIN_WINDOW);
            push(mspt, BUF_15M, FIFTEEN_MIN_WINDOW);
        }
        lastTickNanos = now;
    }

    private static void push(double mspt, double[] buf, int size) {
        if (buf == BUF_CURR) {
            sumCurr -= BUF_CURR[idxCurr];
            BUF_CURR[idxCurr] = mspt; sumCurr += mspt;
            if (++idxCurr >= size) idxCurr = 0;
            if (countCurr < size) countCurr++;
        } else if (buf == BUF_1M) {
            sum1m -= BUF_1M[idx1m];
            BUF_1M[idx1m] = mspt; sum1m += mspt;
            if (++idx1m >= size) idx1m = 0;
            if (count1m < size) count1m++;
        } else if (buf == BUF_5M) {
            sum5m -= BUF_5M[idx5m];
            BUF_5M[idx5m] = mspt; sum5m += mspt;
            if (++idx5m >= size) idx5m = 0;
            if (count5m < size) count5m++;
        } else {
            sum15m -= BUF_15M[idx15m];
            BUF_15M[idx15m] = mspt; sum15m += mspt;
            if (++idx15m >= size) idx15m = 0;
            if (count15m < size) count15m++;
        }
    }

    private static double clampTps(double tps) {
        return Math.max(0.0, Math.min(20.0, tps));
    }

    private static double toTps(double avgMspt) {
        if (avgMspt <= 0) return 20.0;
        return clampTps(1000.0 / avgMspt);
    }

    public static double getTpsCurrent() {
        int n = Math.max(1, countCurr);
        double avg = sumCurr / n;
        return toTps(avg);
    }

    public static double getTps1m() {
        int n = Math.max(1, count1m);
        double avg = sum1m / n;
        return toTps(avg);
    }

    public static double getTps5m() {
        int n = Math.max(1, count5m);
        double avg = sum5m / n;
        return toTps(avg);
    }

    public static double getTps15m() {
        int n = Math.max(1, count15m);
        double avg = sum15m / n;
        return toTps(avg);
    }
}