package knu.lsy;

import org.json.JSONObject;
import knu.lsy.shapes.ShapeGenerator;


public class BACKEND_MANAGER {

    public static void EXEC_TASK(JSONObject jsonResponse) throws Exception {
        JSONObject reqJson = jsonResponse.getJSONObject("REQ");
        JSONObject resJson = jsonResponse.getJSONObject("RES");

        String action = reqJson.getString("Action");

        switch (action) {
            case "ShapesOverlaps":
                if (!reqJson.has("Width") || !reqJson.has("Height") ||
                        !reqJson.has("RadiusMax") || !reqJson.has("HowMany") ||
                        !reqJson.has("MaxEdges")) {
                    throw new Exception("필수 파라미터가 누락되었습니다.");
                }

                try {
                    int width = Integer.parseInt(reqJson.getString("Width"));
                    int height = Integer.parseInt(reqJson.getString("Height"));
                    int radiusMax = Integer.parseInt(reqJson.getString("RadiusMax"));
                    int howMany = Integer.parseInt(reqJson.getString("HowMany"));
                    int maxEdges = Integer.parseInt(reqJson.getString("MaxEdges"));

                    if (width <= 0 || height <= 0 || radiusMax <= 0 ||
                            howMany <= 0 || maxEdges < 3) {
                        throw new Exception("파라미터 값이 올바르지 않습니다.");
                    }

                    ShapeGenerator generator = new ShapeGenerator();
                    JSONObject shapesData = generator.generateShapes(width, height,
                            radiusMax, howMany, maxEdges);

                    resJson.put("RESULT", shapesData);

                } catch (NumberFormatException e) {
                    throw new Exception("숫자 파라미터 형식이 올바르지 않습니다.");
                }
                break;

            default:
                throw new Exception("지원하지 않는 Action입니다: " + action);
        }
    }
}
