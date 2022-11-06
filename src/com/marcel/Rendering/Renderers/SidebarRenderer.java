package com.marcel.Rendering.Renderers;

import com.marcel.Rendering.CanvasThing;
import com.marcel.Rendering.Utils.WindowRect;

import java.awt.*;

public class SidebarRenderer
{
    public WindowRect mainRect = new WindowRect();
    public int sidebarWidth, maxSidebarWidth;
    public Color sidebarCol = new Color(205,216,207);
    public boolean shrinkSidebar;
    public Font sidebarFont = new Font(Font.SANS_SERIF, Font.BOLD, 25);

    public Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
    public Color defaultFontColor = new Color(20,20,40);

    public void init()
    {

    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(sidebarCol);
        CanvasThing.fillRectFromPoints(g2, mainRect.x1, mainRect.y1, mainRect.x2, mainRect.y2);

        g2.setFont(sidebarFont);
        var metrics = g2.getFontMetrics(sidebarFont);
        g2.setColor(defaultFontColor);

        String text = "Logic Gates";

        g2.drawString(text, mainRect.x1 + (int)((maxSidebarWidth - metrics.getStringBounds(text, (Graphics) g2).getWidth()) / 2), mainRect.y1 + 35);


    }
}