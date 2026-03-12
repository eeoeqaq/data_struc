package disjointSet;

import java.util.HashSet;
import java.util.Set;

/** 并查集算法，计划使用数组存储根和数大小，完成标准的并查集
 * 实现方法一般有：listsOfSets，数组记录所属set编号，数组记录树状链表等
 * 这里采用数组表示树状链表的方法
 * @author eeoe
 * */
public class DisjointSet {
    private int[] parents;

    DisjointSet(int i) {
        init(i);
    }

    private void init(int i) {
        this.parents = new int[i];
        for (int x = 0; x < i; x++) {
            this.parents[x] = -1;
        }
    }

    private int findRoot(int i) {
        Set<Integer> a = new HashSet<>();
        while (parents[i] >= 0) {
            a.add(i);
            i = parents[i];
        }
        for (int x : a) {
            parents[x] = i;
        }
        return i;
    }

    private int quickFind(int i) {
        while (parents[i] >= 0) {
            i = parents[i];
        }
        return i;
    }

    private int getWeight(int i) {
        int root = findRoot(i);
        return -parents[root];
    }

    public void union(int i, int j) {
        if (getWeight(i) < getWeight(j)) {
            unionTo(i, j);
        } else {
            unionTo(j, i);
        }
    }

    private void unionTo(int i, int j){
        int rootI = quickFind(i);
        int rootJ = quickFind(j);
        if (rootI == rootJ) {
            return;
        }
        parents[rootJ] += parents[rootI];
        parents[rootI] = rootJ;
    }

    public boolean find(int i, int j) {
        return findRoot(i) == findRoot(j);
    }
}
