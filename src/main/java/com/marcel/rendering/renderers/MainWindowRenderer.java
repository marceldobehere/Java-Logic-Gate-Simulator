package com.marcel.rendering.renderers;

import com.marcel.rendering.CanvasThing;
import com.marcel.rendering.MainRenderController;
import com.marcel.rendering.utils.DPos;
import com.marcel.rendering.utils.WindowRect;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindowRenderer {
    public WindowRect mainRect = new WindowRect();
    public Color mainWindowCol = new Color(206,206,206);
    public Color mainWindowSelectCol = new Color(10,40,200, 80);
    public float zoomLevel = 1;
    public float scrollX = 0;
    public float scrollY = 0;

    //public float oldZoom = 1;

    public List<LogicComponent> logicGates = new ArrayList<>();
    public List<ComponentConnection> connections = new ArrayList<>();

    public void init()
    {
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.SWITCH, new DPos(50, 50), this));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.SWITCH, new DPos(50, 150), this));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.AND, new DPos(180, 50), this));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.OR, new DPos(180, 150), this));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.LED, new DPos(310, 50), this));
        logicGates.add(new LogicComponent(LogicComponent.ComponentType.LED, new DPos(310, 150), this));
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(mainWindowCol);
        CanvasThing.fillRectFromPoints(g2, mainRect.x1, mainRect.y1, mainRect.x2, mainRect.y2);
        drawGates(g2);
        drawWires(g2);

        if (MainRenderController.finishedComponentListBoundary  || MainRenderController.dragComponentList)
        {

            DPos startPos = MainRenderController.ConvertLogicPosToWindowPos(MainRenderController.dragComponentListOrigin.x , MainRenderController.dragComponentListOrigin.y);
            DPos endPos = MainRenderController.ConvertLogicPosToWindowPos(MainRenderController.dragComponentListEnd.x , MainRenderController.dragComponentListEnd.y);
            //System.out.println("DRAWING RECT " + startPos.x + " " + startPos.y + " " + endPos.x + " " + endPos.y);

            if (startPos.x > endPos.x)
            {
                double temp = startPos.x;
                startPos.x = endPos.x;
                endPos.x = temp;
            }
            if (startPos.y > endPos.y)
            {
                double temp = startPos.y;
                startPos.y = endPos.y;
                endPos.y = temp;
            }

            g2.setColor(mainWindowSelectCol);
            g2.fillRect((int) startPos.x, (int) startPos.y, (int) (endPos.x - startPos.x), (int) (endPos.y - startPos.y));
        }
    }

    public void drawWires(Graphics2D g2)
    {
        for (ComponentConnection conn : connections)
        {
            drawWire(g2, conn);
        }
    }

    public void drawWire(Graphics2D g2, ComponentConnection conn)
    {
        if (conn.fromComponent == null || conn.toComponent == null)
            return;

        if (!conn.fromComponent.drawThing || !conn.toComponent.drawThing)
            return;

        DPos startPos = GetOutputPos(conn.fromComponent, conn.fromComponentIndex);
        DPos endPos = GetInputPos(conn.toComponent, conn.toComponentIndex);

        g2.setStroke(new BasicStroke(3 * zoomLevel));
        if (conn.fromComponent.outputs.get(conn.fromComponentIndex))
            g2.setColor(Color.GREEN);
        else
            g2.setColor(Color.RED);
        g2.drawLine(
                (int)(startPos.x),
                (int)(startPos.y),
                (int)(endPos.x),
                (int)(endPos.y)
        );
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
        else if (type == LogicComponent.ComponentType.NAND)
            return LogicComponent.NandImage;
        else if (type == LogicComponent.ComponentType.NOR)
            return LogicComponent.NorImage;
        else if (type == LogicComponent.ComponentType.BUFFER)
            return LogicComponent.BufferImage;
        else if (type == LogicComponent.ComponentType.XOR)
            return LogicComponent.XorImage;
        else if (type == LogicComponent.ComponentType.XNOR)
            return LogicComponent.XnorImage;
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

                type == LogicComponent.ComponentType.NAND ||
                type == LogicComponent.ComponentType.NOR ||
                type == LogicComponent.ComponentType.BUFFER ||

                type == LogicComponent.ComponentType.XOR ||
                type == LogicComponent.ComponentType.XNOR ||

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

    public DPos GetComponentSize(LogicComponent gate, boolean useZoom)
    {
        if (gate == null)
            return null;
        DPos size = new DPos(0, 0);

        Image img = GetImageFromType(gate.type);

        double zoom = 1;
        if (useZoom)
            zoom = zoomLevel;

        size.x = img.getWidth(null) * zoom;
        size.y = img.getHeight(null) * zoom;

        return size;
    }

    public void deleteComponent(LogicComponent gate)
    {
        //System.out.println("deleting component...");

        for (int i = 0; i < connections.size(); i++)
        {
            ComponentConnection conn = connections.get(i);
            if (conn.fromComponent == gate || conn.toComponent == gate)
            {
                conn.toComponent.inputs.set(conn.toComponentIndex, false);
                connections.remove(conn);
                i--;
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
            //float w = img.getWidth(null) * zoomLevel;
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
            //float w = img.getWidth(null) * zoomLevel;
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
