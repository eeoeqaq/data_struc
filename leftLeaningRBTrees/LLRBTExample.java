package leftLeaningRBTrees;

/**
 * @author OpenAI
 * */

public class LLRBTExample <K extends Comparable<K>> {
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            K key;
            Node left, right;
            boolean color; // color of link from parent to this node

            Node(K key, boolean color) {
                this.key = key;
                this.color = color;
            }
        }

        private Node root;

        private boolean isRed(Node x) {
            return x != null && x.color == RED;
        }

        private Node rotateLeft(Node h) {
            Node x = h.right;
            h.right = x.left;
            x.left = h;
            x.color = h.color;
            h.color = RED;
            return x;
        }

        private Node rotateRight(Node h) {
            Node x = h.left;
            h.left = x.right;
            x.right = h;
            ///dangerous?
            x.color = h.color;
            h.color = RED;
            return x;
        }

        private void flipColors(Node h) {
            h.color = !h.color;
            if (h.left != null) h.left.color = !h.left.color;
            if (h.right != null) h.right.color = !h.right.color;
        }

        // Restores LLRB invariants after modifications
        private Node fixUp(Node h) {
            if (isRed(h.right)) h = rotateLeft(h);
            if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
            if (isRed(h.left) && isRed(h.right)) flipColors(h);
            return h;
        }

        // Assuming h is red and both h.left and h.left.left are black,
        // make h.left or one of its children red.
        private Node moveRedLeft(Node h) {
            flipColors(h);
            //if have a 4node:
            if (isRed(h.right != null ? h.right.left : null)) {
                h.right = rotateRight(h.right);
                h = rotateLeft(h);
                flipColors(h);
            }
            return h;
        }

        // Assuming h is red and both h.right and h.right.left are black,
        // make h.right or one of its children red.
        private Node moveRedRight(Node h) {
            flipColors(h);
            //if have a 4node:
            if (isRed(h.left != null ? h.left.left : null)) {
                h = rotateRight(h);
                flipColors(h);
            }
            return h;
        }

        private Node min(Node h) {
            while (h.left != null) h = h.left;
            return h;
        }

        public void remove(K key) {
            if (root == null) return;

            // Optional: if key not present, you can skip; otherwise this still works.
            // But many implementations do a contains() check first.

            // If both children of root are black, set root to red to enable moves.
            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            root = remove(root, key);

            if (root != null) root.color = BLACK;
        }

        private Node remove(Node h, K key) {
            if (key.compareTo(h.key) < 0) {
                // Ensure we don't descend into a 2-node on the left
                if (h.left != null && !isRed(h.left) && !isRed(h.left.left)) {
                    h = moveRedLeft(h);
                }
                if (h.left != null) h.left = remove(h.left, key);
            } else {
                // Prepare for deletions on the right side / at h
                if (isRed(h.left)) {
                    h = rotateRight(h);
                }

                // If matching key and no right child => delete this node (leaf-ish case)
                if (key.compareTo(h.key) == 0 && h.right == null) {
                    return null;
                }

                // Ensure we don't descend into a 2-node on the right
                if (h.right != null && !isRed(h.right) && !isRed(h.right.left)) {
                    h = moveRedRight(h);
                }

                if (key.compareTo(h.key) == 0) {
                    // Replace with successor
                    Node x = min(h.right);
                    h.key = x.key;
                    // Delete successor from right subtree
                    h.right = deleteMin(h.right);
                } else {
                    if (h.right != null) h.right = remove(h.right, key);
                }
            }

            return fixUp(h);
        }

        public void deleteMin() {
            if (root == null) return;
            if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
            root = deleteMin(root);
            if (root != null) root.color = BLACK;
        }

        private Node deleteMin(Node h) {
            if (h.left == null) return null;
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = deleteMin(h.left);
            return fixUp(h);
        }
    }

