package com.marcel.Rendering.Renderers;

import com.marcel.Rendering.Utils.DPos;

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

    public static Image AndImage = null;
    public static Image OrImage = null;
    public static Image NotImage = null;
    public static Image SwitchImage = null;
    public static Image ButtonImage = null;
    public static Image LedImage = null;

    public DPos pos;

    public int inputCount;
    public int height;
    public boolean basicState;
    public boolean isBasic;
    public boolean drawThing = false;
    public ComponentType type;

    public List<Boolean> inputs = new ArrayList<>();
    public List<LogicComponent> inputGates = new ArrayList<>();
    public List<Boolean> outputs = new ArrayList<>();
    public List<List<LogicComponent>> outputGates = new ArrayList<>();
    public int outputCount;

    public LogicComponent(ComponentType type, DPos pos)
    {
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
        inputGates.clear();
        for (int i = 0; i < inputCount; i++)
        {
            inputs.add(false);
            inputGates.add(null);
        }
    }

    public void ResetOutputs()
    {
        outputGates.clear();
        outputs.clear();
        for (int i = 0; i < outputCount; i++)
        {
            outputGates.add(new ArrayList<>());
            outputs.add(false);
        }
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
        for (int i2 = 0; i2 < outputGates.get(index).size(); i2++)
            outputGates.get(index).get(i2).UpdateInput(this, state);
    }

    public void UpdateInput(LogicComponent gate, boolean state)
    {
        int index = inputGates.indexOf(gate);
        if (index == -1)
            return;
        inputs.set(index, state);
        //UpdateOutputState();
    }

    public void UpdateGate()
    {
        UpdateOutputState();
    }

}
