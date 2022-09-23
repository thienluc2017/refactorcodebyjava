//import org.json.JSONArray;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {


    public static String format(long num) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        nf.setMaximumFractionDigits(2);
        return nf.format(num);
    }


    public static String renderHeader(String name) {
        return "Statement for " + name + "\n";
    }

    public static String renderLines(Object plays, Object invoice) {
        int totalAmount = 0;
        int volumeCredits = 0;
        String result = " ";
        JSONObject invoicejsonObject = (JSONObject) invoice;
        JSONArray performances = (JSONArray) invoicejsonObject.get("performances");
        JSONObject playsjsonObject = (JSONObject) plays;
        for (int i = 0; i < performances.size(); i++) {
            JSONObject perf = (JSONObject) performances.get(i);
            String playid = perf.get("playID").toString();
            JSONObject play = (JSONObject) playsjsonObject.get(playid);

            long thisAmount = 0;
            switch ((String) play.get("type")) {
                case "tragedy":
                    thisAmount = 40000;
                    if ((long) perf.get("audience") > 30) {
                        thisAmount += 1000 * ((long) perf.get("audience") - 30);
                    }
                    break;
                case "comedy":
                    thisAmount = 30000;
                    if ((long) perf.get("audience") > 20) {
                        thisAmount += 10000 + 500 * ((long) perf.get("audience") - 20);
                    }
                    thisAmount += 300 * (long) perf.get("audience");
                    break;
                default:
                    String mess = "unknow type: " + play.get("type");
                    throw new Error(mess);
            }
            volumeCredits += Math.max((long) perf.get("audience") - 30, 0);
            if (((String) play.get("type")).compareTo("comedy") == 0) {
                volumeCredits += Math.floor((long) perf.get("audience") / 5);
            }
            result += (String) play.get("name") + " " + format(thisAmount / 100) + " " + perf.get("audience") + " seats\n";
            totalAmount += thisAmount;
        }
        return result;


    }

    public static int calculateTotalAmount(Object plays, Object invoice) {
        int totalAmount = 0;
        String result = " ";
        JSONObject invoicejsonObject = (JSONObject) invoice;
        JSONArray performances = (JSONArray) invoicejsonObject.get("performances");
        JSONObject playsjsonObject = (JSONObject) plays;
        for (int i = 0; i < performances.size(); i++) {
            JSONObject perf = (JSONObject) performances.get(i);
            String playid = perf.get("playID").toString();
            JSONObject play = (JSONObject) playsjsonObject.get(playid);

            long thisAmount = 0;
            switch ((String) play.get("type")) {
                case "tragedy":
                    thisAmount = 40000;
                    if ((long) perf.get("audience") > 30) {
                        thisAmount += 1000 * ((long) perf.get("audience") - 30);
                    }
                    break;
                case "comedy":
                    thisAmount = 30000;
                    if ((long) perf.get("audience") > 20) {
                        thisAmount += 10000 + 500 * ((long) perf.get("audience") - 20);
                    }
                    thisAmount += 300 * (long) perf.get("audience");
                    break;
                default:
                    String mess = "unknow type: " + play.get("type");
                    throw new Error(mess);
            }
            totalAmount += thisAmount;
        }
        return totalAmount;



    }
    public static int calculateVolumeCredits(Object plays, Object invoice) {
        int volumeCredits = 0;
        String result = " ";
        JSONObject invoicejsonObject = (JSONObject) invoice;
        JSONArray performances = (JSONArray) invoicejsonObject.get("performances");
        JSONObject playsjsonObject = (JSONObject) plays;
        for (int i = 0; i < performances.size(); i++) {
            JSONObject perf = (JSONObject) performances.get(i);
            String playid = perf.get("playID").toString();
            JSONObject play = (JSONObject) playsjsonObject.get(playid);
            volumeCredits += Math.max((long) perf.get("audience") - 30, 0);
            if (((String) play.get("type")).compareTo("comedy") == 0) {
                volumeCredits += Math.floor((long) perf.get("audience") / 5);
            }
        }
        return volumeCredits;

    }
    public static String renderFooter(int totalAmount,int volumeCredits) {
        String result=" ";
        result += "Amount owed is " + format(totalAmount / 100) + "\n";
         return result + "You earned " + volumeCredits + "credits\n";
    }


    public static String statement(Object plays, Object invoice) throws Exception {
        int totalAmount = calculateTotalAmount(plays,invoice);
        int volumeCredits = calculateVolumeCredits(plays,invoice);
        JSONObject invoicejsonObject = (JSONObject) invoice;
        String result = renderHeader((String) invoicejsonObject.get("customer"));
        result += renderLines(plays,invoice);
        return result+renderFooter(totalAmount,volumeCredits);

    }
    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        Object plays = parser.parse(new FileReader("C:\\Users\\CHU DANG PHU\\IdeaProjects\\untitled2\\src\\plays.json"));
        Object invoices = parser.parse(new FileReader("C:\\Users\\CHU DANG PHU\\IdeaProjects\\untitled2\\src\\invoices.json"));
        System.out.println(statement(plays, invoices));
    }
}
