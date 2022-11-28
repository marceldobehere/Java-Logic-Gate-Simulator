package com.marcel.ParsingAndStuff;

import com.marcel.rendering.MainRenderController;
import com.marcel.rendering.renderers.ComponentConnection;
import com.marcel.rendering.renderers.LogicComponent;
import com.marcel.rendering.utils.DPos;
import com.marcel.rendering.utils.FileStuff;

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
        data = data.trim();
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

        //System.out.println("Parsing component: " + data);



        int index = Integer.parseInt(parts.get(0));
        LogicComponent tempComp = new LogicComponent(MainRenderController.canvas.mainWindowRenderer);
        componentList.set(index, tempComp);
        if (parts.get(1).equals("BASIC"))
        {
            String type = parts.get(2);
            boolean basicState = Boolean.parseBoolean(parts.get(3));

            List<String> posParts = ParseParts(parts.get(4));
            DPos pos = new DPos(Double.parseDouble(posParts.get(0)),Double.parseDouble(posParts.get(1)));

            List<String> inputStrs = ParseParts(parts.get(5));
            List<Boolean> inputBools = new ArrayList<>();
            for (int i = 0; i < inputStrs.size(); i++)
                inputBools.add(Boolean.parseBoolean(inputStrs.get(i)));

            List<String> outputStrs = ParseParts(parts.get(6));
            List<Boolean> outputBools = new ArrayList<>();
            for (int i = 0; i < outputStrs.size(); i++)
                outputBools.add(Boolean.parseBoolean(outputStrs.get(i)));

            tempComp.type = LogicComponent.GetCompTypeFromString(type);
            tempComp.pos = pos;
            tempComp.isBasic = true;
            tempComp.basicState = basicState;
            tempComp.inputCount = inputBools.size();
            tempComp.inputs = inputBools;
            tempComp.outputCount = outputBools.size();
            tempComp.outputs =  outputBools;
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

    static void ParseConnectionFromString(List<LogicComponent> componentList, List<String> strCompList, List<ComponentConnection> connectionList, String data)
    {
        List<String> parts = ParseParts(data);
        LogicComponent from = GetComponentFromIndex(componentList, strCompList, Integer.parseInt(parts.get(0)));
        int fromIndex = Integer.parseInt(parts.get(1));
        LogicComponent to = GetComponentFromIndex(componentList, strCompList, Integer.parseInt(parts.get(2)));
        int toIndex = Integer.parseInt(parts.get(3));



        connectionList.add(new ComponentConnection(from, fromIndex, to, toIndex));
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
        List<String> mainSaveParts = ParseParts(data);

        //System.out.println("PARTS 1: " + mainSaveParts.get(0));
        //System.out.println("PARTS 2: " + mainSaveParts.get(1));

        List<LogicComponent> comps = new ArrayList<>();
        List<ComponentConnection> conns = new ArrayList<>();

        List<String> compParts = ParseParts(mainSaveParts.get(0));
        List<String> connParts = ParseParts(mainSaveParts.get(1));

        System.out.println("LOGIC GATES:");
        for (int i = 0; i < compParts.size(); i++)
        {
            comps.add(null);
            ParseComponentFromString(comps, compParts, compParts.get(i));
        }

        System.out.println("CONNECTIONS:");
        for (int i = 0; i < connParts.size(); i++)
        {
            //ParseComponentFromString(comps, compParts, compParts.get(i));
            ParseConnectionFromString(comps, compParts, conns, connParts.get(i));
        }






//
//        List<LogicComponent> componentList = new ArrayList<>();
//        for (int i = 0; i < mainSaveParts.size(); i++)
//            componentList.add(null);
//        for (int i = 0; i < mainSaveParts.size(); i++)
//            ParseComponentFromString(componentList, mainSaveParts, mainSaveParts.get(i));
//        for (int i = 0; i < mainSaveParts.size(); i++)
//        {
//            LogicComponent comp = componentList.get(i);
//            for (int i1 = 0; i1 < comp.inputGates.size(); i1++)
//            {
//                if (comp.inputGates.get(i1) == null)
//                    continue;
//                //for (int)
//            }
//        }
//        for (int i = 0; i < mainSaveParts.size(); i++)
//        {
//            System.out.println("UPDATING: " + i);
//            System.out.println(componentList.get(i).inputCount);
//
//            componentList.get(i).UpdateGate();
//        }


        MainRenderController.selectedComponent = null;
        MainRenderController.dragCable = false;
        MainRenderController.dragComponent = false;
        MainRenderController.canvas.mainWindowRenderer.logicGates = comps;
        MainRenderController.canvas.mainWindowRenderer.connections = conns;
        MainRenderController.allowDrawing = true;
        System.out.println("Loading Done!");
    }
}
