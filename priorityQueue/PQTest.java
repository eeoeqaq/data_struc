package priorityQueue;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
public class PQTest {
    @Test
    public void test1() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(6);
        pq.add(13);
        assertEquals(6, pq.getSmallest().intValue());
        pq.add(2);
        pq.add(2);
        assertEquals(2, pq.getSmallest().intValue());
        assertEquals(2, pq.removeSmallest().intValue());
        assertEquals(6, pq.removeSmallest().intValue());
        assertEquals(13, pq.removeSmallest().intValue());
    }
}
