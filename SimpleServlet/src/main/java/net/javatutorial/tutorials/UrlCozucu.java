package net.javatutorial.tutorials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UrlCozucu {


    public String urlToString(String url) {
        URL website = null;
        int i = 0;
        try {
            website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1 Anahtar kelime saydırma
     */
    public int urldekiKelimeleriSay(String url, String aranacakSozcuk) {
        aranacakSozcuk = aranacakSozcuk.toLowerCase();
        String html = urlToString(url);
        html = html.toLowerCase();
        int i = 0;

        Pattern p = Pattern.compile(aranacakSozcuk);
        Matcher m = p.matcher(html);
        while (m.find()) {
            i++;
        }
        return i;
    }

    /**
     * 2. URL Listele 
     */
    public List<URLObjesi> urlSiralama(List<String> urller, List<String> aranicakKelimeler) {
        List<URLObjesi> urlList = new ArrayList<>();
        for (String url_ : urller) {
            HashMap kelimePuan = new HashMap();
            for (String aranicakKelime : aranicakKelimeler) {
                int kelimeSayisi = 0;
                kelimeSayisi = urldekiKelimeleriSay(url_, aranicakKelime);
                kelimePuan.put(aranicakKelime.trim(), kelimeSayisi);
            }
            URLObjesi url = new URLObjesi(url_.trim(), kelimePuan);
            url.puaniniHesapla();
            urlList.add(url);
        }
        /**
         * puanları olan bir listemiz var bunları puanlarına göre testen sıralar
         */
        Collections.sort(urlList, new Comparator<URLObjesi>() {
            @Override
            public int compare(URLObjesi o1, URLObjesi o2) {
                return ((Double) o2.getPuan()).compareTo(o1.getPuan());
            }
        });
        return urlList;
    }

    /**
     * Url içindeki url leri döner
     */
    public List<String> altUrller(String url) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?):(//)+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(url);

        while (urlMatcher.find()) {
            containedUrls.add(url.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    /**
     * 3 Site Sıralama
     */
    public List<URLObjesi> siteSiralama(List<String> urller, List<String> aranicakKelimeler) {
        List<URLObjesi> urlList = new ArrayList<>();
        for (String url_ : urller) { //1. derinlikteki URL'ler
            HashMap kelimeBazliPuan = new HashMap();
            for (String aranicakKelime : aranicakKelimeler) {
                int kelimeSayisi = 0;
                kelimeSayisi = urldekiKelimeleriSay(url_.trim(), aranicakKelime);
                kelimeBazliPuan.put(aranicakKelime.trim(), kelimeSayisi);
            }
            URLObjesi url = new URLObjesi(url_, kelimeBazliPuan, 1);
            url.puaniniSiralamayaGoreHesapla();
            urlList.add(url);

            List<String> altUrller = altUrller(url_);
            for (String altUrl : altUrller) { //2. derinlikteki URL'ler
                kelimeBazliPuan = new HashMap();
                for (String aranicakKelime : aranicakKelimeler) {
                    int kelimeSayisi = 0;
                    kelimeSayisi = urldekiKelimeleriSay(altUrl, aranicakKelime);
                    kelimeBazliPuan.put(aranicakKelime, kelimeSayisi);
                }
                url = new URLObjesi(altUrl, kelimeBazliPuan, 2);
                url.puaniniSiralamayaGoreHesapla();
                urlList.add(url);
                /*
                 * 3. derinlik alt url'in altındaki url'ler
                 */
                List<String> altUrlAltindakiUrller = altUrller(altUrl);
                urlList.addAll(altUrldekiUrller(altUrlAltindakiUrller, aranicakKelimeler, 3));
            }
        }
        /**
         * puanları olan bir listemiz var bunları puanlarına göre testen sıralar
         */
        Collections.sort(urlList, new Comparator<URLObjesi>() {
            @Override
            public int compare(URLObjesi o1, URLObjesi o2) {
                return ((Double) o2.getPuan()).compareTo(o1.getPuan());
            }
        });
        return urlList;
    }

    public List<URLObjesi> altUrldekiUrller(List<String> urller, List<String> aranicakKelimeler, int derinlik) {
        List<URLObjesi> urlList = new ArrayList<>();
        for (String url_ : urller) {
            HashMap kelimeBazliPuan = new HashMap();
            for (String aranicakKelime : aranicakKelimeler) {
                int kelimeSayisi = 0;
                kelimeSayisi = urldekiKelimeleriSay(url_.trim(), aranicakKelime.trim());
                kelimeBazliPuan.put(aranicakKelime.trim(), kelimeSayisi);
            }
            URLObjesi url = new URLObjesi(url_, kelimeBazliPuan, derinlik);
            url.puaniniSiralamayaGoreHesapla();
            urlList.add(url);
        }
        return urlList;
    }

    /**
     * 4. aşama
     */
    public List<URLObjesi> semantikAnaliz(List<String> urller, List<String> aranicakKelimeler) {
        List<URLObjesi> urlList = new ArrayList<>();
        List<TDK> tumKelimeler = esAnlamliKelimeleriEkle(aranicakKelimeler);
        for (String url : urller) {
            HashMap kelimeBazliPuan = new HashMap();
            for (TDK tdk : tumKelimeler) {
                int kelimeSayisi = 0;
                kelimeSayisi = urldekiKelimeleriSay(url.trim(), tdk.name());
                /**
                 * eş anlamlılarıda sayalım
                 */
                for (String kelime : tdk.esAnlamlar()) {
                    kelimeSayisi += urldekiKelimeleriSay(url.trim(), tdk.name());
                }
                kelimeBazliPuan.put(tdk, kelimeSayisi);
            }
            URLObjesi urlObjesi = new URLObjesi(url, kelimeBazliPuan, true);
            urlObjesi.puaniniSiralamayaGoreHesapla();
            urlList.add(urlObjesi);
        }
        return urlList;
    }

    private List<TDK> esAnlamliKelimeleriEkle(List<String> aranicakKelimeler) {
        List<TDK> esAnlamlilar = new ArrayList<>();
        for (String aranicakKelime : aranicakKelimeler) {
            TDK tdk = esanlamliEnumuBul(aranicakKelime.trim());
            if (tdk != null) {
                esAnlamlilar.add(tdk);
            }
        }
        return esAnlamlilar;
    }

    private TDK esanlamliEnumuBul(String kelime) {
        for (TDK tdk : TDK.values()) {
            if (tdk.name().equals(kelime.trim())) {
                return tdk;
            }
        }
        return null;
    }

    public static void main(String[] args) {
       
    }
}
