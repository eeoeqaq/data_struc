package leftLeaningRBTrees;

import java.security.cert.TrustAnchor;
import java.util.Objects;

import static java.awt.Color.red;

/**
 * LLRBTrees, 是一种与2-3-trees双射的数据结构，因此有自平衡属性
 * 与基础BST的实现十分类似
 * */
public class LeftLeaningRBTrees<T extends Comparable<T>> {

    private enum color{
        red,
        black
    }

    private static class node<T>{
        T a;
        /*如何在node中表示node之间分支的红与黑？  考虑用该分支近叶的节点的成员变量表示*/
        color colorTowardsParent;
        node<T> left;
        node<T> right;

        node(T a) {
            this.a = a;
            this.left = this.right = null;
            this.colorTowardsParent = color.red;
        }
    }
    /*member variables*/
    node<T> root;
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
            root.colorTowardsParent = color.red;
        }
    }

    private node<T> insertRecur(T x, node<T> tar, boolean[] haveSameNum) {
        if(tar == null) {
            return new node<>(x);
        } else if (x.compareTo(tar.a) > 0) {
            tar.right = insertRecur(x, tar.right, haveSameNum);
        } else if (x.compareTo(tar.a) < 0) {
            tar.left = insertRecur(x, tar.left, haveSameNum);
        } else if (x.compareTo(tar.a) == 0) {
            haveSameNum[0] = true;
        }
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
        if (isRed(tar.right)) tar = rotateLeft(tar);
        if (isRed(tar.left) && isRed(tar.left.left)) tar = rotateRight(tar);
        if (isRed(tar.left) && isRed(tar.right)) flipColor(tar);
        return tar;
    }

    private boolean isRed(node<T> tar) {
        if (tar == null) {
            return false;
        } else {
            return tar.colorTowardsParent == color.red;
        }
    }

    public boolean get(T x) {
        return getRecur(x, root);
    }

    private boolean getRecur(T x, node<T> tar) {
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

    public boolean remove(T x) {
        if (removeRecur(x, null, root)) {
            size--;
            return true;
        }
        return false;
    }

    private boolean removeRecur(T x, node<T> parent, node<T> tar) {

        if (tar == null) {
            return false;
        } else if (x.compareTo(tar.a) > 0) {
            return removeRecur(x, tar, tar.right);
        } else if (x.compareTo(tar.a) < 0) {
            return removeRecur(x, tar, tar.left);
        } else if (x.compareTo(tar.a) == 0) {
            //TODO: root
            if (tar.left == null && tar.right == null) {
                if (parent.left == tar) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            } else if (tar.left != null && tar.right != null) {
                if (parent.left == tar) {
                    node<T> parentOfRight = parent.right;
                    while(parentOfRight.left != null) {
                        parentOfRight = parentOfRight.left;
                    }
                    parent.left = tar.left;
                    parentOfRight.left = tar.right;
                } else {
                    node<T> parentOfLeft = parent.left;
                    while(parentOfLeft.right != null) {
                        parentOfLeft = parentOfLeft.right;
                    }
                    parent.right = tar.right;
                    parentOfLeft.right = tar.left;
                }
            } else {
                if (parent.left == tar) {
                    //TODO: new method induced.
                    parent.left = Objects.requireNonNullElseGet(tar.left, () -> tar.right);
                } else {
                    parent.right = Objects.requireNonNullElseGet(tar.left, () -> tar.right);
                }
            }
            return true;
        }
        return true;
    }

    /// for a left leaned tree, rotate to left means inserted a right leaned red-key.
    /// target is the centre-node to rotate, target.right will become the new parent of tar.
    /// assume that tar.right and tar is not null.
    private node<T> rotateLeft(node<T> tar) {
        node<T> bf = tar.right;
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
    private node<T> rotateRight(node<T> tar) {
        node<T> bf = tar.left;
        color bf1 = bf.colorTowardsParent;
        bf.colorTowardsParent = tar.colorTowardsParent;
        tar.colorTowardsParent = bf1;
        tar.left = tar.left.right;
        bf.right = tar;
        return bf;
    }

    private void flipColor(node<T> tar) {
        tar.left.colorTowardsParent = color.black;
        tar.right.colorTowardsParent = color.black;
        tar.colorTowardsParent = color.red;
    }
}
