package priorityQueue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        assertEquals(2, pq.removeSmallest().intValue());
        assertEquals(6, pq.removeSmallest().intValue());
        assertEquals(13, pq.removeSmallest().intValue());
    }

    @Test
    public void test2() {
        List<Integer> toHeap = new ArrayList<>(10);
        Collections.addAll(toHeap, 5, 3, 8, 1, 2, 4, 6, 7, 9, 0);
        PriorityQueue<Integer> pq = new PriorityQueue<>(toHeap);
        pq.toStringline();
    }
}
