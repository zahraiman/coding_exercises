package com.stripe.interview.challenge;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.Scanner;

enum Color {
  RED, GREEN
}

abstract class Tree {

  private int value;
  private Color color;
  private int depth;

  public Tree(int value, Color color, int depth) {
    this.value = value;
    this.color = color;
    this.depth = depth;
  }

  public int getValue() {
    return value;
  }

  public Color getColor() {
    return color;
  }

  public int getDepth() {
    return depth;
  }

  public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

  private ArrayList<Tree> children = new ArrayList<>();

  public TreeNode(int value, Color color, int depth) {
    super(value, color, depth);
  }

  public void accept(TreeVis visitor) {
    visitor.visitNode(this);

    for (Tree child : children) {
      child.accept(visitor);
    }
  }

  public void addChild(Tree child) {
    children.add(child);
  }
}

class TreeLeaf extends Tree {

  public TreeLeaf(int value, Color color, int depth) {
    super(value, color, depth);
  }

  public void accept(TreeVis visitor) {
    visitor.visitLeaf(this);
  }
}

abstract class TreeVis
{
  public abstract int getResult();
  public abstract void visitNode(TreeNode node);
  public abstract void visitLeaf(TreeLeaf leaf);

}

class SumInLeavesVisitor extends TreeVis {
  private ArrayList<TreeLeaf> leafs = new ArrayList<>();

  public int getResult() {
    int res = 0;
    for(TreeLeaf leaf : leafs){
      res += leaf.getValue();
    }
    return res;
  }

  public void visitNode(TreeNode node) {
    // visit non-leaf nodes
  }

  public void visitLeaf(TreeLeaf leaf) {
    leafs.add(leaf);
  }
}

class ProductOfRedNodesVisitor extends TreeVis {
  private ArrayList<Tree> nodes = new ArrayList<>();
  private final int modusNum = (int) (Math.pow(10, 9) + 1);

  public int getResult() {
    long product = 1;
    for(Tree tree : nodes){
      product = (product * tree.getValue()) % modusNum;
    }
    return (product == 0)? 1 : (int) (product);
  }

  public void visitNode(TreeNode node) {
    if(node.getColor().equals(Color.RED)) {
      nodes.add(node);
    }
  }

  public void visitLeaf(TreeLeaf leaf) {
    if(leaf.getColor().equals(Color.RED)) {
      nodes.add(leaf);
    }
  }
}

class FancyVisitor extends TreeVis {
  ArrayList<TreeNode> evenDepthNodes = new ArrayList<>();
  ArrayList<TreeLeaf> greenLeaves = new ArrayList<>();

  public int getResult() {
    // absolute difference between the sum of values stored in the tree's non-leaf nodes at even depth and the sum of values stored in the tree's green leaf nodes
    int sumEvenNodes = evenDepthNodes.stream().mapToInt(Tree::getValue).sum();
    int sumGreenLeaves = greenLeaves.stream().mapToInt(Tree::getValue).sum();
    return Math.abs(sumEvenNodes - sumGreenLeaves);
  }

  public void visitNode(TreeNode node) {
    if ((node.getDepth() % 2) == 0){
      evenDepthNodes.add(node);
    }
  }

  public void visitLeaf(TreeLeaf leaf) {
    if (leaf.getColor() == Color.GREEN){
      greenLeaves.add(leaf);
    }
  }
}

public class TreeSolution {

  public static Tree solve() {
    //read the tree from STDIN and return its root as a return value of this function
    // The first line contains a single integer, , denoting the number of nodes in the tree. The second line contains  space-separated integers describing the respective values of . The third line contains  space-separated binary integers describing the respective values of .
    Scanner scanner = new Scanner(System.in);
    int numOfNodes = Integer.parseInt(scanner.nextLine());
    int[] values = new int[numOfNodes];
    int[] colors = new int[numOfNodes];
    HashMap<Integer, ArrayList<Integer>> edges = new HashMap<>();
    int[] numOfParents = new int[numOfNodes];

    StringTokenizer stk = new StringTokenizer(scanner.nextLine(), " ");
    for (int i = 0; i < values.length; i++){
      values[i] = Integer.parseInt(stk.nextToken());
    }

    stk = new StringTokenizer(scanner.nextLine(), " ");
    for (int i = 0; i < colors.length; i++){
      colors[i] = Integer.parseInt(stk.nextToken());
    }

    while((stk = new StringTokenizer(scanner.nextLine(), " ")).hasMoreElements()){
      int parent = Integer.parseInt(stk.nextToken()) - 1;
      int child = Integer.parseInt(stk.nextToken()) - 1;
      ArrayList<Integer> children = edges.getOrDefault(parent, new ArrayList<>());
      children.add(child);
      edges.put(parent,  children);
      numOfParents[child]++;
    }

    int root = -1;
    for(int i = 0; i < numOfParents.length; i++){
      if (numOfParents[i] == 0){
        root = i;
        break;
      }
    }

    Queue<Integer> queue = new LinkedList<>();
    queue.offer(root);
    TreeNode parent = null;
    TreeNode rootNode = null;

    while (!queue.isEmpty()){
      int currNodeInd = queue.poll();
      ArrayList<Integer> childrenIndexes = edges.get(currNodeInd);

      if (childrenIndexes == null){
        // it's a leaf
        assert parent != null;
        TreeLeaf leaf = new TreeLeaf(values[currNodeInd], (colors[currNodeInd] == 1)? Color.GREEN : Color.RED, parent.getDepth() + 1);
        parent.addChild(leaf);
      }else {
        TreeNode node = new TreeNode(values[currNodeInd], (colors[currNodeInd] == 1)? Color.GREEN : Color.RED, (parent == null)? 0 : (parent.getDepth() + 1));
        if (parent != null){
          parent.addChild(node);
        }else {
          rootNode = node;
        }
        parent = node;
        for (int c : childrenIndexes){
          queue.offer(c);
        }
      }

    }

    return rootNode;


  }


  public static void main(String[] args) {
    Tree root = solve();
    SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
    ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
    FancyVisitor vis3 = new FancyVisitor();

    root.accept(vis1);
    root.accept(vis2);
    root.accept(vis3);

    int res1 = vis1.getResult();
    int res2 = vis2.getResult();
    int res3 = vis3.getResult();

    System.out.println(res1);
    System.out.println(res2);
    System.out.println(res3);
  }
}