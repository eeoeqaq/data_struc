package priorityQueue;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * PQ by heap
 * @author eeoe
 */
public class PriorityQueue<T extends Comparable<T>> implements PQ<T> {
    T[] repo;
    int maxCapa;
    int size;
    double maxLimit = 9.0 / 10;
    double minLimit = 1.0 / 10;
    Comparator<T> passedInComparator;
    boolean usingPassedInComparator;
    /**record data from index 0*/
    public PriorityQueue(Comparator<T> comparator) {
        maxCapa = 50;
        size = 0;
        repo = (T[]) new Comparable[maxCapa];
        passedInComparator = comparator;
        usingPassedInComparator = true;
    }

    public PriorityQueue() {
        maxCapa = 50;
        size = 0;
        repo = (T[]) new Comparable[maxCapa];
        usingPassedInComparator = false;
    }

    private void checkSize() {
        if (this.size > this.maxCapa * maxLimit) {
            biggerCapa();
        } else if (this.maxCapa > 50 && this.size < this.maxCapa * minLimit) {
            smallerCapa();
        }
    }

    private void biggerCapa() {
        T[] newRepo = (T[]) new Comparable[2 * maxCapa];
        System.arraycopy(this.repo, 0, newRepo, 0, size);
        maxCapa = maxCapa * 2;
        this.repo = newRepo;
    }

    private void smallerCapa() {
        T[] newRepo = (T[]) new Comparable[2 * maxCapa / 3];
        System.arraycopy(this.repo, 0, newRepo, 0, size);
        maxCapa = maxCapa / 2;
        this.repo = newRepo;
    }

    /**no parents: return -1*/
    private int getPar(int i) {
        if (i == 0){
            return -1;
        }
        return (i - 1) / 2;
    }

    private int getLeftChild(int i) {
        int tar = 2 * i + 1;
        if (tar >= size) {
            return -1;
        }
        return tar;
    }

    private int getRightChild(int i) {
        int tar = 2 * i + 2;
        if (tar >= size) {
            return -1;
        }
        return tar;
    }

    @Override
    public void add(T x) {
        checkSize();
        repo[size] = x;
        floatUp(size);
        size++;
    }

    @Override
    public T getSmallest() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return repo[0];
    }

    @Override
    public T removeSmallest() {
        checkSize();
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T tar = repo[0];
        repo[0] = repo[size - 1];
        size--;
        drownDown(0);
        return tar;
    }

    @Override
    public int size() {
        return this.size;
    }

    private int switchCompare(T x, T y) {
        if (usingPassedInComparator){
            return passedInComparator.compare(x, y);
        } else {
            return x.compareTo(y);
        }
    }

    private void floatUp(int tar) {
        int parentIndex = getPar(tar);
        if (parentIndex != -1 && switchCompare(repo[tar], repo[parentIndex]) < 0){
            T bf = repo[tar];
            repo[tar] = repo[parentIndex];
            repo[parentIndex] = bf;
            floatUp(parentIndex);
        }
    }

    private void drownDown(int tar) {
        int leftIndex = getLeftChild(tar);
        int rightIndex = getRightChild(tar);
        if (leftIndex != -1 && switchCompare(repo[tar], repo[leftIndex]) > 0) {
            if (rightIndex != -1 && switchCompare(repo[leftIndex], repo[rightIndex]) > 0) {
                T bf = repo[tar];
                repo[tar] = repo[rightIndex];
                repo[rightIndex] = bf;
                drownDown(rightIndex);
            } else {
                T bf = repo[tar];
                repo[tar] = repo[leftIndex];
                repo[leftIndex] = bf;
                drownDown(leftIndex);
            }
        } else if (rightIndex != -1 && switchCompare(repo[tar], repo[rightIndex]) > 0) {
            T bf = repo[tar];
            repo[tar] = repo[rightIndex];
            repo[rightIndex] = bf;
            drownDown(rightIndex);
        }
    }
}
