package leftLeaningRBTrees;

import java.util.Objects;

/**
 * LLRBTrees, 是一种与2-3-trees双射的数据结构，因此有自平衡属性
 * 与基础BST的实现十分类似
 * @author eeoe
 * */
public class LeftLeaningRBTrees<T extends Comparable<T>> {

    private enum color{
        red,
        black
    }

    private void changeColor(node x) {
        if (x != null) {
            if (x.colorTowardsParent == color.red) {
                x.colorTowardsParent = color.black;
            } else {
                x.colorTowardsParent = color.red;
            }
        }
    }
    private class node{
        T a;
        /*如何在node中表示node之间分支的红与黑？  考虑用该分支近叶的节点的成员变量表示*/
        color colorTowardsParent;
        node left;
        node right;

        node(T a) {
            this.a = a;
            this.left = this.right = null;
            this.colorTowardsParent = color.red;
        }
    }
    /*member variables*/
    node root;
    int size;

    /*member functions*/
    LeftLeaningRBTrees() {
        root = null;
        size = 0;
    }

    public boolean empty() {
        return this.size == 0;
    }
    public void insert(T x) {
        boolean[] haveSameNum = new boolean[]{false};
        root = insertRecur(x, root, haveSameNum);
        if (!haveSameNum[0]) {
            size++;
            root.colorTowardsParent = color.black;
        }
    }

    private node fixUp(node tar) {
        if (isRed(tar.right)) tar = rotateLeft(tar);
        if (isRed(tar.left) && isRed(tar.left.left)) tar = rotateRight(tar);
        if (isRed(tar.left) && isRed(tar.right)) flipColor(tar);
        return tar;
    }
    
    private node insertRecur(T x, node tar, boolean[] haveSameNum) {
        if(tar == null) {
            return new node(x);
        } else if (x.compareTo(tar.a) > 0) {
            tar.right = insertRecur(x, tar.right, haveSameNum);
        } else if (x.compareTo(tar.a) < 0) {
            tar.left = insertRecur(x, tar.left, haveSameNum);
        } else if (x.compareTo(tar.a) == 0) {
            haveSameNum[0] = true;
        }
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
        return fixUp(tar);
    }

    private boolean isRed(node tar) {
        return tar != null && tar.colorTowardsParent == color.red;
    }

    public boolean get(T x) {
        return getRecur(x, root);
    }

    private boolean getRecur(T x, node tar) {
        if (tar == null) {
            return false;
        } else if (x.compareTo(tar.a) > 0) {
            return getRecur(x, tar.right);
        } else if (x.compareTo(tar.a) < 0) {
            return getRecur(x, tar.left);
        } else if (x.compareTo(tar.a) == 0) {
            return true;
        }
        return false;
    }

    // Assuming h is red and both h.left and h.left.left are black,
    // make h.left or one of its children red.
    private node moveRedLeft(node h) {
        flipColor(h);
        //if have a 4node:
        if (isRed(h.right != null ? h.right.left : null)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColor(h);
        }
        return h;
    }

    // Assuming h is red and both h.right and h.right.left are black,
    // make h.right or one of its children red.
    private node moveRedRight(node h) {
        flipColor(h);
        //if have a 4node:
        if (isRed(h.left != null ? h.left.left : null)) {
            h = rotateRight(h);
            flipColor(h);
        }
        return h;
    }

    public void realRemove(T x) {
        if (root == null) return;
        root.colorTowardsParent = color.red;
        root = realRemove(x, root);
        if (root != null) root.colorTowardsParent = color.black;
    }

    private node realRemove(T key, node h) {
        if (key.compareTo(h.a) < 0) {
            // Ensure we don't descend into a 2-node on the left
            if (h.left != null && !isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            if (h.left != null) h.left = realRemove(key, h.left);
        } else {
            // Prepare for deletions on the right side / at h
            if (isRed(h.left)) {
                h = rotateRight(h);
            }

            // If matching key and no right child => delete this node (leaf-ish case)
            if (key.compareTo(h.a) == 0 && h.right == null) {
                return null;
            }

            // Ensure we don't descend into a 2-node on the right
            if (h.right != null && !isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }

            if (key.compareTo(h.a) == 0) {
                // Replace with successor
                node x = min(h.right);
                h.a = x.a;
                // Delete successor from right subtree
                h.right = deleteMin(h.right);
            } else {
                if (h.right != null) h.right = realRemove(key, h.right);
            }
        }

        return fixUp(h);
    }

    private node min(node h) {
        while (h.left != null) h = h.left;
        return h;
    }

    private node deleteMin(node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = deleteMin(h.left);
        return fixUp(h);
    }


    public void rm(T x) {

    }
    /// for a left leaned tree, rotate to left means inserted a right leaned red-key.
    /// target is the centre-node to rotate, target.right will become the new parent of tar.
    /// assume that tar.right and tar is not null.
    private node rotateLeft(node tar) {
        node bf = tar.right;
        color bf1 = bf.colorTowardsParent;
        bf.colorTowardsParent = tar.colorTowardsParent;
        tar.colorTowardsParent = bf1;
        tar.right = tar.right.left;
        bf.left = tar;
        return bf;
    }

    /// for a left leaned tree, rotate to right means two continuing left leaned red keys.
    /// target is the parent to rotate, target.left will become the new parent of tar.
    /// assume that tar.left and tar is not null.
    private node rotateRight(node tar) {
        node bf = tar.left;
        color bf1 = bf.colorTowardsParent;
        bf.colorTowardsParent = tar.colorTowardsParent;
        tar.colorTowardsParent = bf1;
        tar.left = tar.left.right;
        bf.right = tar;
        return bf;
    }

    private void flipColor(node tar) {
        changeColor(tar);
        changeColor(tar.left);
        changeColor(tar.right);
    }
}
