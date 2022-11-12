package com.marcel.Rendering.renderers;

import com.marcel.Rendering.CanvasThing;
import com.marcel.Rendering.utils.DPos;
import com.marcel.Rendering.utils.WindowRect;


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
        //logicGates.add(new LogicComponent(LogicComponent.ComponentType.AND, new DPos(80, 50)));
        //logicGates.add(new LogicComponent(LogicComponent.ComponentType.OR, new DPos(200, 50)));

        logicGates.add(new LogicComponent(LogicComponent.ComponentType.SWITCH, new DPos(50, 50)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.SWITCH, new DPos(50, 150)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.AND, new DPos(180, 50)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.OR, new DPos(180, 150)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.LED, new DPos(310, 50)));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.LED, new DPos(310, 150)));
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(mainWindowCol);
        CanvasThing.fillRectFromPoints(g2, mainRect.x1, mainRect.y1, mainRect.x2, mainRect.y2);
        drawGates(g2);
        drawWires(g2);
    }

    public void drawWires(Graphics2D g2)
    {
        for (LogicComponent gate : logicGates)
        {
            drawWire(g2, gate);
        }
    }

    public void drawWire(Graphics2D g2, LogicComponent gate)
    {
        if (!gate.drawThing)
            return;

        for (int i1 = 0; i1 < gate.outputCount; i1++)
        {
            DPos startPos = GetOutputPos(gate, i1);
            List<LogicComponent> components = gate.outputGates.get(i1);
            for (int i2 = 0; i2 < components.size(); i2++)
            {
                LogicComponent component = components.get(i2);
                for (int i3 = 0; i3 < component.inputCount; i3++)
                {
                    if (component.inputGates.get(i3) != gate)
                        continue;

                    g2.setStroke(new BasicStroke(3 * zoomLevel));
                    if (gate.outputs.get(i1))
                        g2.setColor(Color.GREEN);
                    else
                        g2.setColor(Color.RED);
                    DPos endPos = GetInputPos(component, i3);
                    g2.drawLine(
                            (int)(startPos.x),
                            (int)(startPos.y),
                            (int)(endPos.x),
                            (int)(endPos.y)
                    );
                }
            }
        }





    }

    public void drawGates(Graphics2D g2)
    {
        for (LogicComponent gate : logicGates)
        {
            drawGate(g2, gate);
        }
    }

    public Image GetImageFromType(LogicComponent.ComponentType type)
    {
        if (type == LogicComponent.ComponentType.AND)
            return LogicComponent.AndImage;
        else if (type == LogicComponent.ComponentType.OR)
            return LogicComponent.OrImage;
        else if (type == LogicComponent.ComponentType.NOT)
            return LogicComponent.NotImage;
        else if (type == LogicComponent.ComponentType.LED)
            return LogicComponent.LedImage;
        else if (type == LogicComponent.ComponentType.SWITCH)
            return LogicComponent.SwitchImage;
        else if (type == LogicComponent.ComponentType.BUTTON)
            return LogicComponent.ButtonImage;
        return null;
    }

    public boolean IsBasicType(LogicComponent.ComponentType type)
    {
        return (type == LogicComponent.ComponentType.AND ||
                type == LogicComponent.ComponentType.OR ||
                type == LogicComponent.ComponentType.NOT ||
                type == LogicComponent.ComponentType.LED ||
                type == LogicComponent.ComponentType.SWITCH ||
                type == LogicComponent.ComponentType.BUTTON);
    }

    public LogicComponent GetComponentAt(int mX, int mY)
    {
        for (int t = logicGates.size() - 1; t >= 0; t--)
        {
            LogicComponent gate = logicGates.get(t);
            if (gate == null)
                continue;


            if (IsBasicType(gate.type))
            {
                Image img = GetImageFromType(gate.type);

                int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
                int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);

                if (mX >= sX - (2 * zoomLevel) && mY >= sY - (2 * zoomLevel)&&
                        mX <= sX + (int)(img.getWidth(null) * zoomLevel) + (2 * zoomLevel)&&
                mY <= sY + (int)(img.getHeight(null) * zoomLevel) + (2 * zoomLevel))
                    return gate;
            }
        }




        return null;
    }

    public void deleteComponent(LogicComponent gate)
    {
        System.out.println("deleting component...");
        for (int i1 = 0; i1 < gate.inputGates.size(); i1++)
        {
            LogicComponent temp = gate.inputGates.get(i1);
            if (temp == null)
                continue;
            for (int i2 = 0; i2 < temp.outputGates.size(); i2++)
            {
                while (temp.outputGates.get(i2).remove(gate))
                {
                    //System.out.println("del 1 1");
                }
            }
            temp.UpdateGate();
            //System.out.println("del 1");
        }

        for (int i1 = 0; i1 < gate.outputGates.size(); i1++)
        {
            List<LogicComponent> tempList = gate.outputGates.get(i1);
            for (int i2 = 0; i2 < tempList.size(); i2++)
            {
                LogicComponent temp = tempList.get(i2);
                while(temp.inputGates.contains(gate))
                {
                    int tIndex = temp.inputGates.indexOf(gate);
                    temp.inputGates.set(tIndex, null);
                    temp.inputs.set(tIndex, false);
                    //System.out.println("del 2 2");
                }
                temp.UpdateGate();
                //System.out.println("del 2");
            }
        }


        logicGates.remove(gate);

    }

    public int GetComponentInputIndexAt(LogicComponent gate, int mX, int mY)
    {
        if (IsBasicType(gate.type))
        {
            Image img = GetImageFromType(gate.type);

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);


            float h = img.getHeight(null) * zoomLevel;
            float w = img.getWidth(null) * zoomLevel;
            float d = h / (gate.inputCount + 1);
            float y = 0;

            for (int i = 0; i < gate.inputCount; i++)
            {
                y += d;

                if (    mX >= sX - (int)(5 * zoomLevel) &&
                        mX <= (sX + (int)(5 * zoomLevel)) &&

                        mY >= sY - (int)(5 * zoomLevel) + (int)y &&
                        mY <= (sY + (int)(5 * zoomLevel) + (int)y)
                )
                    return i;
            }
        }



        return -1;
    }


    public int GetComponentOutputIndexAt(LogicComponent gate, int mX, int mY)
    {
        if (IsBasicType(gate.type))
        {
            Image img = GetImageFromType(gate.type);

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
                if (    mX >= sX + (int)w - (int)(5 * zoomLevel) &&
                        mX <= (sX + (int)w + (int)(5 * zoomLevel))&&

                        mY >= sY - (int)(5 * zoomLevel) + (int)y &&
                        mY <= (sY + (int)(5 * zoomLevel) + (int)y)
                )
                    return i;
            }
        }



        return -1;
    }

    public DPos GetInputPos(LogicComponent gate, int index)
    {
        DPos pos = new DPos();
        if (IsBasicType(gate.type))
        {
            Image img = GetImageFromType(gate.type);

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);


            float h = img.getHeight(null) * zoomLevel;
            float w = img.getWidth(null) * zoomLevel;
            float d = h / (gate.inputCount + 1);
            float y = 0;

            for (int i = 0; i < gate.inputCount; i++)
            {
                y += d;

                if (i == index)
                {
                    pos.x = sX;
                    pos.y = sY + y;
                    return pos;
                }
            }
        }



        return pos;
    }

    public DPos GetOutputPos(LogicComponent gate, int index)
    {
        DPos pos = new DPos();
        if (IsBasicType(gate.type))
        {
            Image img = GetImageFromType(gate.type);

            int sX = (int)((gate.pos.x - scrollX) * zoomLevel + mainRect.x1);
            int sY = (int)((gate.pos.y - scrollY) * zoomLevel + mainRect.y1);


            float h = img.getHeight(null) * zoomLevel;
            float w = img.getWidth(null) * zoomLevel;
            float d = h / (gate.outputCount + 1);
            float y = 0;

            for (int i = 0; i < gate.outputCount; i++)
            {
                y += d;

                if (i == index)
                {
                    pos.x = sX + w;
                    pos.y = sY + y;
                    return pos;
                }
            }
        }



        return pos;
    }


    public void drawGate(Graphics2D g2, LogicComponent gate)
    {
        if (!gate.drawThing)
            return;

        if (IsBasicType(gate.type))
        {
            Image img = GetImageFromType(gate.type);

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
