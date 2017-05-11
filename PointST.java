import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class PointST<Value> {
   private RedBlackBST<Point2D, Value> ST;

   // construct an empty symbol table of points 
   public PointST() {
      ST = new RedBlackBST<Point2D, Value>();
   }

   // is the symbol table empty? 
   public boolean isEmpty() {
      return ST.isEmpty();
   }

   // number of points 
   public int size() {
      return ST.size();
   }

   // associate the value val with point p
   public void put(Point2D p, Value val) {
      if (p == null || val == null)
         throw new java.lang.NullPointerException();
      
      ST.put(p, val);
   }

   // value associated with point p 
   public Value get(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();

      return ST.get(p);
   }

   // does the symbol table contain point p? 
   public boolean contains(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();
   
      return ST.contains(p);
   }

   // all points in the symbol table 
   public Iterable<Point2D> points() {
      return ST.keys();
   }

   // all points that are inside the rectangle 
   public Iterable<Point2D> range(RectHV rect) {
      if (rect == null)   throw new java.lang.NullPointerException();
      
      Queue<Point2D> q = new Queue<Point2D>();

      for (Point2D p : ST.keys())
         if (rect.contains(p))    q.enqueue(p);

      return q;
   }

   // a nearest neighbor to point p; null if the symbol table is empty 
   public Point2D nearest(Point2D p) {
      if (p == null)   throw new java.lang.NullPointerException();
      if (ST.isEmpty())    return null;

      Point2D n = new Point2D(0, 0);

      for (Point2D q : ST.keys())
         if (p.distanceSquaredTo(n) > p.distanceSquaredTo(q))    n = q;

      return n;
   }

   
   /****************
    * unit testing *
    ****************/
   public static void main(String[] args) {
      PointST<Integer> ST = new PointST<Integer>();
      
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

      Point2D p = new Point2D(15, 10);
      StdOut.println("\nPonto mais proximo de "+p.toString()+": "+ST.nearest(p));
   }
}
