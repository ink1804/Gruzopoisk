package com.ink1804.gruzopoisk.fragmentClases;

import android.util.Log;
import android.widget.Toast;

import com.ink1804.gruzopoisk.Activities.AuthorizationAct;
import com.ink1804.gruzopoisk.jsonClases.ParseJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ink1804 on 05.03.15.
 */
public class JSONAnalysis {
    static final String LOG_TAG = "myLogs";
    public String JSON_Success="",JSON_Errors,JSON_Data,JSON_Person;
    public String id,email="",fio,result,cell="";
    public String lastId="0",predId;
    public static String newCount;
    public String password="";

    public ArrayList<struct> list = new ArrayList<struct>();
    struct JsonStruct;

    public String JsonAuth(){
        try {
            //сделать массив с данными и ошибками
            JSONObject Json_Auth_Full = new JSONObject(ParseJSON.jsonString);
            JSON_Success = Json_Auth_Full.getString("success");

            switch (JSON_Success){
                case "true":
                    JSON_Data = Json_Auth_Full.getString("data");
                    JSONObject JDataObj = new JSONObject(JSON_Data);
                    id = JDataObj.getString("id");
                    email = JDataObj.getString("email");
                    fio = JDataObj.getString("fio");
                    Log.d(LOG_TAG, id+"\n"+email+"\n"+fio);
                    break;
                case "false":
                    JSON_Errors = Json_Auth_Full.getString("errors");
                    JSONObject JErrorsObj = new JSONObject(JSON_Errors);
                    if(AuthorizationAct.type=="email")
                    email = JErrorsObj.getString("email");
                    else cell = JErrorsObj.getString("cell");
                    password = JErrorsObj.getString("password");

                    Log.d(LOG_TAG,email+"\n"+password);
                    break;
                case "":break;
                    default:break;
            }
        } catch (Exception e) {
            Log.d(LOG_TAG,"Где то ошибка");
          //  e.printStackTrace();
        }
        return (email+""+password+cell);

    }
    public void Json_newResult(int i){
        JsonStruct = new struct("ct","cf","n","cp","no");
        try {
            JSONObject Json_List_Full = new JSONObject(ParseJSON.jsonString);
            result = Json_List_Full.getString("result");
            JSONArray jArray = new JSONArray(result);
            Log.d(LOG_TAG,result);
            JSON_Success=result;

                JSONObject OneJsonObj = jArray.getJSONObject(i);

                JsonStruct.city_from = OneJsonObj.getString("city_from");
                JsonStruct.city_to = OneJsonObj.getString("city_to");
                JsonStruct.name = OneJsonObj.getString("name");
                JsonStruct.common_price = OneJsonObj.getString("common_price");
                JsonStruct.note = OneJsonObj.getString("note");

                JsonStruct._url = OneJsonObj.getString("_url");
                list.add(JsonStruct);
        } catch (Exception e) {

        }
    }
    public void Json_listUpdate(){
        try {
            JSONObject Json_News = new JSONObject(ParseJSON.jsonString);
            newCount = Json_News.getString("newCount");
            lastId = Json_News.getString("lastId");
            predId = Json_News.getString("prevLastId");
            Log.d(LOG_TAG, " "+newCount+" "+lastId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void All_Data_To_Arraylist(int t1,int t2,String A[][], String JsonResult){
        int c=0;
        JsonStruct = new struct("ct","cf","n","cp","no");
            try{
                JSONObject Json_List_Full = new JSONObject(JsonResult);
                result = Json_List_Full.getString("result");
                JSONArray jArray = new JSONArray(result);
                for(int t=t1;t<t2;t++){
                    JSONObject OneJsonObj = jArray.getJSONObject(c);
                    c++;
                    A[0][t] = OneJsonObj.getString("city_from");//откуда+
                    A[1][t] = OneJsonObj.getString("city_to");//куда+
                    A[2][t] = OneJsonObj.getString("name");//что+
                    A[3][t] = OneJsonObj.getString("common_price").replace("0 р.","Цена договорная");
                    A[4][t] = OneJsonObj.getString("note");//примечание+
                    A[5][t] = OneJsonObj.getString("_url");//адрес
                    A[6][t] = OneJsonObj.getString("city_from")+" → "+OneJsonObj.getString("city_to")+", "+OneJsonObj.getString("distance_human").replace("&#160;"," ");//
                    A[7][t] = (("!*"+OneJsonObj.getString("common_price")+" р.нал/"+OneJsonObj.getString("nds_price")+
                            " р. б/нал").replace("!*0 р.нал/0 р. б/нал","Цена договорная").replace("/0 р. б/нал","").replace("!*0 р.нал/","")).replace("!*","");
                    A[8][t] = OneJsonObj.getString("weight")+" тонн • "+ OneJsonObj.getString("vol")+" м3 "+getBody(OneJsonObj.getString("body"));

                    A[9][t] = ((OneJsonObj.getString("firm_name")+"\n"+OneJsonObj.getString("firm_location")).replace("Физическое лицо\nnull",OneJsonObj.getString("user_fio")+", Физ. лицо")).replace("null,","").replace("null","");
                    A[10][t] =  OneJsonObj.getString("pre_pay").replace("1", "предоплата").replace("0","без предоплаты");
                    A[11][t] = "https://gruzopoisk.ru"+OneJsonObj.getString("firm_logo");
                    A[12][t] = "https://gruzopoisk.ru"+OneJsonObj.getString("user_avatar");
                    A[13][t] = OneJsonObj.getString("lastId");
                    A[14][t] = OneJsonObj.getString("id");
                    A[15][t] = getLoadDate(OneJsonObj);
                    //Log.d(LOG_TAG,getLoadDate(OneJsonObj));
                }
            }catch (Exception e){}Log.d(LOG_TAG,"опять");
    }
    public String getBody(String bodyString){
        String body = bodyString.replace("[","").replace("]","").replace("1,2,3,4","закрытый").replace("8,9,10,11","открытый")
                .replace("5,6,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41","другое")
                .replace("10","пикап").replace("11","шаланда").replace("12","автобус").replace("13","автовоз").replace("14","автовышка")
                .replace("15","автотранспортер").replace("16","бетоновоз").replace("17","бензовоз").replace("18","газовоз")
                .replace("19","зерновоз").replace("20","коневоз").replace("21","конт. площадка").replace("22","кормовоз")
                .replace("23","кран").replace("24","лесовоз").replace("25","манипулятор").replace("26","микроавтобус")
                .replace("27","муковоз").replace("28","низкорамный").replace("29","низкорам. платф.").replace("30","панелевоз")
                .replace("31","самосвал").replace("32","седельный тягач").replace("33","скотовоз").replace("34","стекловоз")
                .replace("35","трал").replace("36","трубовоз").replace("37","цементовоз").replace("38","цистерна")
                .replace("39","щеповоз").replace("40","эвакуатор").replace("41","балковоз")
                .replace("1","тентованный").replace("2","контейнер").replace("3","фургон").replace("4","цельнометалл")
                .replace("5","рефрижератор").replace("6","изотермический").replace("8","бортовой").replace("9","открытый конт.");
        if(body!="") body = "• "+body;
        return body;
    }
    public void getPersonData(){
        try {
            JSONObject personJson = new JSONObject(ParseJSON.jsonString);
           Log.d(LOG_TAG,"person: "+ParseJSON.jsonString);
        }catch (Exception e){}
    }

    public String getLoadDate(JSONObject loadDateString){
        long from,to;
        Date dateFrom,dateTo;
        try {
            JSONArray ldArray = new JSONArray(loadDateString.getString("load_date"));
            from = Long.parseLong(ldArray.getString(0))*1000;
            to = Long.parseLong(ldArray.getString(1))*1000;
            DateFormat dateFormat = new SimpleDateFormat("dd.MM");
            dateFrom = (new Date(Long.parseLong(ldArray.getString(0))*1000));
            if(from!=to){
                dateTo = (new Date(Long.parseLong(ldArray.getString(1))*1000));
                return dateFormat.format(dateFrom)+" - "+dateFormat.format(dateTo);}
            else{
                dateTo = (new Date(to+24*60*60*1000));
                return dateFormat.format(dateFrom);}

        }catch (Exception e){}
        return "";
    }
}
