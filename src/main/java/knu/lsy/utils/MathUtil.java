package knu.lsy.utils;

import knu.lsy.shapes.Point;

import java.util.List;

public class MathUtil {
    public static final double EPSILON = 1e-8;

    // 도형을 주어진 축에 투영 -> [최솟값, 최댓값]
    public static double[] projectOntoAxis(List<Point> vertices, Point axis) {
        double min = dotProduct(vertices.getFirst(), axis);
        double max = min;

        for (int i = 1; i < vertices.size(); i++) {
            double proj = dotProduct(vertices.get(i), axis);
            if (proj < min) min = proj;
            if (proj > max) max = proj;
        }

        return new double[]{min, max};
    }

    // 벡터 내적
    public static double dotProduct(Point a, Point b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

}
