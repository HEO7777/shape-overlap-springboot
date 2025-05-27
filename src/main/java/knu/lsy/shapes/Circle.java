package knu.lsy.shapes;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

import static knu.lsy.utils.CollisionUtil.*;

public class Circle extends Shape {

    public Circle(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other.getShapeType().equals("circle")) {
            return checkCircleCircleOverlap(this, (Circle)other);
        } else if (other.getShapeType().equals("irregularPolygon") ||
                other.getShapeType().equals("regularPolygon")) {
            return checkCirclePolygonOverlap(this, other);
        }

        return false;
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