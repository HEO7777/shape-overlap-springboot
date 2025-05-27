package knu.lsy.shapes;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

import static knu.lsy.utils.MathUtil.EPSILON;

public class Circle extends Shape {

    public Circle(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public boolean overlaps(Shape other) {

        if (other.getShapeType().equals("circle")) {
            // 1. 다른 도형이 원인 경우: 두 원의 중심 거리가 반지름의 합보다 작은지 확인
            double distance = center.distanceTo(other.getCenter());
            return (distance <= radius + other.getRadius() + EPSILON);
        } else if (other.getShapeType().equals("irregularPolygon") ||
                other.getShapeType().equals("regularPolygon")) {
            // 2. 다른 도형이 다각형인 경우: 다각형의 모든 정점 중
            // 적어도 하나의 정점이 원 안에 있는지 확인
            for (Point vertex : other.getVertices()) {
                if (vertex.distanceTo(center) <= radius + EPSILON) {
                    return true;
                }
            }

            // 또는 다각형의 모든 변이 원과 교차하는지 확인
            List<Point> vertices = other.getVertices();
            int size = vertices.size();
            for (int i = 0; i < size; ++i) {
                Point p1 = vertices.get(i);
                Point p2 = vertices.get((i + 1) % size);

                if (lineSegmentIntersectsCircle(p1, p2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean lineSegmentIntersectsCircle(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();

        double fx = p1.getX() - center.getX();
        double fy = p1.getY() - center.getY();

        double a = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);
        double b_half = fx * dx + fy * dy;
        double c = Math.pow(fx, 2.0) + Math.pow(fy, 2.0) - Math.pow(radius, 2.0);

        double discriminant_4 = Math.pow(b_half, 2.0) - a * c;
        if (discriminant_4 < -EPSILON) {
            return false;
        }

        discriminant_4 = Math.sqrt(discriminant_4);
        double k1 = (-b_half + discriminant_4) / a;
        double k2 = (-b_half - discriminant_4) / a;

        return (-EPSILON <= k1 && k1 <= 1.0 + EPSILON) || (-EPSILON <= k2 && k2 <= 1.0 + EPSILON);
    }


    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "circle");
        json.put("id", id);
        json.put("center", center.toJSON());
        json.put("radius", radius);
        json.put("color", color);
        return json;
    }

    @Override
    public String getShapeType() {
        return "circle";
    }

    @Override
    public List<Point> getVertices() {
        // 원의 경계를 근사하는 점들 생성
        List<Point> vertices = new ArrayList<>();
        int numPoints = 32;
        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            vertices.add(new Point(x, y));
        }
        return vertices;
    }
}