package com.marcel.rendering.renderers;

import com.marcel.rendering.utils.DPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogicComponent
{
    public enum ComponentType
    {
        AND,
        OR,
        NOT,

        SWITCH,
        LED,
        BUTTON,


        CUSTOM
    }

    public static ComponentType GetCompTypeFromString(String data)
    {
        if (data.equals("AND"))
            return ComponentType.AND;
        if (data.equals("OR"))
            return ComponentType.OR;
        if (data.equals("NOT"))
            return ComponentType.NOT;

        if (data.equals("SWITCH"))
            return ComponentType.SWITCH;
        if (data.equals("LED"))
            return ComponentType.LED;
        if (data.equals("BUTTON"))
            return ComponentType.BUTTON;

        if (data.equals("CUSTOM"))
            return ComponentType.CUSTOM;

        return ComponentType.CUSTOM;
    }

    public static Image AndImage = null;
    public static Image OrImage = null;
    public static Image NotImage = null;
    public static Image SwitchImage = null;
    public static Image ButtonImage = null;
    public static Image LedImage = null;

    public DPos pos;

    public int inputCount;
    public int outputCount;
    public int height;
    public boolean basicState;
    public boolean isBasic;
    public boolean drawThing = false;
    public ComponentType type;

    public List<Boolean> inputs = new ArrayList<>();
    public List<Boolean> outputs = new ArrayList<>();

    public MainWindowRenderer cont;



    public LogicComponent(MainWindowRenderer cont)
    {
this.cont = cont;
    }

    public LogicComponent(ComponentType type, DPos pos, MainWindowRenderer cont)
    {
        this.cont = cont;
        this.type = type;
        this.pos = pos;
        height = AndImage.getHeight(null);
        isBasic = false;
        basicState = false;
        if (type == ComponentType.AND || type == ComponentType.OR)
        {
            inputCount = 2;
            outputCount = 1;
            isBasic = true;
            drawThing = true;
        }
        else if (type == ComponentType.NOT)
        {
            inputCount = 1;
            outputCount = 1;
            isBasic = true;
            drawThing = true;
        }
        else if (type == ComponentType.LED)
        {
            inputCount = 1;
            outputCount = 0;
            isBasic = true;
            drawThing = true;
        }
        else if (type == ComponentType.SWITCH)
        {
            inputCount = 0;
            outputCount = 1;
            isBasic = true;
            drawThing = true;
        }
        else if (type == ComponentType.BUTTON)
        {
            inputCount = 0;
            outputCount = 1;
            isBasic = true;
            drawThing = true;
        }
        else if (type == ComponentType.CUSTOM)
        {
            height = 100;
            inputCount = 5;
            outputCount = 3;
            drawThing = true;
        }

        ResetInputs();
        ResetOutputs();
        //UpdateGate();
    }

    public void ResetInputs()
    {
        inputs.clear();
        for (int i = 0; i < inputCount; i++)
            inputs.add(false);
    }

    public void ResetOutputs()
    {
        outputs.clear();
        for (int i = 0; i < outputCount; i++)
            outputs.add(false);
    }

    public void CalcState()
    {
        if (type == ComponentType.AND)
            basicState = inputs.get(0) && inputs.get(1);
        else if (type == ComponentType.OR)
            basicState = inputs.get(0) || inputs.get(1);
        else if (type == ComponentType.NOT)
            basicState = !inputs.get(0);
        else if (type == ComponentType.LED)
            basicState = inputs.get(0);
        else if (type == ComponentType.SWITCH)
            ;
        else if (type == ComponentType.BUTTON)
            ;
        else
            basicState = false;
    }

    public void UpdateOutputState()
    {
        CalcState();
        if (isBasic && outputCount == 1)
        {
            outputs.set(0, basicState);
        }
        else
        {

        }

        for (int i = 0; i < outputCount; i++)
            UpdateOutput(i, outputs.get(i));
    }

    public void UpdateOutput(int index, boolean state)
    {
        outputs.set(index, state);
        for (int i = 0; i < cont.connections.size(); i++)
        {
            ComponentConnection conn = cont.connections.get(i);
            if (conn.fromComponent == this && conn.fromComponentIndex == index)
                conn.toComponent.inputs.set(conn.toComponentIndex, state);
        }
    }

    public void UpdateInput(ComponentConnection conn, boolean state)
    {
        inputs.set(conn.toComponentIndex, state);
    }

    public void UpdateGate()
    {
        UpdateOutputState();
    }

}
