package leftLeaningRBTrees;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import static org.junit.Assert.*;

public class LLRBTreesTest {
    @Test
    public void timeTest1() {
        java.util.Vector<Double> times1 = new Vector<>();
        LeftLeaningRBTrees<Integer> llret = new LeftLeaningRBTrees<Integer>();
        Random x = new Random();
        int bf;
        for (int i = 0; i <= 10000000; i++) {
            bf = x.nextInt(1000000);
            if (i == 10 || i == 1000 || i == 100000 || i == 10000000) {
                Stopwatch sw = new Stopwatch();
                for (int j = 0; j < 10000; j++) {
                    llret.insert(bf);
                }
                times1.add(sw.elapsedTime());
            } else {
                llret.insert(bf);
            }
        }
        for (double db : times1) {
            System.out.println(db);
        }
    }

    @Test
    public void timeTest2() {
        java.util.Vector<Double> times1 = new Vector<>();
        LeftLeaningRBTrees<Integer> llret = new LeftLeaningRBTrees<Integer>();
        Random x = new Random();
        int bf;
        Stopwatch[] a = new Stopwatch[4];
        for (int i = 0; i <= 3; i++) {
            a[i] = new Stopwatch();
        }
        for (int i = 0; i <= 100000; i++) {
            bf = x.nextInt(1000000);
            llret.insert(bf);
            if (i == 100) {
                times1.add(a[0].elapsedTime());
            }
            if (i == 1000) {
                times1.add(a[1].elapsedTime());
            }
            if (i == 10000) {
                times1.add(a[2].elapsedTime());
            }
            if (i == 100000) {
                times1.add(a[3].elapsedTime());
            }
        }
        for (double db : times1) {
            System.out.println(db);
        }
    }
    @Test
    public void randomtest() {
        LeftLeaningRBTrees<Integer> ll = new LeftLeaningRBTrees<>();
        Random rd = new Random();
        int flag = 0;
        Stack<Integer> lastNum = new Stack<>();
        for (int i = 0; i < 10000; i++) {
            flag = rd.nextInt(3);
            switch (flag) {
                case 0:
                    ll.get(i);
                case 1:
                    ll.insert(i);
                    lastNum.push(i);
                    break;
                case 2:
                    if (!lastNum.empty()){
                        ll.realRemove(lastNum.pop());
                    }
                    break;
            }
        }
    }
    @Test
    public void worstCase() {
        java.util.Vector<Double> times1 = new Vector<>();
        LeftLeaningRBTrees<Integer> llret = new LeftLeaningRBTrees<Integer>();
        int bf;

        Stopwatch[] a = new Stopwatch[5];
        for (int i = 0; i < 5; i++) {
            a[i] = new Stopwatch();
        }

        for (int i = 0; i <= 10000; i++) {
            llret.insert(i);
            if (i == 100) {
                times1.add(a[0].elapsedTime());
            }
            if (i == 1000) {
                times1.add(a[1].elapsedTime());
            }
            if (i == 10000) {
                times1.add(a[2].elapsedTime());
            }
        }
        for (double db : times1) {
            System.out.println(db);
        }
    }

    /// AI generated
    private long measureInsertNs(int n, boolean sorted, long seed) {
        LeftLeaningRBTrees<Integer> t = new LeftLeaningRBTrees<>();
        Random r = new Random(seed);

        // 预热：减少JIT对小样本测量的影响
        for (int i = 0; i < 5000; i++) {
            t.insert(r.nextInt());
        }

        long start = System.nanoTime();
        if (sorted) {
            for (int i = 0; i < n; i++) {
                t.insert(i);
            }
        } else {
            for (int i = 0; i < n; i++) {
                t.insert(r.nextInt());
            }
        }
        return System.nanoTime() - start;
    }

    private double mean(long[] values) {
        double sum = 0.0;
        for (long value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private double stdDev(long[] values, double mean) {
        if (values.length < 2) {
            return 0.0;
        }
        double sum = 0.0;
        for (long value : values) {
            double diff = value - mean;
            sum += diff * diff;
        }
        return Math.sqrt(sum / (values.length - 1));
    }

    @Test
    public void compareRandomVsSorted() {
        int[] ns = {10000, 50000, 100000, 200000};
        int rounds = 12;

        double prevRandomMeanNs = -1.0;
        double prevSortedMeanNs = -1.0;

        for (int n : ns) {
            long[] randomNs = new long[rounds];
            long[] sortedNs = new long[rounds];

            for (int i = 0; i < rounds; i++) {
                randomNs[i] = measureInsertNs(n, false, 12345L + i);
                sortedNs[i] = measureInsertNs(n, true, 54321L + i);
            }

            double randomMeanNs = mean(randomNs);
            double randomSdNs = stdDev(randomNs, randomMeanNs);
            double sortedMeanNs = mean(sortedNs);
            double sortedSdNs = stdDev(sortedNs, sortedMeanNs);

            String randomGrowth = prevRandomMeanNs < 0 ? "-" : String.format("%.2fx", randomMeanNs / prevRandomMeanNs);
            String sortedGrowth = prevSortedMeanNs < 0 ? "-" : String.format("%.2fx", sortedMeanNs / prevSortedMeanNs);

            System.out.printf(
                    "N=%d | random=%.3f+-%.3f ms (%.1f ns/op, growth=%s) | sorted=%.3f+-%.3f ms (%.1f ns/op, growth=%s)%n",
                    n,
                    randomMeanNs / 1_000_000.0, randomSdNs / 1_000_000.0, randomMeanNs / n, randomGrowth,
                    sortedMeanNs / 1_000_000.0, sortedSdNs / 1_000_000.0, sortedMeanNs / n, sortedGrowth
            );

            prevRandomMeanNs = randomMeanNs;
            prevSortedMeanNs = sortedMeanNs;
        }
    }
}
