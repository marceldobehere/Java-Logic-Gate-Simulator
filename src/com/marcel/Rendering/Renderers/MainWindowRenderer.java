package com.marcel.Rendering.Renderers;

import com.marcel.Rendering.CanvasThing;
import com.marcel.Rendering.Utils.DPos;
import com.marcel.Rendering.Utils.WindowRect;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindowRenderer {
    public WindowRect mainRect = new WindowRect();
    public Color mainWindowCol = new Color(206,206,206);
    public float zoomLevel = 1;
    public float scrollX = 0;
    public float scrollY = 0;

    public float oldZoom = 1;

    public List<LogicComponent> logicGates = new ArrayList<LogicComponent>();

    public void init()
    {
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.AND, new DPos(80, 50)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.OR, new DPos(200, 50)));
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(mainWindowCol);
        CanvasThing.fillRectFromPoints(g2, mainRect.x1, mainRect.y1, mainRect.x2, mainRect.y2);
        drawGates(g2);
    }

    public void drawGates(Graphics2D g2)
    {
        for (LogicComponent gate : logicGates)
        {
            drawGate(g2, gate);
        }
    }

    public LogicComponent GetComponentAt(int mX, int mY)
    {
        for (int t = logicGates.size() - 1; t >= 0; t--)
        {
            LogicComponent gate = logicGates.get(t);


            if (gate.type == LogicComponent.ComponentType.AND ||
                    gate.type == LogicComponent.ComponentType.OR ||
                    gate.type == LogicComponent.ComponentType.NOT ||
                    gate.type == LogicComponent.ComponentType.LED ||
                    gate.type == LogicComponent.ComponentType.SWITCH)
            {
                Image img = null;
                if (gate.type == LogicComponent.ComponentType.AND)
                    img = LogicComponent.AndImage;
                else if (gate.type == LogicComponent.ComponentType.OR)
                    img = LogicComponent.OrImage;
                else if (gate.type == LogicComponent.ComponentType.NOT)
                    img = LogicComponent.NotImage;
                else if (gate.type == LogicComponent.ComponentType.LED)
                    img = LogicComponent.LedImage;
                else if (gate.type == LogicComponent.ComponentType.SWITCH)
                    img = LogicComponent.SwitchImage;

                int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
                int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);

                /*
                                        sX,
                        sY,
                        (int)(img.getWidth(null) * zoomLevel),
                        (int)(img.getHeight(null) * zoomLevel),
                 */

                if (mX >= sX && mY >= sY && mX <= sX + (int)(img.getWidth(null) * zoomLevel) &&
                mY <= sY + (int)(img.getHeight(null) * zoomLevel))
                    return gate;
            }
        }




        return null;
    }

    public int GetComponentInputIndexAt(LogicComponent gate, int mX, int mY)
    {
        if (gate.type == LogicComponent.ComponentType.AND ||
                gate.type == LogicComponent.ComponentType.OR ||
                gate.type == LogicComponent.ComponentType.NOT ||
                gate.type == LogicComponent.ComponentType.LED ||
                gate.type == LogicComponent.ComponentType.SWITCH)
        {
            Image img = null;
            if (gate.type == LogicComponent.ComponentType.AND)
                img = LogicComponent.AndImage;
            else if (gate.type == LogicComponent.ComponentType.OR)
                img = LogicComponent.OrImage;
            else if (gate.type == LogicComponent.ComponentType.NOT)
                img = LogicComponent.NotImage;
            else if (gate.type == LogicComponent.ComponentType.LED)
                img = LogicComponent.LedImage;
            else if (gate.type == LogicComponent.ComponentType.SWITCH)
                img = LogicComponent.SwitchImage;

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);


            float h = img.getHeight(null) * zoomLevel;
            float w = img.getWidth(null) * zoomLevel;
            float d = h / (gate.inputCount + 1);
            float y = 0;

            for (int i = 0; i < gate.inputCount; i++)
            {
                y += d;

                if (    mX >= sX - (int)(2 * zoomLevel) &&
                        mX <= (sX - (int)(2 * zoomLevel)) + (int)(5 * zoomLevel) &&

                        mY >= sY - (int)(2.5 * zoomLevel) + (int)y &&
                        mY <= (sY - (int)(2.5 * zoomLevel) + (int)y) + (int)(5 * zoomLevel)
                )
                    return i;
            }
        }



        return -1;
    }


    public int GetComponentOutputIndexAt(LogicComponent gate, int mX, int mY)
    {
        if (gate.type == LogicComponent.ComponentType.AND ||
                gate.type == LogicComponent.ComponentType.OR ||
                gate.type == LogicComponent.ComponentType.NOT ||
                gate.type == LogicComponent.ComponentType.LED ||
                gate.type == LogicComponent.ComponentType.SWITCH)
        {
            Image img = null;
            if (gate.type == LogicComponent.ComponentType.AND)
                img = LogicComponent.AndImage;
            else if (gate.type == LogicComponent.ComponentType.OR)
                img = LogicComponent.OrImage;
            else if (gate.type == LogicComponent.ComponentType.NOT)
                img = LogicComponent.NotImage;
            else if (gate.type == LogicComponent.ComponentType.LED)
                img = LogicComponent.LedImage;
            else if (gate.type == LogicComponent.ComponentType.SWITCH)
                img = LogicComponent.SwitchImage;

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);


            float h = img.getHeight(null) * zoomLevel;
            float w = img.getWidth(null) * zoomLevel;
            float d = h / (gate.outputCount + 1);
            float y = 0;

            for (int i = 0; i < gate.outputCount; i++)
            {
                y += d;

                // sX + (int)w - (int)(3 * zoomLevel), sY - (int)(2.5 * zoomLevel) + (int)y
                if (    mX >= sX + (int)w - (int)(3 * zoomLevel) &&
                        mX <= (sX + (int)w - (int)(3 * zoomLevel)) + (int)(5 * zoomLevel) &&

                        mY >= sY - (int)(2.5 * zoomLevel) + (int)y &&
                        mY <= (sY - (int)(2.5 * zoomLevel) + (int)y) + (int)(5 * zoomLevel)
                )
                    return i;
            }
        }



        return -1;
    }



    public void drawGate(Graphics2D g2, LogicComponent gate)
    {
        if (gate.type == LogicComponent.ComponentType.AND ||
                gate.type == LogicComponent.ComponentType.OR ||
                gate.type == LogicComponent.ComponentType.NOT ||
                gate.type == LogicComponent.ComponentType.LED ||
                gate.type == LogicComponent.ComponentType.SWITCH)
        {
            Image img = null;
            if (gate.type == LogicComponent.ComponentType.AND)
                img = LogicComponent.AndImage;
            else if (gate.type == LogicComponent.ComponentType.OR)
                img = LogicComponent.OrImage;
            else if (gate.type == LogicComponent.ComponentType.NOT)
                img = LogicComponent.NotImage;
            else if (gate.type == LogicComponent.ComponentType.LED)
                img = LogicComponent.LedImage;
            else if (gate.type == LogicComponent.ComponentType.SWITCH)
                img = LogicComponent.SwitchImage;

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);

            gate.UpdateGate();

            {
                if (!gate.isBasic)
                    g2.setColor(Color.LIGHT_GRAY);
                else
                {
                    if (gate.basicState)
                        g2.setColor(Color.GREEN);
                    else
                        g2.setColor(Color.RED);
                }

                g2.fillRect(
                        sX,
                        sY,
                        (int)(img.getWidth(null) * zoomLevel),
                        (int)(img.getHeight(null) * zoomLevel)
                );
            }

            g2.drawImage(img,
                    sX,
                    sY,
                    (int)(img.getWidth(null) * zoomLevel),
                    (int)(img.getHeight(null) * zoomLevel),
                    null);

            {
                float h = img.getHeight(null) * zoomLevel;
                float w = img.getWidth(null) * zoomLevel;
                float d = h / (gate.inputCount + 1);
                float y = 0;

                for (int i = 0; i < gate.inputCount; i++)
                {
                    y += d;
                    if (gate.inputs.get(i))
                        g2.setColor(Color.GREEN);
                    else
                        g2.setColor(Color.RED);
                    g2.fillRect(sX - (int)(2 * zoomLevel), sY - (int)(2.5 * zoomLevel) + (int)y, (int)(5 * zoomLevel), (int)(5 * zoomLevel));
                }

                d = h / (gate.outputCount + 1);
                y = 0;
                for (int i = 0; i < gate.outputCount; i++)
                {
                    y += d;
                    if (gate.outputs.get(i))
                        g2.setColor(Color.GREEN);
                    else
                        g2.setColor(Color.RED);
                    g2.fillRect(sX + (int)w - (int)(3 * zoomLevel), sY - (int)(2.5 * zoomLevel) + (int)y, (int)(5 * zoomLevel), (int)(5 * zoomLevel));
                }
            }

        }
    }
}
