package com.kmortyk.canekoandthechessking.util;

public class GameMath {

    /* --- control ------------------------------------------------------------------------------- */

    private static float sign(Vector2 p1, Vector2 p2, Vector2 p3) { return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y); }

    public static boolean pointInTriangle (Vector2 pt, Vector2 v1, Vector2 v2, Vector2 v3) {

        float d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(pt, v1, v2);
        d2 = sign(pt, v2, v3);
        d3 = sign(pt, v3, v1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);

    }

}
