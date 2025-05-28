package knu.lsy.utils;

import knu.lsy.shapes.Circle;
import knu.lsy.shapes.Point;
import knu.lsy.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

import static knu.lsy.utils.MathUtil.*;

public class CollisionUtil {
    public static boolean checkCircleCircleOverlap(Circle thisCircle, Circle otherCircle) {
        // 원, 원인 경우: 두 원의 중심 거리가 반지름의 합보다 작은지 확인
        double distance = thisCircle.getCenter().distanceTo(otherCircle.getCenter());
        return (distance <= thisCircle.getRadius() + otherCircle.getRadius() + EPSILON);
    }


    public static boolean checkCirclePolygonOverlap(Circle thisCircle, Shape otherPolygon) {
        // 원, 다각형인 경우: 다각형의 모든 정점 중
        // 적어도 하나의 정점이 원 안에 있는지 확인
        for (Point vertex : otherPolygon.getVertices()) {
            if (vertex.distanceTo(thisCircle.getCenter()) <= thisCircle.getRadius() + EPSILON) {
                return true;
            }
        }

        // 또는 다각형에서 적어도 하나의 변이 원과 교차하는지 확인
        List<Point> vertices = otherPolygon.getVertices();
        int size = vertices.size();
        for (int i = 0; i < size; ++i) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % size);

            if (lineSegmentIntersectsCircle(thisCircle, p1, p2)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkConvexPolygonPolygonOverlap(Shape thisPolygon, Shape otherPolygon) {
        // 볼록 다각형, 볼록 다각형인 경우
        // thisPolygon의 분리축들 생성 및 추가
        List<Point> axes = getSeparatingAxes(thisPolygon.getVertices());
        // otherPolygon의 분리축들 생성 및 추가
        axes.addAll(getSeparatingAxes(otherPolygon.getVertices()));

        // 각 축에 대해
        for (Point axis : axes) {
            // 모든 정점을 투영
            double[] projection1 = projectOntoAxis(thisPolygon.getVertices(), axis);
            double[] projection2 = projectOntoAxis(otherPolygon.getVertices(), axis);

            // 겹침 검사
            if (!rangesOverlap(projection1[0], projection1[1], projection2[0], projection2[1])) {
                return false; // 분리 축 발견
            }
        }

        return true; // 모든 축에서 겹침
    }

    public static List<Point> getSeparatingAxes(List<Point> vertices) {
        // 각 변마다 법선(수직 벡터)을 계산
        List<Point> axes = new ArrayList<>();
        int size = vertices.size();

        for (int i = 0; i < size; i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % size);

            // 방향 벡터 (edge)
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();

            // 법선 벡터 (수직)
            Point normal = new Point(-dy, dx);

            // 단위 벡터로 정규화
            double length = Math.sqrt(normal.getX() * normal.getX() + normal.getY() * normal.getY());
            if (EPSILON < length) {
                normal = new Point(normal.getX() / length, normal.getY() / length);
            }

            axes.add(normal);
        }

        return axes;
    }

    public static boolean rangesOverlap(double minA, double maxA, double minB, double maxB) {
        // 두 투영 범위가 겹치는지 확인
        return !(maxA < minB - EPSILON || maxB < minA - EPSILON);
    }

    public static boolean lineSegmentIntersectsCircle(Circle thisCircle, Point p1, Point p2) {
        // 원 중심에서 선분까지의 최소 거리가 반지름보다 작거나 같은지 검사
        // 선분 (p1, p2)와 원 (center, radius)의 교차 여부 계산
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();

        double fx = p1.getX() - thisCircle.getCenter().getX();
        double fy = p1.getY() - thisCircle.getCenter().getY();

        double a = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);
        double b_half = fx * dx + fy * dy;
        double c = Math.pow(fx, 2.0) + Math.pow(fy, 2.0) - Math.pow(thisCircle.getRadius(), 2.0);

        double discriminant_4 = Math.pow(b_half, 2.0) - a * c;
        if (discriminant_4 < -EPSILON) {
            return false;
        }

        discriminant_4 = Math.sqrt(discriminant_4);
        double k1 = (-b_half + discriminant_4) / a;
        double k2 = (-b_half - discriminant_4) / a;

        return (-EPSILON <= k1 && k1 <= 1.0 + EPSILON) || (-EPSILON <= k2 && k2 <= 1.0 + EPSILON);
    }

}
