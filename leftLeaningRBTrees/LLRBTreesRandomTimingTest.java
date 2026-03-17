package leftLeaningRBTrees;

import org.junit.Assume;
import org.junit.Test;

import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LLRBTreesRandomTimingTest {
    private static final int KEY_RANGE = 20_000;

    @Test
    public void randomConsistencyWithTreeSet() {
        LeftLeaningRBTrees<Integer> tree = new LeftLeaningRBTrees<>();
        TreeSet<Integer> model = new TreeSet<>();
        Random random = new Random(20260313L);

        for (int i = 0; i < 30_000; i++) {
            int key = random.nextInt(KEY_RANGE);
            int op = random.nextInt(100);

            if (op < 50) {
                tree.insert(key);
                model.add(key);
            } else if (op < 80) {
                assertEquals(model.contains(key), tree.get(key));
            } else {
                if (model.contains(key)) {
                    tree.realRemove(key);
                    model.remove(key);
                }
                assertEquals(model.contains(key), tree.get(key));
            }

            if (i % 1000 == 0) {
                int probe = random.nextInt(KEY_RANGE);
                assertEquals(model.contains(probe), tree.get(probe));
            }
        }

        for (int i = 0; i < 2000; i++) {
            assertEquals(model.contains(i), tree.get(i));
        }
    }

    @Test
    public void timingSmokeTestInsertAndGet() {
        LeftLeaningRBTrees<Integer> tree = new LeftLeaningRBTrees<>();
        Random random = new Random(7);
        int n = 50_000;

        long insertStart = System.nanoTime();
        for (int i = 0; i < n; i++) {
            tree.insert(random.nextInt(KEY_RANGE));
        }
        long insertNs = System.nanoTime() - insertStart;

        long getStart = System.nanoTime();
        int hits = 0;
        for (int i = 0; i < n; i++) {
            if (tree.get(random.nextInt(KEY_RANGE))) {
                hits++;
            }
        }
        long getNs = System.nanoTime() - getStart;

        System.out.printf("[timing-smoke] insert %,d ops: %.3f ms (%.1f ns/op)%n",
                n, insertNs / 1_000_000.0, (double) insertNs / n);
        System.out.printf("[timing-smoke] get    %,d ops: %.3f ms (%.1f ns/op), hits=%d%n",
                n, getNs / 1_000_000.0, (double) getNs / n, hits);

        assertTrue(insertNs > 0);
        assertTrue(getNs > 0);
    }

    @Test
    public void timingPerfOptionalLargerScale() {
        Assume.assumeTrue("Set -Dperf=true to enable larger timing run", Boolean.getBoolean("perf"));

        int[] sizes = {100_000, 300_000, 600_000};
        for (int n : sizes) {
            LeftLeaningRBTrees<Integer> tree = new LeftLeaningRBTrees<>();
            Random random = new Random(1000 + n);

            long t1 = System.nanoTime();
            for (int i = 0; i < n; i++) {
                tree.insert(random.nextInt(KEY_RANGE * 10));
            }
            long insertNs = System.nanoTime() - t1;

            long t2 = System.nanoTime();
            for (int i = 0; i < n; i++) {
                tree.get(random.nextInt(KEY_RANGE * 10));
            }
            long getNs = System.nanoTime() - t2;

            System.out.printf("[timing-perf] n=%,d insert=%.3f ms(%.1f ns/op), get=%.3f ms(%.1f ns/op)%n",
                    n,
                    insertNs / 1_000_000.0, (double) insertNs / n,
                    getNs / 1_000_000.0, (double) getNs / n);
        }
    }
}

