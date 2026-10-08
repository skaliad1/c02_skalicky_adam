package rasterizer;

import view.Panel;

public class LineRasterizer {
    private Panel panel;

    public LineRasterizer(Panel panel) {
        this.panel = panel;
    }

    public void rasterize(int x1, int y1, int x2, int y2, int color) {

        //DDA Algoritmus
        float dx = x2 - x1;
        float dy = y2 - y1;

        float steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            panel.drawPixel(x1, y1, color);
            return;
        }

        float xInc = dx / steps;
        float yInc = dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            panel.drawPixel(Math.round(x), Math.round(y), color);
            x += xInc;
            y += yInc;
        }
    }
}