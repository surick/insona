package com.jieweifu.common.gizwits;

import com.alibaba.fastjson.JSON;
import com.jieweifu.models.gizwits.Data;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 陶Lyn
 * on 2018/5/9.
 */
public class FindJsonUtil {

    public static List<String> format(String jsonStr) {
        Stack<JsonStack> stringStack = new Stack<JsonStack>();
        List<Integer> indexList = new LinkedList<Integer>();
        List<String> jsonList = new ArrayList<String>();
        for (int i = 0; i < jsonStr.length(); i++) {
            if (jsonStr.charAt(i) == '{' || jsonStr.charAt(i) == '[') {
                stringStack.push(new JsonStack(i, jsonStr.charAt(i)));
            } else if (jsonStr.charAt(i) == '}' || jsonStr.charAt(i) == ']') {
                if (!stringStack.empty()) {
                    JsonStack js = stringStack.peek();
                    if (jsonStr.charAt(i) == '}' && js.getStr() == '{') {
                        js = stringStack.pop();
                    } else if (jsonStr.charAt(i) == ']' && js.getStr() == '[') {
                        js = stringStack.pop();
                    }
                    indexList.add(js.getIndex());
                    indexList.add(i);
                }
                if (stringStack.empty()) {
                    String tempStr = getJsonStr(indexList, jsonStr);
                    if (!(tempStr == null || tempStr.isEmpty())) {
                        jsonList.add(tempStr);
                    }
                    indexList.clear();
                }
            }
        }



        if (indexList != null && indexList.size() > 0) {
            String tempStr = getJsonStr(indexList, jsonStr);
            if (!(tempStr == null || tempStr.isEmpty())) {
                jsonList.add(tempStr);
            }
        }
        return jsonList;
    }

    private static String getJsonStr(List<Integer> indexList,String str) {
        String temp= "";
        for(int i = indexList.size() -1 ; i>=0 ; i=i-2){
            try {
                temp = str.substring(indexList.get(i - 1), indexList.get(i)+1);
                JSON.parse(temp);
                return str.substring(indexList.get(i - 1), indexList.get(i)+1);
            }catch (Exception e){
                continue;
            }
        }
        return null;
    }
    static class JsonStack {
        private Integer index;
        private char str;

        public JsonStack(Integer index, char str) {
            this.index = index;
            this.str = str;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public char getStr() {
            return str;
        }

        public void setStr(char str) {
            this.str = str;
        }
    }


    public static String timeStamp2Date(String seconds){
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
                      return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }



    public static void main(String[] args) {
      /*  Base64.Decoder decoder = Base64.getDecoder();
        String base = "AAAAA940AACRBtzNAgIAABpSeyJpZCI6MCwicmVzcG9uc2VzIjpbeyJuYW1lIjoiZ2V0X2l0ZW0iLCJzZXF1ZW5jZSI6MCwicmV0Ijoib2siLCJkYXRhIjp7Iml0ZW0iOnsiaWQiOjAsInR5cGUiOiJwcm9qZWN0IiwibmFtZSI6IkV4aGliaXRpb24iLCJhdHRyaWJ1dGVzIjp7Im1hbnVmYWN0dXJlIjoiaW5Tb25hIENvLEx0ZCIsInByb3RvY29sX3ZlcnNpb24iOiIyLjAiLCJhcHBsaWNhdGlvbiI6IlNNQVJUX0hPTUUiLCJ0aW1lem9uZSI6Iis4OjAwIiwicHJvamVjdF92ZXJzaW9uIjoiMjAxODA1MDYiLCJsb2NhdGlvbiI6IlN1emhvdSIsImhvc3RfZGV2aWNlIjo1OH0sInN1Yml0ZW1zIjpbeyJpZCI6MSwidHlwZSI6ImNvbnRhaW5lciIsIm5hbWUiOiJFeGhpYml0aW9uIEhhbGwiLCJhdHRyaWJ1dGVzIjp7ImVuZHBvaW50cyI6Wzc1LDgxXSwiaHZhY3MiOltdLCJjdXJ0YWlucyI6W10sImxpZ2h0cyI6WzQxLDQ2LDUxLDU2LDYsOCwxNywxOSwyNSwyNywzMywzNV19LCJzdWJpdGVtcyI6W3siaWQiOjIsInR5cGUiOiJjb250YWluZXIiLCJuYW1lIjoiQmVkcm9vbSIsImF0dHJpYnV0ZXMiOnsiZW5kcG9pbnRzIjpbXSwiaHZhY3MiOltdLCJjdXJ0YWlucyI6WzYzLDY4XSwibGlnaHRzIjpbXX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjozLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU3ZDVmMSIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjo2LCJ0eXBlIjoibGlnaHQiLCJuYW1lIjoiU3dpdGNoIExpZ2h0IDEiLCJhdHRyaWJ1dGVzIjp7ImxpZ2h0X3R5cGUiOiJQRU5EQU5UIiwiY29sb3JhYmxlIjpmYWxzZSwiZGltbWVyYWJsZSI6ZmFsc2V9LCJzdWJpdGVtcyI6W119LHsiaWQiOjgsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJTd2l0Y2ggTGlnaHQgMiIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IkJFTFQiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjpmYWxzZX0sInN1Yml0ZW1zIjpbXX0seyJpZCI6MTEsInR5cGUiOiJrZXlwYWQiLCJuYW1lIjoia2V5cGFkIiwiYXR0cmlidXRlcyI6eyJzdXBwb3J0QWN0aW9uIjpbXSwiYnV0dG9uQXR0cmlidXRlIjpbXSwiY29sb3JhYmxlIjp0cnVlLCJwcmVzZXRfY29sb3JfbGlzdCI6W10sImxlZEFjdGlvbiI6W10sImJ1dHRvbkNvdW50Ijo0fSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjE0LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4OTkzMSIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjoxNywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCAzIiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjoxOSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA0IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjIyLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4ZjQ3MCIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjoyNSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA1IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjoyNywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA2IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjMwLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4ODliYyIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjozMywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA3IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjozNSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA4IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjM4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDIxOGUiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NDEsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMSIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjQzLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDMxNjIiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NDYsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMiIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjQ4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDMxZGMiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTEsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMyIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjUzLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDUwMTAiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTYsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgNCIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjU4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkhvbWVDZW50ZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9oYyIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1IQy0wMSIsInNuIjoiMDAwMDAwMDAwMDAwMDAwMCIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS4wLjAuMCIsInN3dmVyc2lvbiI6IjEuMC4wLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTksInR5cGUiOiJ6YXAiLCJuYW1lIjoiemlnYmVlIGNvbnRyb2xlciBvZiBob21lY2VudGVyIiwiYXR0cmlidXRlcyI6e30sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo2MiwidHlwZSI6ImRldmljZSIsIm5hbWUiOiJDdXJ0YWluIENvbnRyb2xsZXIgMSIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfY3VydGFpbiIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1DMDEtV0NNLURUODJFIiwic24iOiIwMDBkNmYwMDBkZDI4M2ZkIiwidmVyc2lvbiI6eyJod3ZlcnNpb24iOiIxLjQiLCJzd3ZlcnNpb24iOiIyLjMuMy4wIn19LCJzdWJpdGVtcyI6W3siaWQiOjYzLCJ0eXBlIjoiY3VydGFpbiIsIm5hbWUiOiJCZWRyb29tIERyYXBlIDEiLCJhdHRyaWJ1dGVzIjp7ImFuZ2xlYWJsZSI6ZmFsc2UsImN1cnRhaW50eXBlIjoiRFJBUEUiLCJwZXJjZW50YWJsZSI6dHJ1ZX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo2NywidHlwZSI6ImRldmljZSIsIm5hbWUiOiJDdXJ0YWluIENvbnRyb2xsZXIgMiIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfY3VydGFpbiIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1DMDEtV0NNLURUODJFIiwic24iOiIwMDBkNmYwMDBkZDI5YWMwIiwidmVyc2lvbiI6eyJod3ZlcnNpb24iOiIxLjQiLCJzd3ZlcnNpb24iOiIyLjMuMy4wIn19LCJzdWJpdGVtcyI6W3siaWQiOjY4LCJ0eXBlIjoiY3VydGFpbiIsIm5hbWUiOiJCZWRyb29tIERyYXBlIDIiLCJhdHRyaWJ1dGVzIjp7ImFuZ2xlYWJsZSI6ZmFsc2UsImN1cnRhaW50eXBlIjoiRFJBUEUiLCJwZXJjZW50YWJsZSI6dHJ1ZX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo3MiwidHlwZSI6ImRldmljZSIsIm5hbWUiOiJZb3V6aHVhbiBNdXNpYyBQbGF5ZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL3lvdXpodWFuL1laLTMwMCIsIm1hbnVmIjoiWW91emh1YW4iLCJtb2RlbCI6IllaLTMwMCIsInNuIjoiMDAwMDAwMDAiLCJ2ZXJzaW9uIjp7InNvZnR3YXJlIjoiWVotMzAwIG4uMjAxNjA1MzAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NzMsInR5cGUiOiJtZWRpYSIsIm5hbWUiOiJZb3V6aHVhbiBNdXNpYyBQbGF5ZXIiLCJhdHRyaWJ1dGVzIjp7InBsYXllcnR5cGUiOiJMT0NBTF9QTEFZRVIiLCJlbmFibGVyZW1vdGUiOnRydWUsIm1lZGlhdHlwZSI6IkFVRElPIiwicmVtb3RlX2tleXMiOlsiVk9MVVAiLCJWT0xET1dOIiwiTVVURSIsIlBMQVkiLCJQQVVTRSIsIk5FWFQiLCJQUkVWIl19LCJzdWJpdGVtcyI6W119XX0seyJpZCI6NzgsInR5cGUiOiJkZXZpY2UiLCJuYW1lIjoiTWVkaWEgQ29udHJvbGxlciIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfbWVkaWEiLCJtYW51ZiI6IkJldml4IiwibW9kZWwiOiJIaXRhY2gtQUM1MDAiLCJzbiI6IjAwZGYxMjMyZTA4YzIyNTAiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NzksInR5cGUiOiJtZWRpYSIsIm5hbWUiOiJCZXZpeCBtZWRpYSIsImF0dHJpYnV0ZXMiOnsicGxheWVydHlwZSI6IkxPQ0FMX1BMQVlFUiIsImVuYWJsZXJlbW90ZSI6dHJ1ZSwibWVkaWF0eXBlIjoiVklERU8iLCJyZW1vdGVfa2V5cyI6WyJQV1IiLCJQV1JPTiIsIlBXUk9GRiIsIlNUQU5EQlkiLCJWT0xVUCIsIlZPTERPV04iLCJNVVRFIiwiUExBWSIsIlBBVVNFIiwiU1RPUCIsIlJFQyIsIk5FWFQiLCJQUkVWIiwiRkZXRCIsIkZSRVYiLCJTRldEIiwiVVAiLCJET1dOIiwiTEVGVCIsIlJJR0hUIiwiRU5URVIiLCJDTEVBUiIsIk5VTTAiLCJOVU0xIiwiTlVNMiIsIk5VTTMiLCJOVU00IiwiTlVNNSIsIk5VTTYiLCJOVU03IiwiTlVNOCIsIk5VTTkiLCJBU1RFUklTSyIsIlBPVU5EIiwiUk9PVE1FTlUiLCJQT1BNRU5VIiwiSU5GTyIsIlNFVFRJTkciLCJSRVRVUk4iLCJFWElUIiwiRUpFQ1QiLCJTVUJUSVRMRSIsIlRSQUNLIiwiU0VBUkNIIiwiWk9PTUlOIiwiWk9PTU9VVCIsIlNIT1ciLCJSRVBFQVQiLCJBX0IiLCJBTkdMRSIsIlBJUCIsIkZVTkNUSU9OQSIsIkZVTkNUSU9OQiIsIkZVTkNUSU9OQyIsIkZVTkNUSU9ORCJdfSwic3ViaXRlbXMiOltdfV19XX19fV19";
        String b = "";
        try {

            b= new String (decoder.decode(base.trim()),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        FindJsonUtil fju=new FindJsonUtil();
        List<String> list=fju.format(b);
        for(String  aa:list){
            System.out.println("==>"+aa.length());
        }*/
        float a=1.52628971109200000763e+09f;
        int b=(int)a;
        System.out.println(System.currentTimeMillis());
        System.out.println(b);
        BigDecimal bigDecimal =new BigDecimal(a);
        String aa=bigDecimal.toString();
        System.out.println( timeStamp2Date(aa));
       ;
    }

    public static String longToDate(long lo){
        double a=1.22;
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }
}

