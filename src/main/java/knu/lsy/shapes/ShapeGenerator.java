package knu.lsy.shapes;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class ShapeGenerator {
    private Random random;

    public ShapeGenerator() {
        this.random = new Random();
    }

    public JSONObject generateShapes(int width, int height, int radiusMax, int howMany, int maxEdges) {
        List<Shape> shapes = new ArrayList<>();

        // 도형 생성 (원: 20%, 정다각형: 25%, 일반다각형: 55%)
        for (int i = 0; i < howMany; i++) {
            double probability = random.nextDouble();

            // 무작위 중심점 생성
            double centerX = radiusMax + random.nextDouble() * (width - 2 * radiusMax);
            double centerY = radiusMax + random.nextDouble() * (height - 2 * radiusMax);
            Point center = new Point(centerX, centerY);

            // 무작위 반경
            double radius = 10 + random.nextDouble() * (radiusMax - 10);

            Shape shape;
            if (probability < 0.20) {
                shape = new Circle(center, radius);
            } else if (probability < 0.45) {
                int sides = 3 + random.nextInt(maxEdges - 2);
                double rotation = random.nextDouble() * 2 * Math.PI;
                shape = new RegularPolygon(center, radius, sides, rotation);
            } else {
                int vertices = 3 + random.nextInt(maxEdges - 2);
                shape = new IrregularPolygon(center, radius, vertices);
            }

            shapes.add(shape);
        }

        // 연쇄적 그룹화 처리
        List<Set<String>> overlapGroups = findConnectedComponents(shapes);
        assignGroupColors(shapes, overlapGroups);

        // JSON 응답 생성
        JSONObject response = new JSONObject();
        JSONArray shapesArray = new JSONArray();

        for (Shape shape : shapes) {
            shapesArray.put(shape.toJSON());
        }

        response.put("shapes", shapesArray);
        response.put("totalCount", shapes.size());
        response.put("overlapGroups", convertGroupsToJSON(overlapGroups));

        return response;
    }

    // Union-Find를 사용한 연결 요소 찾기 (연쇄적 그룹화 해결)
    private List<Set<String>> findConnectedComponents(List<Shape> shapes) {

        return null;
    }

    private void assignGroupColors(List<Shape> shapes, List<Set<String>> groups) {
        Map<String, Shape> shapeMap = new HashMap<>();
        for (Shape shape : shapes) {
            shapeMap.put(shape.getId(), shape);
        }

        String[] COLORS = {
                "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF",
                "#00FFFF", "#FFA500", "#800080", "#008000", "#000080"
        };

        for (int i = 0; i < groups.size(); i++) {
            Set<String> group = groups.get(i);
            if (group.size() > 1) {
                String color = COLORS[i % COLORS.length];
                for (String shapeId : group) {
                    Shape shape = shapeMap.get(shapeId);
                    if (shape != null) {
                        shape.setColor(color);
                    }
                }
            }
        }
    }

    private JSONArray convertGroupsToJSON(List<Set<String>> groups) {
        JSONArray groupsArray = new JSONArray();
        String[] COLORS = {
                "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF",
                "#00FFFF", "#FFA500", "#800080", "#008000", "#000080"
        };

        for (int i = 0; i < groups.size(); i++) {
            Set<String> group = groups.get(i);
            if (group.size() > 1) {
                JSONObject groupJson = new JSONObject();
                JSONArray shapeIds = new JSONArray();

                for (String shapeId : group) {
                    shapeIds.put(shapeId);
                }

                groupJson.put("shapeIds", shapeIds);
                groupJson.put("color", COLORS[i % COLORS.length]);
                groupJson.put("size", group.size());

                groupsArray.put(groupJson);
            }
        }

        return groupsArray;
    }
}