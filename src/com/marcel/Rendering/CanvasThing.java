package com.marcel.Rendering;

import com.marcel.Rendering.Renderers.MainWindowRenderer;
import com.marcel.Rendering.Renderers.SidebarRenderer;
import com.marcel.Rendering.Renderers.TopMenuRenderer;

import javax.swing.*;
import java.awt.*;

public class CanvasThing extends JComponent
{
    private double deltaTime;
    private long oldTime;

    public int sizeX, sizeY, mouseX, mouseY, windowX1, windowX2, windowY1, windowY2;

    public SidebarRenderer sidebarRenderer = new SidebarRenderer();
    public TopMenuRenderer topMenuRenderer = new TopMenuRenderer();
    public MainWindowRenderer mainWindowRenderer = new MainWindowRenderer();


    public void updateSizes()
    {
        updateMousePosition();
        topMenuRenderer.mainRect.x1 = 0;
        topMenuRenderer.mainRect.x2 = sizeX - 1;
        topMenuRenderer.mainRect.y1 = 0;
        topMenuRenderer.mainRect.y2 = topMenuRenderer.topMenuHeight - 1;

        if (sidebarRenderer.shrinkSidebar)
        {
            if (sidebarRenderer.sidebarWidth > 0)
                sidebarRenderer.sidebarWidth -= deltaTime * (int)(sidebarRenderer.maxSidebarWidth * 5);
            else
                sidebarRenderer.sidebarWidth = 0;
        }
        else
        {
            if (sidebarRenderer.sidebarWidth < sidebarRenderer.maxSidebarWidth)
                sidebarRenderer.sidebarWidth += deltaTime * (int)(sidebarRenderer.maxSidebarWidth * 5);
            else
                sidebarRenderer.sidebarWidth = sidebarRenderer.maxSidebarWidth;
        }


        mainWindowRenderer.mainRect.x1 = 0;
        mainWindowRenderer.mainRect.x2 = sizeX - 1 - sidebarRenderer.sidebarWidth;
        mainWindowRenderer.mainRect.y1 = topMenuRenderer.topMenuHeight;
        mainWindowRenderer.mainRect.y2 = sizeY - 1;

        sidebarRenderer.mainRect.x1 = sizeX - sidebarRenderer.sidebarWidth;
        sidebarRenderer.mainRect.x2 = sizeX - 1;
        sidebarRenderer.mainRect.y1 = topMenuRenderer.topMenuHeight;
        sidebarRenderer.mainRect.y2 = sizeY - 1;

        deltaTime = 1;
        oldTime = System.currentTimeMillis();
    }

    public CanvasThing()
    {
        sizeX = getWidth();
        sizeY = getHeight();
        topMenuRenderer.topMenuHeight = 50;
        sidebarRenderer.maxSidebarWidth = 300;
        sidebarRenderer.sidebarWidth = sidebarRenderer.maxSidebarWidth;
        sidebarRenderer.shrinkSidebar = false;
        //updateSizes();
        topMenuRenderer.init();
        sidebarRenderer.init();
        mainWindowRenderer.init();

        windowX1 = 0;
        windowY1 = 0;
    }


    private void updateMousePosition()
    {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        mouseX = (int) b.getX() - getLocationOnScreen().x;
        mouseY = (int) b.getY() - getLocationOnScreen().y;

        sizeX = getWidth();
        sizeY = getHeight();
        windowX2 = sizeX - 1;
        windowY2 = sizeY - 1;
    }


    public static void fillRectFromPoints(Graphics2D g2, int x1, int y1, int x2, int y2)
    {
        g2.fillRect(x1, y1, (x2 - x1) + 1, (y2 - y1) + 1);
    }


    @Override
    public void paintComponent(Graphics g)
    {
        long cTime = System.currentTimeMillis();
        deltaTime = (cTime - oldTime) / 1000.0;
        if (deltaTime < 0.001)
            deltaTime = 0.001;
        oldTime = cTime;
        //System.out.println("DELTA: " + deltaTime);

        updateSizes();
        if (!(g instanceof Graphics2D) || !MainRenderController.allowDrawing)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

//        g2.setColor(Color.DARK_GRAY);
//        fillRectFromPoints(g2, windowX1, windowY1, windowX2, windowY2);
//        g2.setColor(Color.ORANGE);
//        g2.setStroke(new BasicStroke(2));
//        g2.drawLine(windowX1, windowY1, windowX2, windowY2);

        mainWindowRenderer.draw(g2);
        sidebarRenderer.draw(g2);
        topMenuRenderer.draw(g2);
        if (MainRenderController.dragCable)
            renderTempLine(g2);

        {
            g2.setColor(Color.red);
            g2.fillRect(mouseX -1, mouseY-1, 3, 3);
        }
    }

    public void renderTempLine(Graphics2D g2)
    {
        {
            int sX = (int)((MainRenderController.cableFrom.x - mainWindowRenderer.scrollX) * mainWindowRenderer.zoomLevel + mainWindowRenderer.mainRect.x1);
            int sY = (int)((MainRenderController.cableFrom.y - mainWindowRenderer.scrollY) * mainWindowRenderer.zoomLevel + mainWindowRenderer.mainRect.y1);

            g2.setStroke(new BasicStroke(4 * mainWindowRenderer.zoomLevel));
            g2.setColor(Color.orange);
            g2.drawLine(mouseX, mouseY,
                    (sX + (int)(2.5 * mainWindowRenderer.zoomLevel)),
                    (sY + (int)(2.5 * mainWindowRenderer.zoomLevel))
            );
        }
    }


    public void update()
    {
        repaint();
    }
}
