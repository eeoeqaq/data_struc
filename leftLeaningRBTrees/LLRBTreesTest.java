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
                        ll.remove(lastNum.pop());
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
    private double measureInsert(int n, boolean sorted) {
        LeftLeaningRBTrees<Integer> t = new LeftLeaningRBTrees<>();
        Random r = new Random(12345);

        // 轻量预热
        for (int i = 0; i < 2000; i++) t.insert(r.nextInt());

        Stopwatch sw = new Stopwatch();
        if (sorted) {
            for (int i = 0; i < n; i++) t.insert(i);
        } else {
            for (int i = 0; i < n; i++) t.insert(r.nextInt());
        }
        return sw.elapsedTime();
    }
    @Test
    public void compareRandomVsSorted() {
        int[] ns = {100, 1000, 10000, 50000};
        for (int n : ns) {
            double tr = measureInsert(n, false);
            double ts = measureInsert(n, true);
            System.out.printf("N=%d random=%.6fs(%.3f us/op), sorted=%.6fs(%.3f us/op)%n",
                    n, tr, tr * 1e6 / n, ts, ts * 1e6 / n);
        }
    }
}
