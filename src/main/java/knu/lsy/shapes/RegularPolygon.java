package knu.lsy.shapes;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

import static knu.lsy.utils.MathUtil.EPSILON;

public class RegularPolygon extends Shape {
    private int sides;
    private double rotationAngle;
    private List<Point> vertices;

    public RegularPolygon(Point center, double radius, int sides, double rotationAngle) {
        super(center, radius);
        this.sides = sides;
        this.rotationAngle = rotationAngle;
        this.vertices = generateVertices();
    }

    private List<Point> generateVertices() {
        List<Point> points = new ArrayList<>();
        double angleStep = 2 * Math.PI / sides;

        for (int i = 0; i < sides; i++) {
            double angle = angleStep * i + rotationAngle;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            points.add(new Point(x, y));
        }

        return points;
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other.getShapeType().equals("circle")) {
            return other.overlaps(this);
        } else if (other.getShapeType().equals("regularPolygon") ||
                other.getShapeType().equals("irregularPolygon")) {
            List<Point> axes = getSeparatingAxes(this.getVertices());
            axes.addAll(getSeparatingAxes(other.getVertices())); // 양쪽 모두 검사

            for (Point axis : axes) {
                double[] projection1 = projectOntoAxis(this.getVertices(), axis);
                double[] projection2 = projectOntoAxis(other.getVertices(), axis);

                if (!rangesOverlap(projection1[0], projection1[1], projection2[0], projection2[1])) {
                    return false; // 분리 축 발견
                }
            }

            return true; // 모든 축에서 겹침
        }

        return false;
    }

    // 각 변마다 법선(수직 벡터)을 계산
    private List<Point> getSeparatingAxes(List<Point> vertices) {
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

    // 도형을 주어진 축에 투영 -> [최솟값, 최댓값]
    private double[] projectOntoAxis(List<Point> vertices, Point axis) {
        double min = dotProduct(vertices.getFirst(), axis);
        double max = min;

        for (int i = 1; i < vertices.size(); i++) {
            double proj = dotProduct(vertices.get(i), axis);
            if (proj < min) min = proj;
            if (proj > max) max = proj;
        }

        return new double[]{min, max};
    }

    // 두 투영 범위가 겹치는지 확인
    private boolean rangesOverlap(double minA, double maxA, double minB, double maxB) {
        return !(maxA < minB - EPSILON || maxB < minA - EPSILON);
    }

    // 벡터 내적
    private double dotProduct(Point a, Point b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "regularPolygon");
        json.put("id", id);
        json.put("center", center.toJSON());
        json.put("radius", radius);
        json.put("sides", sides);
        json.put("rotationAngle", rotationAngle);
        json.put("color", color);

        JSONArray verticesArray = new JSONArray();
        for (Point vertex : vertices) {
            verticesArray.put(vertex.toJSON());
        }
        json.put("vertices", verticesArray);

        return json;
    }

    @Override
    public String getShapeType() {
        return "regularPolygon";
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }
}