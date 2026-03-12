package leftLeaningRBTrees;

import java.util.Random;

/**
 * Small benchmark runner to compare different workloads on LeftLeaningRBTrees.
 * This is intentionally separate from JUnit tests so it does not slow normal test runs.
 */
public class LLRBTreesTimingComparison {
    private static final int[] TREE_SIZES = {1000, 10000, 100000};
    private static final int INSERT_OPS = 20000;
    private static final int GET_OPS = 50000;
    private static final int WARMUP_ROUNDS = 10;
    private static final int MEASURE_ROUNDS = 10;
    private static final int KEY_RANGE = 1_000_000;
    private static final boolean PRINT_PER_ROUND = false;

    private static final class Stats {
        private final double meanMs;
        private final double stdDevMs;

        private Stats(double meanMs, double stdDevMs) {
            this.meanMs = meanMs;
            this.stdDevMs = stdDevMs;
        }

        private double opsPerSecond(int operationCount) {
            return (operationCount * 1000.0) / meanMs;
        }
    }

    public static void main(String[] args) {
        System.out.println("size\trandomInsert(ms)\tduplicateInsert(ms)\trandomGet(ms)");
        System.out.println("\tmean+-sd | ops/s\tmean+-sd | ops/s\tmean+-sd | ops/s");

        for (int size : TREE_SIZES) {
            Stats randomInsert = runStatsMs("randomInsert", size, INSERT_OPS, () -> benchRandomInsert(size));
            Stats duplicateInsert = runStatsMs("duplicateInsert", size, INSERT_OPS, () -> benchDuplicateInsert(size));
            Stats randomGet = runStatsMs("randomGet", size, GET_OPS, () -> benchRandomGet(size));

            System.out.printf(
                    "%d\t%.3f+-%.3f | %.0f\t%.3f+-%.3f | %.0f\t%.3f+-%.3f | %.0f%n",
                    size,
                    randomInsert.meanMs, randomInsert.stdDevMs, randomInsert.opsPerSecond(INSERT_OPS),
                    duplicateInsert.meanMs, duplicateInsert.stdDevMs, duplicateInsert.opsPerSecond(INSERT_OPS),
                    randomGet.meanMs, randomGet.stdDevMs, randomGet.opsPerSecond(GET_OPS)
            );
        }
    }

    private static Stats runStatsMs(String label, int size, int operationCount, Runnable task) {
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            task.run();
        }

        double[] roundMs = new double[MEASURE_ROUNDS];
        for (int i = 0; i < MEASURE_ROUNDS; i++) {
            long start = System.nanoTime();
            task.run();
            roundMs[i] = (System.nanoTime() - start) / 1_000_000.0;

            if (PRINT_PER_ROUND) {
                double roundOpsPerSec = (operationCount * 1000.0) / roundMs[i];
                System.out.printf("  %s size=%d round=%d: %.3f ms (%.0f ops/s)%n",
                        label, size, i + 1, roundMs[i], roundOpsPerSec);
            }
        }

        return new Stats(mean(roundMs), stdDev(roundMs));
    }

    private static double mean(double[] values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private static double stdDev(double[] values) {
        if (values.length < 2) {
            return 0.0;
        }

        double meanValue = mean(values);
        double sumSquaredDiff = 0.0;
        for (double value : values) {
            double diff = value - meanValue;
            sumSquaredDiff += diff * diff;
        }
        // Sample standard deviation gives better small-sample stability.
        return Math.sqrt(sumSquaredDiff / (values.length - 1));
    }

    private static void benchRandomInsert(int initialSize) {
        Random random = new Random(2026 + initialSize);
        LeftLeaningRBTrees<Integer> tree = buildRandomTree(initialSize, random);
        for (int i = 0; i < INSERT_OPS; i++) {
            tree.insert(random.nextInt(KEY_RANGE));
        }
    }

    private static void benchDuplicateInsert(int initialSize) {
        Random random = new Random(4040 + initialSize);
        LeftLeaningRBTrees<Integer> tree = buildRandomTree(initialSize, random);
        int hotKey = random.nextInt(KEY_RANGE);
        tree.insert(hotKey);
        for (int i = 0; i < INSERT_OPS; i++) {
            tree.insert(hotKey);
        }
    }

    private static void benchRandomGet(int initialSize) {
        Random random = new Random(8080 + initialSize);
        LeftLeaningRBTrees<Integer> tree = buildRandomTree(initialSize, random);
        for (int i = 0; i < GET_OPS; i++) {
            tree.get(random.nextInt(KEY_RANGE));
        }
    }

    private static LeftLeaningRBTrees<Integer> buildRandomTree(int size, Random random) {
        LeftLeaningRBTrees<Integer> tree = new LeftLeaningRBTrees<>();
        for (int i = 0; i < size; i++) {
            tree.insert(random.nextInt(KEY_RANGE));
        }
        return tree;
    }
}