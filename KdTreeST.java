import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class KdTreeST<Value> {
   private Node root;
   private int n;

   private class Node {
      private Point2D key;
      private Value val;
      private Node right, left;

      public Node(Point2D key, Value val) {
         this.key = key;
         this.val = val;
      }
   }

   // construct an empty symbol table of points 
   public KdTreeST() {
      root = null;
      n = 0;
   }

   // is the symbol table empty? 
   public boolean isEmpty() {
      return n == 0;
   }

   // number of points
   public int size() {
      return n;
   }

   // associate the value val with point p
   public void put(Point2D p, Value val) {
      if (p == null || val == null)
         throw new java.lang.NullPointerException();

      root = put(root, p, val, true);
   }

   // value associated with point p 
   public Value get(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();

      Node x = get(root, p, true);

      if (x == null)    return null;
      return x.val;
   }

   // does the symbol table contain point p? 
   public boolean contains(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();

      return get(root, p, true) != null;
   }

   // all points in the symbol table 
   public Iterable<Point2D> points() {
      Queue<Point2D> q = new Queue<Point2D>();
      Queue<Node> p = new Queue<Node>();

      if (root != null) {
         q.enqueue(root.key);
         p.enqueue(root);

         q = points(q, p, 1);
      }

      return q;
   }

   // all points that are inside the rectangle 
   public Iterable<Point2D> range(RectHV rect) {
      if (rect == null)   throw new java.lang.NullPointerException();

      Queue<Point2D> q = new Queue<Point2D>();

      return range(q, root, rect);
   }

   // a nearest neighbor to point p; null if the symbol table is empty 
   public Point2D nearest(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();

      return root.key;
   }
/*
   // see Challenge for the bored
   public Iterable<Point2D> nearest(Point2D p, int k) {
      if (p == null)   throw new java.lang.NullPointerException();
   }


   /**********************
    * Auxiliar functions *
    **********************/
   // compares the x coordinates of Point2D p and Point2D q
   private int compareXCoord(Point2D p, Point2D q) {
      double px = p.x();
      double qx = q.x();

      if (px < qx)    return -1;
      if (px > qx)    return 1;
      
      return 0;
   }

   // compares the y coordinates of Point2D p and Point2D q
   private int compareYCoord(Point2D p, Point2D q) {
      double py = p.y();
      double qy = q.y();

      if (py < qy)    return -1;
      if (py > qy)    return 1;

      return 0;
   }

   // recursive version of put()
   private Node put(Node node, Point2D p, Value val, boolean x_coord) {
      if (node == null) {
         n++;

         return new Node(p, val);
      }

      int cmp, cmp2;

      if (x_coord) {
         cmp = compareXCoord(node.key, p);
         cmp2 = compareYCoord(node.key, p);
      }

      else {
         cmp = compareYCoord(node.key, p);
         cmp2 = compareXCoord(node.key, p);
      }

      if (cmp > 0)    node.left = put(node.left, p, val, !x_coord);
      else if (cmp == 0 && cmp2 == 0)    node.val = val;
      else   node.right = put(node.right, p, val, !x_coord);

      return node;
   }

   // recursive version of get()
   private Node get(Node node, Point2D p, boolean x_coord) {
      if (node == null)    return null;

      int cmp;

      if (x_coord)    cmp = compareXCoord(node.key, p);
      else    cmp = compareYCoord(node.key, p);

      if (cmp > 0)    return get(node.left, p, !x_coord);
      if (cmp < 0)    return get(node.right, p, !x_coord);
      return node;
   }

   // recursive version of points()
   private Queue<Point2D> points(Queue<Point2D> q, Queue<Node> p, int num) {
      if (p.isEmpty())    return q;

      int counter = 0;

      for (int i = 0; i < num; i++) {
         Node node = p.dequeue();

         if (node.left != null) {
            q.enqueue(node.left.key);
            p.enqueue(node.left);
            
            counter++;
         }

         if (node.right != null) {
            q.enqueue(node.right.key);
            p.enqueue(node.right);

            counter++;
         }
      }

      return points(q, p, counter);
   }

   private Queue<Point2D> range(Queue<Point2D> queue, Node node, RectHV r) {
      if (node == null)    return queue;

      if (r.contains(node.key))    queue.enqueue(node.key);

      double[] coord = new double[4];
      coord[0] = coord[1] = node.key.x();
      coord[2] = coord[3] = node.key.y();
      coord = treeRectangle(node.left, coord);
     
      if (r.intersects(new RectHV(coord[0], coord[2], coord[1], coord[3])))
         queue = range(queue, node.left, r);

      coord = treeRectangle(node.right, coord);

      if (r.intersects(new RectHV(coord[0], coord[2], coord[1], coord[3])))
         queue = range(queue, node.right, r);

      return queue;
   }

   private double[] treeRectangle(Node node, double[] c) {
      if (node == null)    return c;

      if (node.key.x() < c[0])    c[0] = node.key.x();
      else if (node.key.x() > c[1])    c[1] = node.key.x();

      if (node.key.y() < c[2])    c[2] = node.key.y();
      else if (node.key.y() > c[3])    c[3] = node.key.y();

      return treeRectangle(node.right, treeRectangle(node.left, c));
   }

   private int height(Node r) {
      if (r == null)    return -1;

      int h_left = height(r.left);
      int h_right = height(r.right);

      return 1+Math.max(h_left, h_right);
   }

   
   /****************
    * unit testing *
    ****************/
   public static void main(String[] args) {
      KdTreeST<Integer> ST = new KdTreeST<Integer>();
      
      ST.put(new Point2D(15, 20), 0);
      ST.put(new Point2D(10, 5), 2);
      ST.put(new Point2D(3, 9), -5);
      ST.put(new Point2D(18, 7), 11);
      ST.put(new Point2D(17, 9), 3);
      ST.put(new Point2D(11, 5), 2);
      ST.put(new Point2D(15, 5), 9);
      
      StdOut.println("numero de pontos inseridos = "+ST.size());

      for (Point2D p : ST.points())
         StdOut.println(p.toString());

      RectHV r = new RectHV(10, 0, 15, 10);

      StdOut.println("\nPontos dentro do retangulo "+r.toString()+":");
      for (Point2D p : ST.range(r))
         StdOut.println(p.toString());
   }
}
