package com.marcel.ParsingAndStuff;

import com.marcel.Rendering.MainRenderController;
import com.marcel.Rendering.Renderers.LogicComponent;
import com.marcel.Rendering.Utils.DPos;
import com.marcel.Rendering.Utils.FileStuff;

import java.util.ArrayList;
import java.util.List;

public class LoadLogic
{
    public static LogicComponent GetComponentFromIndex(List<LogicComponent> componentList, List<String> strCompList, int index)
    {
        if (index == - 1)
            return null;

        if (componentList.get(index) == null)
            ParseComponentFromString(componentList, strCompList, strCompList.get(index));

        return componentList.get(index);
    }

    public static List<String> ParseParts(String data)
    {
        System.out.println("Parsing \"" + data +"\"");
        List<String> parts = new ArrayList<>();

        if (data.length() < 1)
            return parts;

        if (data.charAt(0) == '{' && data.charAt(data.length() - 1) == '}')
            data = data.substring(1, data.length() - 1);
        if (data.charAt(0) == '[' && data.charAt(data.length() - 1) == ']')
            data = data.substring(1, data.length() - 1);
        //System.out.println("Parsing \"" + data +"\"");

        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == ' ')
                continue;

            if (data.charAt(i) == '{')
            {
                int sI = i;
                int layer = 1;
                while (layer > 0)
                {
                    i++;
                    if (i >= data.length())
                        break;

                    if (data.charAt(i) == '{')
                        layer++;
                    if (data.charAt(i) == '}')
                        layer--;
                }
                if (i >= data.length())
                {
                    System.out.println("ERROR: INDEX OUT OF BOUNDS!");
                    return null;
                }
                i++;
                String t = data.substring(sI, i);
                //System.out.println("P 2: \"" + t + "\"");
                parts.add(t);
            }
            else if (data.charAt(i) == '[')
            {
                int sI = i;
                int layer = 1;
                while (layer > 0)
                {
                    i++;
                    if (i >= data.length())
                        break;

                    if (data.charAt(i) == '[')
                        layer++;
                    if (data.charAt(i) == ']')
                        layer--;
                }
                if (i >= data.length())
                {
                    System.out.println("ERROR: INDEX OUT OF BOUNDS!");
                    return null;
                }
                i++;
                String t = data.substring(sI, i);
                //System.out.println("P 3: \"" + t + "\"");
                parts.add(t);
            }
            else
            {
                int sI = i;
                while (i < data.length() && data.charAt(i) != ',')
                    i++;
                String t = data.substring(sI, i);
                //System.out.println("P 1: \"" + t + "\"");
                parts.add(t);
            }
        }
        return parts;
    }

    public static void ParseComponentFromString(List<LogicComponent> componentList, List<String> strCompList, String data)
    {
        List<String> parts = ParseParts(data);

        int index = Integer.parseInt(parts.get(0));
        LogicComponent tempComp = new LogicComponent();
        componentList.set(index, tempComp);
        if (parts.get(1).equals("BASIC"))
        {
            String type = parts.get(2);
            boolean basicState = Boolean.parseBoolean(parts.get(3));

            List<String> posParts = ParseParts(parts.get(4));
            DPos pos = new DPos(Double.parseDouble(posParts.get(0)),Double.parseDouble(posParts.get(1)));

            List<String> inputCompStrs = ParseParts(parts.get(5));
            List<LogicComponent> inputComps = new ArrayList<>();
            for (int i = 0; i < inputCompStrs.size(); i++)
                inputComps.add(GetComponentFromIndex(componentList, strCompList, Integer.parseInt(inputCompStrs.get(i))));

            List<String> outputStrComps = ParseParts(parts.get(6));
            List<Boolean> outputBools = new ArrayList<>();
            for (int i = 0; i < outputStrComps.size(); i++)
                outputBools.add(Boolean.parseBoolean(outputStrComps.get(i)));

            tempComp.type = LogicComponent.GetCompTypeFromString(type);
            tempComp.pos = pos;
            tempComp.isBasic = true;
            tempComp.basicState = basicState;
            tempComp.inputCount = inputComps.size();
            tempComp.inputGates = inputComps;
            tempComp.inputs = new ArrayList<>(tempComp.inputCount);
            for (int i = 0; i < tempComp.inputCount; i++)
                tempComp.inputs.add(false);
            tempComp.outputCount = outputBools.size();
            tempComp.outputs =  outputBools;
            tempComp.outputGates = new ArrayList<>(tempComp.outputCount);
            for (int i = 0; i < tempComp.outputCount; i++)
                tempComp.outputGates.add(new ArrayList<>());
            System.out.println();
            tempComp.height = LogicComponent.AndImage.getHeight(null);
            tempComp.drawThing = true;
        }
        else
        {
            System.out.println("ERR: NOT SUPPORTED YET");
            return;
        }
    }


    public static void LoadSave(String path)
    {
        System.out.println("Loading Save...");
        MainRenderController.allowDrawing = false;
        String data = FileStuff.ReadFile(path);
        if (data == null)
        {
            MainRenderController.allowDrawing = true;
            return;
        }
        System.out.println("DATA:");
        System.out.println(data);
        List<String> strCompList = ParseParts(data);

        List<LogicComponent> componentList = new ArrayList<>();
        for (int i = 0; i < strCompList.size(); i++)
            componentList.add(null);
        for (int i = 0; i < strCompList.size(); i++)
            ParseComponentFromString(componentList, strCompList, strCompList.get(i));
        for (int i = 0; i < strCompList.size(); i++)
        {
            LogicComponent comp = componentList.get(i);
            for (int i1 = 0; i1 < comp.inputGates.size(); i1++)
            {
                if (comp.inputGates.get(i1) == null)
                    continue;
                //for (int)
            }
        }
        for (int i = 0; i < strCompList.size(); i++)
        {
            System.out.println("UPDATING: " + i);
            System.out.println(componentList.get(i).inputCount);

            componentList.get(i).UpdateGate();
        }


        MainRenderController.selectedComponent = null;
        MainRenderController.dragCable = false;
        MainRenderController.dragComponent = false;
        MainRenderController.canvas.mainWindowRenderer.logicGates = componentList;
        MainRenderController.allowDrawing = true;
        System.out.println("Loading Done!");
    }
}
