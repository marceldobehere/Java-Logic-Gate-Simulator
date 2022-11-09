package com.marcel.Rendering.Renderers;

import com.marcel.Rendering.CanvasThing;
import com.marcel.Rendering.MainRenderController;
import com.marcel.Rendering.Utils.WindowRect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopMenuRenderer
{
    public WindowRect mainRect = new WindowRect();
    public int topMenuHeight;
    public Color topMenuCol = new Color(131,157,175),
            topMenuTextCol= new Color(20,20,40),
            topMenuHighlightedTextCol= new Color(220,220,240);;
    public Font topMenuFont = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
    public enum SelectedTextEnum
    {
        NONE,
        SAVE,
        LOAD,
        DEATH
    }
    public List<String> menuText = new ArrayList<>();
    public List<SelectedTextEnum> menuTextEnum = new ArrayList<>();

    public SelectedTextEnum selectedText;

    public void init()
    {
        selectedText = SelectedTextEnum.NONE;

        menuText.add("SAVE");
        menuTextEnum.add(SelectedTextEnum.SAVE);
        menuText.add("LOAD");
        menuTextEnum.add(SelectedTextEnum.LOAD);
        menuText.add("DEATH");
        menuTextEnum.add(SelectedTextEnum.DEATH);
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(topMenuCol);
        CanvasThing.fillRectFromPoints(g2, mainRect.x1, mainRect.y1, mainRect.x2, mainRect.y2);

        int mouseX = MainRenderController.canvas.mouseX;
        int mouseY = MainRenderController.canvas.mouseY;

        g2.setFont(topMenuFont);
        var metrics = g2.getFontMetrics(topMenuFont);

        int xPos = mainRect.x1 + 10;
        selectedText = SelectedTextEnum.NONE;
        for (int i = 0; i < menuText.size(); i++)
        {
            String text = menuText.get(i);
            var bounds = metrics.getStringBounds(text, (Graphics) g2);
            if (mouseX >= xPos && mouseX <= xPos + bounds.getWidth() &&
                    mouseY >= 0 && mouseY <= topMenuHeight)
            {
                g2.setColor(topMenuHighlightedTextCol);
                selectedText = menuTextEnum.get(i);
            }
            else
                g2.setColor(topMenuTextCol);


            //g2.drawRect(xPos, 0, (int)bounds.getWidth(), topMenuHeight);
            g2.drawString(text, xPos, mainRect.y1 + 35);
            xPos +=  (int)bounds.getWidth() + 30;
        }
    }
}