package com.stripe.interview.challenge;

/*
A simple class for a directed Graph with Nodes having ids and a list of children
The graph support TopologicalSort
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
  public static void main(String[] args) {
    Graph graph = new Graph();



  }



  public static class GraphNode {
    int id;
    List<GraphNode> children;

    public GraphNode(int id) {
      this.id = id;
      children = new ArrayList<>();
    }

    public List<GraphNode> getChildren() {
      return children;
    }
  }

  public static class Graph{
    List<GraphNode> nodes;

    public Graph() {
      nodes = new ArrayList<>();
    }

    public List<GraphNode> getNodes() {
      return nodes;
    }

    public void addNodes(GraphNode node) {
      this.nodes.add(node);
    }

    public List<GraphNode> topologicalSort(){
      List<GraphNode> sorted = new ArrayList<>();
      Queue<GraphNode> nodeQueue = new LinkedList<>();

      if(nodes.isEmpty()){
        return nodes;
      }
      nodeQueue.offer(nodes.get(0));
      sorted.add(nodes.get(0));

      while(!nodeQueue.isEmpty()){
        GraphNode curr = nodeQueue.poll();
        sorted.add(curr);

        for(GraphNode c : curr.getChildren()){
          if(!sorted.contains(c)){
            nodeQueue.offer(c);
          }
        }
      }

      return sorted;
    }
  }
}
