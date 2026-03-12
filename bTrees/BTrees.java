package bTrees;

/**
 * B-Trees，这里实现的是2-3trees，是一种自平衡的树状结构
 * 所有节点均可包含一或两个数据，有一个数据的节点可以有2个子嗣，两个数据的节点可以有3个子嗣，分别称为2node和3node
 * 23trees有以下性质：
 * 严格平衡，根节点到每个叶子节点的距离严格一致
 * 除叶子节点以外的节点必须有最大的子嗣数量，即2node有2个子嗣，3node有3个子嗣
 * @author eeoe
 * */
public class BTrees {
    private class node {

        int state;
    }
}
