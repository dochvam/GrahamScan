#Graham Scan Algorithm

Finds the convex hull of a set of randomly generated points; animates the search.

###Details

This program uses Graham's scanning algorithm. First, points are obtained from Adam Knapp's GenericPointGenerator. Then the points are sorted by their slope to the leftmost point. Then each point is added to a stack and tested; if at any point the next point added creates a clockwise turn rather than a counterclockwise one, the points are removed from the stack until the problem is fixed.

###How to execute
Call GrahamScan. The optional first command line argument is an integer specifying the number of points in the point cloud.