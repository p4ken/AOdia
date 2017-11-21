package com.kamelong.JPTI;


import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.kamelong.OuDia.OuDiaFile;
import com.kamelong.tool.Color;
import com.kamelong.tool.Font;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 時刻表路線を記録するクラス
 * 時刻表路線はOuDiaファイル１つに対応する
 */
public class Service {
    private JPTI jpti;

    private String name="";
    private Map<Route,Integer> route=new LinkedHashMap<>();
    private int stationWidth=-1;
    private int trainWidth=-1;
    private String startTime=null;
    private int defaulyStationSpace=-1;
    private String comment=null;

    private Color diaTextColor=null;
    private Color diaBackColor=null;
    private Color diaTrainColor=null;
    private Color diaAxisColor=null;
    private ArrayList<Font> timeTableFont=new ArrayList();
    private Font timeTableVFont=null;
    private Font diaStationFont=null;
    private Font diaTimeFont=null;
    private Font diaTrainFont=null;
    private Font commentFont=null;

    protected ArrayList<Train>trainList=new ArrayList<>();

    protected static final String NAME="service_name";
    protected static final String ROUTE="route_array";
    private static final String ROUTE_ID="route_id";
    private static final String DIRECTION ="direction";
    private static final String STATION_WIDTH="station_width";
    private static final String TRAIN_WIDTH="train_width";
    private static final String START_TIME="timetable_start_time";
    private static final String STATION_SPACING="station_spacing";
    private static final String COMMENT="comment_text";
    private static final String TRAIN="train";

    private static final String DIA_TEXT_COLOR="dia_text_color";
    private static final String DIA_BACK_COLOR="dia_back_color";
    private static final String DIA_TRAIN_COLOR="dia_train_color";
    private static final String DIA_AXICS_COLOR="dia_axics_color";
    private static final String TIMETABLE_FONT="font_timetable";
    private static final String TIMETABLE_VFONT="font_vfont";
    private static final String DIA_STATION_FONT="font_dia_station";
    private static final String DIA_TIME_FONT="font_dia_time";
    private static final String DIA_TRAIN_FONT="font_dia_train";
    private static final String COMMENT_FONT="font_comment";

    public Service(JPTI jpti){
        this.jpti=jpti;
    }
    public Service(JPTI jpti, JsonObject json){
        this(jpti);
        try{

            name=json.getString(NAME,"");
            JsonArray routeArray=json.get(ROUTE).asArray();
            for(int i=0;i<routeArray.size();i++){
                route.put(jpti.routeList.get(routeArray.get(i).asObject().getInt(ROUTE_ID,0)),routeArray.get(i).asObject().getInt(DIRECTION,0));
            }
            stationWidth=json.getInt(STATION_WIDTH,7);
            trainWidth=json.getInt(TRAIN_WIDTH,5);
            startTime=json.getString(START_TIME,"300");

            defaulyStationSpace=json.getInt(STATION_SPACING,60);
            comment=json.getString(COMMENT,"");
            diaTextColor=new Color(Long.decode(json.getString(DIA_TEXT_COLOR,"#000000")).intValue());
            diaBackColor=new Color(Long.decode(json.getString(DIA_BACK_COLOR,"#ffffff")).intValue());
            diaTrainColor=new Color(Long.decode(json.getString(DIA_TRAIN_COLOR,"#000000")).intValue());
            diaAxisColor=new Color(Long.decode(json.getString(DIA_AXICS_COLOR,"#000000")).intValue());
            JsonArray timeTableFontArray=json.get(TIMETABLE_FONT).asArray();
            for(int i=0;i<timeTableFontArray.size();i++){
                timeTableFont.add(new Font(timeTableFontArray.get(i).asObject()));
            }
            timeTableVFont=new Font(json.get(TIMETABLE_VFONT).asObject());
            diaStationFont=new Font(json.get(DIA_STATION_FONT).asObject());
            diaTimeFont=new Font(json.get(DIA_TIME_FONT).asObject());
            diaTrainFont=new Font(json.get(DIA_TRAIN_FONT).asObject());
            commentFont=new Font(json.get(COMMENT_FONT).asObject());

            JsonArray trainArray=json.get(TRAIN).asArray();
            for(int i=0;i<trainArray.size();i++){
                trainList.add(new Train(jpti,this,trainArray.get(i).asObject()));
            }


        }catch(Exception e){
            e.printStackTrace();

        }
    }
    public  Service(JPTI jpti,ArrayList<Route>routeList){
        this.jpti=jpti;
        for(Route r:routeList){
            route.put(r,0);
        }
        name=((Route)route.keySet().toArray()[0]).getName();
        for(int i=0;i<jpti.getTripSize();i++){
            if(route.containsKey(jpti.getTrip(i).getRoute())){
                trainList.add(new Train(jpti,this,jpti.getTrip(i)));
            }
        }
    }

    public JsonObject makeJSONObject(){
        JsonObject json=new JsonObject();
        try{
            json.add(NAME,name);
            JsonArray routeArray=new JsonArray();
            for (Map.Entry<Route, Integer> bar : route.entrySet()) {
                JsonObject routeObject=new JsonObject();
                routeObject.add(ROUTE_ID,bar.getKey().index());
                routeObject.add(DIRECTION,bar.getValue());
                routeArray.add(routeObject);
            }
            json.add(ROUTE,routeArray);
            if(stationWidth>-1){
                json.add(STATION_WIDTH,stationWidth);
            }
            if(trainWidth>-1){
                json.add(TRAIN_WIDTH,trainWidth);
            }
            if(startTime!=null){
                json.add(START_TIME,startTime);
            }
            if(defaulyStationSpace>-1){
                json.add(STATION_SPACING,defaulyStationSpace);
            }
            if(comment!=null){
                json.add(COMMENT,comment);
            }
            if(diaTextColor!=null){
                json.add(DIA_TEXT_COLOR,diaTextColor.getHTMLColor());
            }
            if(diaBackColor!=null){
                json.add(DIA_BACK_COLOR,diaBackColor.getHTMLColor());
            }
            if(diaTrainColor!=null){
                json.add(DIA_TRAIN_COLOR,diaTrainColor.getHTMLColor());
            }
            if(diaAxisColor!=null){
                json.add(DIA_AXICS_COLOR,diaAxisColor.getHTMLColor());
            }
            JsonArray timetableFontArray=new JsonArray();
            for(Font font:timeTableFont){
                timetableFontArray.add(font.makeJsonObject());
            }
            json.add(TIMETABLE_FONT,timetableFontArray);
            if(timeTableVFont!=null){
                json.add(TIMETABLE_VFONT,timeTableVFont.makeJsonObject());
            }
            if(diaStationFont!=null){
                json.add(DIA_STATION_FONT,diaStationFont.makeJsonObject());
            }
            if(diaTimeFont!=null){
                json.add(DIA_TIME_FONT,diaTimeFont.makeJsonObject());
            }
            if(diaTrainFont!=null){
                json.add(DIA_TRAIN_FONT,diaTrainFont.makeJsonObject());
            }
            if(commentFont!=null){
                json.add(COMMENT_FONT,commentFont.makeJsonObject());
            }
            JsonArray trainArray=new JsonArray();
            for(Train train:trainList){
                trainArray.add(train.makeJSONObject());
            }
            json.add(TRAIN,trainArray);


        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public void loadOuDia(OuDiaFile diaFile){
        name=diaFile.getLineName();
        stationWidth=diaFile.getStationNameLength();
        trainWidth=diaFile.getTrainWidth();
        startTime=timeInt2String(diaFile.getStartTime());
        defaulyStationSpace=diaFile.getStationDistanceDefault();
        comment=diaFile.getComment();
        diaTextColor=diaFile.getDiaTextColor();
        diaBackColor=diaFile.getBackGroundColor();
        diaTrainColor=diaFile.getTrainColor();
        diaAxisColor=diaFile.getAxisColor();
        timeTableFont=diaFile.getTableFont();
        timeTableVFont=diaFile.getVfont();
        diaStationFont=diaFile.getStationFont();
        diaTimeFont=diaFile.getDiaTimeFont();
        diaTrainFont=diaFile.getDiaTextFont();
        commentFont=diaFile.getCommnetFont();
    }
    public void loadOuDia2(OuDiaFile diaFile){
        int blockID=0;
        for(int diaNum=0;diaNum<diaFile.getDiaNum();diaNum++){
            for(int i=0;i<diaFile.getTrainNum(diaNum,0);i++){
                trainList.add(new Train(jpti,this,jpti.getCalendar(diaNum),diaFile,diaFile.getTrain(diaNum,0,i),blockID));
                blockID++;
            }
            for(int i=0;i<diaFile.getTrainNum(diaNum,1);i++){
                trainList.add(new Train(jpti,this,jpti.getCalendar(diaNum),diaFile,diaFile.getTrain(diaNum,1,i),blockID));
                blockID++;
            }
        }

    }


    protected static int timeString2Int(String time){
        int hh=Integer.parseInt(time.split(":",-1)[0]);
        int mm=Integer.parseInt(time.split(":",-1)[1]);
        int ss=Integer.parseInt(time.split(":",-1)[2]);
        return hh*3600+mm*60+ss;
    }
    protected static String timeInt2String(int time){
        int ss=time%60;
        time=time/60;
        int mm=time%60;
        time=time/60;
        int hh=time%24;
        return String.format("%02d",hh)+":"+String.format("%02d",mm)+":"+String.format("%02d",ss);
    }
    /**
     * 路線のRouteのリストを渡す
     * @return
     */
    public ArrayList<Route>getRouteList(int direction){
        ArrayList<Route> result=new ArrayList<>();
        for(Route i:route.keySet()){
            result.add(i);
        }
        if(direction==1){
            Collections.reverse(result);
        }
        return result;
    }
    public void addRoute(Route r, int direct){
        route.put(r,direct);
    }

    /**
     * routeIDからそのrouteがservice中の何番目に位置するかを返す
     * @return routeIDが存在しないときは-1を返す
     */
    public int routeIndex(Route mRoute, int direction){
        int result=-1;
        int i=0;
        for(Route id:route.keySet()){
            if(id==mRoute){
                result=i;
                break;
            }
            i++;
        }
        if(result==-1){
            //このrouteIDはservice内に存在しないので-1を返す
            return -1;
        }
        if(direction==1){
            result=route.size()-result-1;
        }
        return result;
    }
    /**
     * このObjectはjpti中のリストの何番目に位置するのかを返す
     */
    public int index(){
        if(jpti.serviceList.contains(this)) {
            return jpti.serviceList.indexOf(this);
        }else{
            Exception e=new Exception();
            e.printStackTrace();
            return -1;
        }
    }
    public String getName(){
        return name;
    }
    public int getTimeTableFontNum(){
        return timeTableFont.size();
    }
    public Font getTimeTableFont(int index){
        try {
            return timeTableFont.get(index);
        }catch(ArrayIndexOutOfBoundsException e){
            return new Font();
        }
    }
    public Font getTimeTableVFont(){
        return timeTableVFont;
    }
    public Font getDiaStationFont(){
        return diaStationFont;
    }
    public Font getDiaTimeFont(){
        return diaTimeFont;
    }
    public Font getDiaTrainFont(){
        return diaTrainFont;
    }
    public Font getCommentFont(){
        return commentFont;
    }
    public Color getDiaTextColor(){
        return diaTextColor;
    }
    public Color getDiaBackColor(){
        return diaBackColor;
    }
    public Color getDiaTrainColor(){
        return diaTrainColor;
    }
    public Color getDiaAxisColor(){
        return diaAxisColor;
    }

    public ArrayList<Train>getTrainList(){
        return trainList;
    }
    public String getComment(){
        return comment;
    }
    public int getDiagramStartTime(){
        if(startTime==null){
            return 60*60*3;
        }
        return (timeString2Int(startTime))/3600*3600;
    }



}
