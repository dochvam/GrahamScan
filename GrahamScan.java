import java.awt.geom.Point2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.*;

/**
 * @author knappa
 */
public class GrahmScan {

	public static void main(String[] args) {
		ArrayList<Point2D> points = GenericPointGenerator.getPoints(500);

		//find the leftmost point
		double leftmost = 0;
		int leftIndex = 0;

		for(int i = 0; i < points.size(); i++) {
			double current = points.get(i).getX();
			if (current < leftmost){
				leftmost = current;
				leftIndex = i;
			}
		}

		//move ptP to the front of the list
		Point2D temp = points.get(leftIndex);
		points.set(leftIndex, points.get(0));
		points.set(0, temp);

		Point2D ptP = points.get(0);

		//sort all points by slope to ptP

		Comparator<Point2D> slopeToPtP = new Comparator<Point2D>() {
			@Override
			public int compare(Point2D pt1, Point2D pt2) {

				//if both points are below or above the reference point, swap the order
				int corrector = 1;
				if (pt1.getY() > ptP.getY() && pt2.getY() > ptP.getY()) corrector = -1;
				if (pt1.getY() < ptP.getY() && pt2.getY() < ptP.getY()) corrector = -1;

				if (getSlope(pt1, ptP) < getSlope(pt2, ptP)) return corrector*-1;
				else return corrector*1;

			}
		};

		points.sort(slopeToPtP);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-4,4);
		drawPoints(points);

		Stack<Point2D> stack = new Stack<Point2D>();
		stack.push(points.get(0));

		boolean complete = false;
		Point2D q = points.get(1);
		int loc = 2;

		while (!complete){
			Point2D r = points.get(loc);
			if( ccw(stack.peek(), q, r) < 0 ){
				stack.push(q);
				q = r;
				loc++;
			}
			else{
				q = stack.pop();
			}
			if (loc == 500){
				complete = true;
				stack.push(q);
				stack.push(points.get(0));
			}
			drawPoints(points);

			if (stack.peek() != ptP){
				Stack<Point2D> sclone = (Stack<Point2D>)stack.clone();
				drawLines(sclone);
			}

			StdDraw.setPenRadius(0.004);
			StdDraw.setPenColor(Color.RED);
			StdDraw.line(q.getX(), q.getY(), ptP.getX(), ptP.getY());
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.show();
		}

		drawLines(stack);
		StdDraw.show();


	}

	/**
	 * Determine if the angle p1-p2-p3 is clockwise or counterclockwise
	 *
	 * @param p1 1st point
	 * @param p2 2nd point
	 * @param p3 3rd point
	 * @return positive number if the angle p1-p2-p3 is counterclockwise,
	 * negative if clockwise, zero if the 3 points are colinear.
	 */
	private static double ccw(Point2D p1, Point2D p2, Point2D p3) {
		// compute coordinates of vectors from p2 to p1 and p3
		double v1x = p1.getX() - p2.getX();
		double v1y = p1.getY() - p2.getY();

		double v2x = p3.getX() - p2.getX();
		double v2y = p3.getY() - p2.getY();

		// desired quantity is 3rd component of cross product (other
		// components are zero as these vectors are 2d)
		return v1x * v2y - v2x * v1y;
	}

	public static double getSlope(Point2D p1, Point2D p2) {
		double slope = (p1.getX() - p2.getX()) / (p1.getY() - p2.getY());
		return slope;

	}

	public static void drawPoints(ArrayList<Point2D> points){
		StdDraw.clear();
		StdDraw.setPenRadius(0.01);
		for (int i = 0; i<500; i++) {
			if (i == 0) StdDraw.setPenColor(Color.RED);
			else StdDraw.setPenColor(Color.BLACK);
			StdDraw.point(points.get(i).getX(), points.get(i).getY());
		}
		StdDraw.show();
	}

	public static void drawLines(Stack<Point2D> stack){
		Point2D dp1 = stack.pop();
		Point2D dp2 = stack.pop();

		StdDraw.setPenRadius(0.004);
		boolean finished = false;

		while (!finished){
			if (stack.isEmpty()) finished = true;
			StdDraw.line(dp1.getX(), dp1.getY(),
					dp2.getX(), dp2.getY());
			if (!finished){
				dp1 = dp2;
				dp2 = stack.pop();
			}
		}
	}	
}

