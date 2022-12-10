package com.marcel.rendering;

import com.marcel.ParsingAndStuff.LoadLogic;
import com.marcel.ParsingAndStuff.SaveLogic;
import com.marcel.rendering.renderers.ComponentConnection;
import com.marcel.rendering.renderers.LogicComponent;
import com.marcel.rendering.renderers.TopMenuRenderer;
import com.marcel.rendering.utils.DPos;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class MainRenderController
{
    public static CanvasThing canvas;
    public static JFrame jFrame;
    public static boolean allowDrawing;

    private static void HandleKeyEvent(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            canvas.sidebarRenderer.shrinkSidebar = !canvas.sidebarRenderer.shrinkSidebar;
        }
        else if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_3 ||
                 e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_6)
        {
            DPos mPos = ConvertWindowPosToLogicPos(canvas.mouseX, canvas.mouseY);

            LogicComponent.ComponentType type = LogicComponent.ComponentType.AND;
            if (e.isShiftDown())
            {
                if (e.getKeyCode() == KeyEvent.VK_1)
                    type = LogicComponent.ComponentType.NAND;
                if (e.getKeyCode() == KeyEvent.VK_2)
                    type = LogicComponent.ComponentType.NOR;
                if (e.getKeyCode() == KeyEvent.VK_3)
                    type = LogicComponent.ComponentType.BUFFER;
                if (e.getKeyCode() == KeyEvent.VK_4)
                    type = LogicComponent.ComponentType.XNOR;
                if (e.getKeyCode() == KeyEvent.VK_5)
                    type = LogicComponent.ComponentType.BUTTON;
            }
            else
            {
                if (e.getKeyCode() == KeyEvent.VK_1)
                    type = LogicComponent.ComponentType.AND;
                if (e.getKeyCode() == KeyEvent.VK_2)
                    type = LogicComponent.ComponentType.OR;
                if (e.getKeyCode() == KeyEvent.VK_3)
                    type = LogicComponent.ComponentType.NOT;
                if (e.getKeyCode() == KeyEvent.VK_4)
                    type = LogicComponent.ComponentType.XOR;
                if (e.getKeyCode() == KeyEvent.VK_5)
                    type = LogicComponent.ComponentType.SWITCH;
            }
            if (e.getKeyCode() == KeyEvent.VK_6)
                type = LogicComponent.ComponentType.LED;

            canvas.mainWindowRenderer.logicGates.add(new LogicComponent(type, mPos, canvas.mainWindowRenderer));

        }
    }

    private static void HandleMouseWheel(MouseWheelEvent e)
    {

        int amount = e.getScrollAmount() * e.getWheelRotation();

        if (e.isControlDown())
        {
            float oldZoom = canvas.mainWindowRenderer.zoomLevel;
            canvas.mainWindowRenderer.zoomLevel -= amount / 50.0;
            if (canvas.mainWindowRenderer.zoomLevel < 0.5)
                canvas.mainWindowRenderer.zoomLevel = 0.5f;
            if (canvas.mainWindowRenderer.zoomLevel > 5)
                canvas.mainWindowRenderer.zoomLevel = 5f;
            float newZoom = canvas.mainWindowRenderer.zoomLevel;

            float scrollX = canvas.mainWindowRenderer.scrollX;
            float scrollY = canvas.mainWindowRenderer.scrollY;

            float mPosX0 = canvas.mouseX - canvas.mainWindowRenderer.mainRect.x1;
            float mPosY0 = canvas.mouseY - canvas.mainWindowRenderer.mainRect.y1;

            float mPosX2 = (mPosX0 / oldZoom) + scrollX;
            float mPosY2 = (mPosY0 / oldZoom) + scrollY;

            float mPosX3 = (mPosX0 / newZoom) + scrollX;
            float mPosY3 = (mPosY0 / newZoom) + scrollY;

            float mPosDX = mPosX3 - mPosX2;
            float mPosDY = mPosY3 - mPosY2;

            canvas.mainWindowRenderer.scrollX -= mPosDX;
            canvas.mainWindowRenderer.scrollY -= mPosDY;
        }
        else
        {
            float nAmount = amount * (10/canvas.mainWindowRenderer.zoomLevel);
            if (e.isShiftDown())
                canvas.mainWindowRenderer.scrollX += nAmount;
            else
                canvas.mainWindowRenderer.scrollY += nAmount;
        }
    }


    public static List<LogicComponent> selectedComponents = new ArrayList<LogicComponent>();
    public static List<DPos> selectedComponentOffsets = new ArrayList<DPos>();
    public static boolean dragComponentList = false;
    public static boolean finishedComponentListBoundary = false;
    public static DPos dragComponentListOrigin = new DPos(0, 0);
    public static DPos dragComponentListEnd = new DPos(0, 0);
    public static DPos dragComponentListOriginOffset = new DPos(0, 0);
    public static DPos dragComponentListEndOffset = new DPos(0, 0);


    public static LogicComponent selectedComponent = null;
    public static DPos selectedComponentOffset = new DPos();
    public static boolean dragComponent = false;
    public static boolean dragCable = false;
    public static int dragCableFromOutputIndex = -1;
    public static DPos cableFrom = new DPos();

    public static void HandleMouse(MouseEvent e)
    {

    }

    public static DPos ConvertWindowPosToLogicPos(double x, double y)
    {
        float newZoom = canvas.mainWindowRenderer.zoomLevel;

        float scrollX = canvas.mainWindowRenderer.scrollX;
        float scrollY = canvas.mainWindowRenderer.scrollY;

        double mPosX0 = x - canvas.mainWindowRenderer.mainRect.x1;
        double mPosY0 = y - canvas.mainWindowRenderer.mainRect.y1;

        double mPosX3 = (mPosX0 / newZoom) + scrollX;
        double mPosY3 = (mPosY0 / newZoom) + scrollY;

        return new DPos(mPosX3, mPosY3);
    }

    public static DPos ConvertLogicPosToWindowPos(double x, double y)
    {
        float newZoom = canvas.mainWindowRenderer.zoomLevel;

        float scrollX = canvas.mainWindowRenderer.scrollX;
        float scrollY = canvas.mainWindowRenderer.scrollY;

        double mPosX3 = (x - scrollX) * newZoom + canvas.mainWindowRenderer.mainRect.x1;
        double mPosY3 = (y - scrollY) * newZoom + canvas.mainWindowRenderer.mainRect.y1;

        return new DPos(mPosX3, mPosY3);
    }

    public static void HandleMouseDrag(MouseEvent e)
    {

    }

    public static void updateSelectedComponentPosition()
    {
        if (selectedComponent == null)
        {
            if (dragComponentList)
            {
                DPos mPos = ConvertWindowPosToLogicPos(canvas.mouseX, canvas.mouseY);
                if (!finishedComponentListBoundary)
                {
                    dragComponentListEnd = mPos;
                    return;
                }
                else
                {
                    dragComponentListOrigin.x = mPos.x + dragComponentListOriginOffset.x;
                    dragComponentListOrigin.y = mPos.y + dragComponentListOriginOffset.y;

                    dragComponentListEnd.x = mPos.x + dragComponentListEndOffset.x;
                    dragComponentListEnd.y = mPos.y + dragComponentListEndOffset.y;
                }
            }


            return;
        }
        if (!dragComponent)
            return;

        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int nMouseX = (int) b.getX() - canvas.getLocationOnScreen().x;
        int nMouseY = (int) b.getY() - canvas.getLocationOnScreen().y;
        canvas.mouseX = nMouseX;
        canvas.mouseY = nMouseY;


        DPos mPos = ConvertWindowPosToLogicPos(canvas.mouseX, canvas.mouseY);

        selectedComponent.pos.x = selectedComponentOffset.x + mPos.x;
        selectedComponent.pos.y = selectedComponentOffset.y + mPos.y;

    }

    public static void HandleMousePreClick(MouseEvent e)
    {
        if (canvas.topMenuRenderer.selectedText != TopMenuRenderer.SelectedTextEnum.NONE)
        {
            if (canvas.topMenuRenderer.selectedText == TopMenuRenderer.SelectedTextEnum.SAVE)
            {
                SaveLogic.SaveLogicComponentsToFile(canvas.mainWindowRenderer.logicGates, canvas.mainWindowRenderer.connections, "test.txt");
                System.out.println("SAVING....");
            }
            else if (canvas.topMenuRenderer.selectedText == TopMenuRenderer.SelectedTextEnum.LOAD)
            {
                LoadLogic.LoadSave("test.txt");
                System.out.println("LOADING....");
            }

            return;
        }




        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int nMouseX = (int) b.getX() - canvas.getLocationOnScreen().x;
        int nMouseY = (int) b.getY() - canvas.getLocationOnScreen().y;

        DPos mPos = ConvertWindowPosToLogicPos(nMouseX, nMouseY);


        selectedComponent = canvas.mainWindowRenderer.GetComponentAt(nMouseX, nMouseY);
        if (selectedComponent != null)
        {
            selectedComponentOffset.x = selectedComponent.pos.x - mPos.x;
            selectedComponentOffset.y = selectedComponent.pos.y - mPos.y;

            canvas.mainWindowRenderer.logicGates.remove(selectedComponent);
            canvas.mainWindowRenderer.logicGates.add(selectedComponent);
        }
        else
        {
            selectedComponentOffset.x = 0;
            selectedComponentOffset.y = 0;
        }


        if (e.getButton() == MouseEvent.BUTTON2)
        {
            for (int i = 0; i < canvas.mainWindowRenderer.connections.size(); i++)
            {
                ComponentConnection x = canvas.mainWindowRenderer.connections.get(i);

                if (x.fromComponent == null || x.toComponent == null)
                    continue;
                if (!x.fromComponent.drawThing || !x.toComponent.drawThing)
                    continue;

                DPos startPos = canvas.mainWindowRenderer.GetOutputPos(x.fromComponent, x.fromComponentIndex);
                DPos endPos = canvas.mainWindowRenderer.GetInputPos(x.toComponent, x.toComponentIndex);

                double distance = Line2D.ptSegDist(
                        startPos.x, startPos.y,
                        endPos.x, endPos.y,
                        canvas.mouseX, canvas.mouseY);

                //System.out.println("X1: " + startPos.x + " Y1: " + startPos.y + " X2: " + endPos.x + " Y2: " + endPos.y + " MX: " + mPosX3 + " MY: " + mPosY3 + " DISTANCE: " + distance);

                if (distance <= 3 * canvas.mainWindowRenderer.zoomLevel)
                {
                    x.toComponent.inputs.set(x.toComponentIndex, false);
                    canvas.mainWindowRenderer.connections.remove(x);
                    //System.out.println("DELETING CONNECTION!");
                    return;
                }
            }
        }

        dragComponent = false;
        if (selectedComponent != null)
        {
            int sIndex = canvas.mainWindowRenderer.GetComponentOutputIndexAt(selectedComponent, nMouseX, nMouseY);
            if (sIndex == -1)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    dragComponent = true;
                }
                else if (e.getButton() == MouseEvent.BUTTON2)
                {
                    canvas.mainWindowRenderer.deleteComponent(selectedComponent);
                }
                else
                {
                    if (selectedComponent.type == LogicComponent.ComponentType.SWITCH)
                    {
                        selectedComponent.basicState = !selectedComponent.basicState;
                    }
                    else if (selectedComponent.type == LogicComponent.ComponentType.BUTTON)
                    {
                        selectedComponent.basicState = true;
                    }
                }
            }
            else
            {
                dragComponent = false;
                dragCable = true;
                dragCableFromOutputIndex = sIndex;
                DPos startingFrom = canvas.mainWindowRenderer.GetOutputPos(selectedComponent, sIndex);

                cableFrom = ConvertWindowPosToLogicPos(startingFrom.x, startingFrom.y);
                //System.out.println("Drag cable moment!");
                return;
            }
            return;
        }
        else
        {
           if (!dragComponentList && e.getButton() == MouseEvent.BUTTON1)
           {
               //DPos mPos = ConvertWindowPosToLogicPos(canvas.mouseX, canvas.mouseY);
               if (finishedComponentListBoundary)
               {
                    if (mPos.x >= dragComponentListOrigin.x && mPos.x <= dragComponentListEnd.x &&
                            mPos.y >= dragComponentListOrigin.y && mPos.y <= dragComponentListEnd.y)
                    {
                        System.out.println("Dragging Selection...");
                        dragComponentList = true;
                        dragComponentListOriginOffset = new DPos(dragComponentListOrigin.x - mPos.x, dragComponentListOrigin.y - mPos.y);
                        dragComponentListEndOffset = new DPos(dragComponentListEnd.x - mPos.x, dragComponentListEnd.y - mPos.y);
                        return;
                    }
               }
               else
               {
                   dragComponentList = true;
                   dragComponentListOrigin = mPos;
                   System.out.println("DRAG LIST ORIGIN: " + dragComponentListOrigin.x + " " + dragComponentListOrigin.y);
               }
           }
        }



        //System.out.println("COMPONENT: " + selectedComponent);


    }


    public static boolean handleMouse = false;

    public static void main()
    {
        jFrame = new JFrame("Logic Gate Simulator");
        Container pane = jFrame.getContentPane();
        allowDrawing = false;

        canvas = new CanvasThing();
        pane.add(canvas);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Exiting...");
            }
        });

        jFrame.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                HandleKeyEvent(e);
            }
        });

        jFrame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                HandleMouseWheel(e);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                HandleMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        jFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HandleMouse(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                HandleMousePreClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (dragComponentList)
                {
                    if (!finishedComponentListBoundary)
                    {
                        boolean gotComponents = false;

                        gotComponents = true;

                        System.out.println("ADD COMPONENTS TO THE LIST WITH THE OFFSETS HERE!");



                        if (gotComponents)
                        {
                            finishedComponentListBoundary = true;
                        }
                        else
                        {
                            finishedComponentListBoundary = false;
                        }
                    }
                    else
                    {

                    }
                    dragComponentList = false;
                    return;
                }


                LogicComponent last = canvas.mainWindowRenderer.GetComponentAt(canvas.mouseX, canvas.mouseY);
                if (last != null)
                {
                    if (last.type == LogicComponent.ComponentType.BUTTON)
                    {
                        last.basicState = false;
                    }

                    if (dragCable &&  dragCableFromOutputIndex != -1)
                    {
                        int sIndex = canvas.mainWindowRenderer.GetComponentInputIndexAt(last, canvas.mouseX, canvas.mouseY);

                        if (sIndex != -1)
                        {
                            //System.out.println("CONNECTION!");
                            //selectedComponent.outputGates.get(dragCableFromOutputIndex).add(last);
                            //last.inputGates.set(sIndex, selectedComponent);
                            selectedComponent.cont.connections.add(new ComponentConnection(selectedComponent, dragCableFromOutputIndex, last, sIndex));
                        }
                    }
                }
                dragComponent = false;
                dragCable = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                handleMouse = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                handleMouse = false;
            }
        });

        jFrame.setSize(1280, 720);
        jFrame.setVisible(true);
        jFrame.setResizable(true);
        jFrame.setMinimumSize(new Dimension(400,400));

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
