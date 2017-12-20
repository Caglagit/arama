package net.javatutorial.tutorials;

import java.util.HashMap;
import java.util.Map;

public class URLObjesi {
    private String url;
    private double puan;
    private HashMap<String, Integer> kelimePuanlari = new HashMap<>();
    private HashMap<Enum, Integer> esanlamliKelimelerlePuanlari = new HashMap<>();
    private int derinlik;

    public URLObjesi(String url, HashMap<String, Integer> kelimePuanlari) {
        this.derinlik = 1;
        this.url = url;
        this.kelimePuanlari = kelimePuanlari;
    }

    public URLObjesi(String url, HashMap<Enum, Integer> esanlamliKelimelerlePuanlari, boolean esAnlamlilar) {
        this.url = url;
        this.esanlamliKelimelerlePuanlari = esanlamliKelimelerlePuanlari;
        this.derinlik = 1;
    }

    public URLObjesi(String url, HashMap<String, Integer> kelimePuanlari, int derinlik) {
        this.url = url;
        this.kelimePuanlari = kelimePuanlari;
        this.derinlik = derinlik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPuan() {
        return puan;
    }

    public void setPuan(double puan) {
        this.puan = puan;
    }

    public HashMap<String, Integer> getKelimeBazliPuanlari() {
        return kelimePuanlari;
    }

    public void setKelimePuanlari(HashMap<String, Integer> kelimePuanlari) {
        this.kelimePuanlari = kelimePuanlari;
    }

    public void puaniniHesapla() {
        this.puan = 1;
        for (Integer puan : kelimePuanlari.values()) {
        	 if(this.puan==0) {
             	this.puan++;
             }
            this.puan *= puan;
            
        }
        this.puan = (double) this.puan;
    }

    public void puaniniSiralamayaGoreHesapla() {
        this.puan = 1;
        for (Integer puan : kelimePuanlari.values()) {
            this.puan += (puan * derinlik);
        }
    }

    public String yazdir(){
        String s= url;

        s += " puan: "+this.puan +" [ ";

        for (Map.Entry<String, Integer> stringIntegerEntry : kelimePuanlari.entrySet()) {
            s+= stringIntegerEntry.getKey()+": "+stringIntegerEntry.getValue()+"\t";
        }
        s+= "]";
        return s;
    }
}
