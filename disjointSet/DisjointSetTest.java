package disjointSet;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisjointSetTest {
    @Test
    public void test1() {
        DisjointSet a = new DisjointSet(10);
        a.union(0,1);
        a.union(5,6);
        a.union(1,5);
        assertTrue(a.find(0, 6));
        assertFalse(a.find(0, 9));
    }
    /**
     * 并查集：对于大小为n的集合
     * 并为O(1)
     * 查为O(log(n))或O(A(n)), A为反阿克曼函数
     * */
    @Test
    public void timingTest0() {
        double[] answers =new double[10];
        int size = 1000;
        for (int i = 0; i < 10; i++) {
            DisjointSet djs = new DisjointSet(size);
            Stopwatch sw =new Stopwatch();
            for (int j = 1; j < size; j++) {
                djs.union(0, j);
            }
            answers[i] = sw.elapsedTime()/size;
            size*=2;
        }
        for (double x : answers) {
            System.out.println(x);
        }
    }

    @Test
    public void timingTest1() {
        double[] answers =new double[10];
        int size = 1000;
        for (int i = 0; i < 10; i++) {
            DisjointSet djs = new DisjointSet(size);
            for (int j = 1; j < size; j++) {
                djs.union(0, j);
            }
            Stopwatch sw =new Stopwatch();
            for (int j = 1; j < size; j++) {
                djs.find(0, j);
            }
            answers[i] = sw.elapsedTime()/size;
            size*=2;
        }
        for (double x : answers) {
            System.out.println(x);
        }
    }

}
