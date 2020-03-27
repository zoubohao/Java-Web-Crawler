
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainPubmedPage {

    public String cookie = "PRESERVTARGET=%2FtYTXpgzJne16bwfb4ZN2lGIny" +
            "YoZNk58TVbSvhIR0njSJplCp65%2BiF2SZAktvmmznDxgJBJhBCH%0ANoo" +
            "2je1cMugfM9MKK09WhGt%2B1upWcy9gvyWiyzMpHLk0QESaTZ4Bb4AHd%2" +
            "FqTyjimsQAYYo8pt4Yd%0Ay2bIXQoeJLNXZn6It0GzA9t0d%2BsKnv4JGP" +
            "f8d7L5O7Fp9ZWJIqVruQ1nEeRv8GkdO1Q1%2F9hrL8o%2F%0AJRFQ7TBLS" +
            "jkNAVNDkfD30W7Ni2qTiCcod%2BnpCPazZ51Lqej4jR18speLCsWGBd1bg" +
            "FZLKvBPgLVZ%0AytdIekSuo%2FfLpRq%2BJMYZ588uDcVUWVMukHMqP86H" +
            "f4fK06Z%2BahHImGIPMk%2FHLAgkZcspS8%2BFcBKF%0AVSpW0nory8rBg" +
            "KjdXi%2F%2FIYdAp8GTbl%2BV6eAfOnhoNxuPV6zufiIOO3M2S18Jgsn6x" +
            "atLw9guzaY9%0AzyF91SyoQC6Vt76eNYuP5CXP4RbJ1opwhwwAQkqbQQ3c" +
            "Ue3%2BFggOrTJm1tpR6NnrWrDUMSmTQ4LX%0ACALJhy8EChoSV%2BpE0Hz" +
            "buyAMd7lR%2FvbtQk7QibUhmCYa%2B4YXXmxR7MeexeUe4jqGIO8Sp98A%" +
            "2BO7R%0A8399fnO1oehydR1npJP%2BX85d9YQm7tFUrYImTF9v9U5P%2BA" +
            "kHmAmC054G4siHitRqzdsoJ%2FrOTORQ%0AfqUO38HfrWMMwCQrg3EYlQj" +
            "NpQsPkDz89409Hpaolg%3D%3D; NIHSMPROFILE=9i9xFyZxcZ3DeEBWJ1" +
            "M%2B1%2BnomqAdq8x0Uj92QgQv2oQAlXY20xNoqCTjlCDlHmC37KMF1wnR" +
            "TtlqltGuJKPhCMCnXx6Xpb2zD5VmlcajH67PaSrbzelPxWDVenhI7K1Wf6" +
            "ekIvyJhYwsfiLxlfnUzkj1K2f%2BJSVsxi5l4UNAxVW7P%2FSU7TtSpadO" +
            "of162hhNRB8RhZnTmNURpcqyqvv5YnbucGuRg2h%2BomAcjDvlq9Yjvxtl" +
            "hQbSMQ2NF0HEllgprU%2BkOE%2FyE%2BJUkfgTaJJ%2Fn1eijlq6kR0i2Bn" +
            "hWd0ePKlQn8fhMj9cKb3wsYsj0LTqqgz6gSisKtUiyQxQnl%2FZo2PCsxur4" +
            "r%2BHqhOFT7lfHpCkU%2BpPgOng0GIR0l7sL29flbUPx6s2df8ul4m821pLr" +
            "Ih89O7eYSKQAOhezacAyQ5cIB6Ugwo%2Bp4fKCHfvEyAPEs%2FsqEI1UlSj2K" +
            "RX%2FU%2Bx2oGFCnCtMXKLgdz%2FyZSJVplP8nTthc49bM38X9GLJw6CEiWcSm" +
            "%2FH94DA%2B%2BQhuTsjbuQ5x5t2VgXOX9gVnH8igDRgYeD3d1kLLpX2ul4Ydk" +
            "lFoQ80JW%2FUZLeV1zYUsDE2xQ%3D%3D; NIHSMSESSION=oygbkuLgNyjTBQ44" +
            "p0ahiNDfpGccw1SEwWTbRrnMpQ/EYtJa3ayDj35zJn9k8PrBVyhNXSWe0n9k8MxD" +
            "aE+VnnXK5ZtLtjHtXES6E+tExWqdIbl450yOLFKyAQjhr2Odf5yfMAsz4/35gkjF" +
            "IVJuvA5uDzoZStKrJ3ZGboVEGJY1Uf3FRThZOnVVoJsvUxgkOnVTX8Bxafdya6C4P" +
            "OYdyrDd9ffH7Tmg0S0BR0RtrT+8OL947P1UpA+UU/iTqBsTUo8/klpXAYwfQCjDc39" +
            "RRNKQqw584frL0SGiZ8XZBqS8pRE+ddMwjVvQ0nFXHYG5ElHqTRLb3dT+D0+oqI9k1Q" +
            "gjOW6OSPotjMt9mdoYm4+w6y0Y6e21Q+RK5umBM/VSvarakwKwpEfobW4HJid8hAF1vz" +
            "H92KQ09/LdDaNhH/eaoZphx2WMxWlQ1/pP8acCuTv4wdUCiRBqU9jUDkrIWqtHwFu1O2w" +
            "4i0KYwdwo3JhWjDCEflEUncDro5NBGYmuyF7c9tJlKWqG++eNsxCdbMb+esuy9DhB0UN9a" +
            "F3a4M02k0dakPruE1E+HPiVH/8HRf3ACXTTSjrp7CrEEq5qLko/IA7GkguLvBjcvjEnSn+a" +
            "JiixWKzngfA70yJVAvfCkjVVMI6uCjuuyUbAIV2qGU/qRZQh8OSNyC19mIgaPg6Ings79I43" +
            "YXdYTCAcQzUb33+4jjZEC6F9Sh3YidpB+Pveg6jky2lRP/1SFFwUwR30JI/VtSYrykyn7qxW" +
            "gKZyz2cfcA8jETKy9b1ZXHHZuaMQ9zviKE9w+xPCxpCnQdKLFO3VixcU9s01ghRf6AjPer4h" +
            "SSMHndFjvkRNG82D7odu33tQBcYMAug4el/dVIPyQJKKpqmfTWXdtwWAyoT8jXxpC4qNM1xku" +
            "L4aoKB4H9lSr0RyP1VX7XK7PLchbTnQkAnqsed3GrVYOvhw7S/pNxmbgJDZuzqBRIuXyXqt8Ku" +
            "4hD6raceuaylvTL1zNr7KiNIBih+ILOUNkQRrxG4Pij4GiHgfE1Y0hfviO2bkw5acVx29AqjPTSs" +
            "hldQ8FxZUXH96iq/lFre9ScTywJzZLL6+s8AC279iPBa8i4/scV/YyW3JQoee3PqmSkN10IV+wOK" +
            "2DjjJVZpLrbNfTJTFuGPif05s3/s3T0NaUvDhmHAlhQ+y5XFkS6t2xmrJouQMwe7Ptgzeq/666dX" +
            "v+fGXLgUu8QnN9QI5UWH2ymzKMTNS7jPazAX3z5y+Oy14AxtckF2SF+utpazioaFYYTSXbhsLtWB+K" +
            "BAD/Ovs3r2w/DnXhN2StEmWYonVAD01n+hNXc+7vF29hoghQBeT5KRn6dRck2E/jVdOjqdga+9zoC1" +
            "hWngC4mad; WebCubbyUser=3KLA1UEUO9PA1TSD317Z6VDXALZB3NA9%3Blogged-in%3Dtrue%3B" +
            "my-name%3Dbhzou%2540ucdavis.edu%3Bpersistent%3Dfalse%408A1B7638E3F8C641_1640SI" +
            "D; MyNcbiSigninPreferences=O2dvb2dsZSY%3D; ncbi_sid=8A1B7638E3F8C641_1640SID; _" +
            "gid=GA1.2.342353529.1585193136; _ga=GA1.2.667612504.1581223018; books.article.r" +
            "eport=; pmc.article.report=; _gat_ncbiSg=1; _gat_dap=1; ncbi_prevPHID=CE887F34E7" +
            "C2E9810000000000480043; ncbi-login-route=google; ncbi_pinger=N4IgDgTgpgbg+mAFgSwC" +
            "YgFwgBwEECMAQgOwBsAzNgKLkBi2AwqQCwCc+ArNhwEwfM8ADDwHkAygEkAIiAA0IAK4A7ADYB7AIaolUA" +
            "B4AXTKB6YQAcwVoockOVM3mpgBJqAtlAAKGs9fkd78kKmeERklDT0TGyc3HwCwqKSMoH4phZWGAByAPKZVB" +
            "gu7l4+WblUNjwmWADutQB0SgDGAEbIDSquDciIdWZqMBWspvisQfLkgsOC2JPjqVgjY7ZVIPiCzLO2dlj6EA" +
            "q+to47eweUAbZDC6TExA6TWDzk5EPyzNsggnU82F8OR4qqTTaPSGV7+LCbDjvVj8GywrDYViOPykYYCOG3CE2" +
            "UjzECsSYAXwJQA; labs-pubmed-csrftoken=8si1wRTHl3py1EoX8VI5O6K1C2l2IyWLyMGd92ej7AyIBhpv" +
            "8xgMoEMpEZBWfPLK; sessionid=6ft1ufa479bsao91lg8mskaom0gt3r4n";

    private String urlEncoder(String term) {
        String encoder = null;
        try {
            encoder = URLEncoder.encode(term, StandardCharsets.UTF_8.toString());
        }
        catch (Exception e){
            System.out.println("This error takes place in the urlEncoder function of MainPubmedPage. " +
                    "Can not encoder the query terms.");
            e.printStackTrace();
        }
        return encoder;
    }

    private List<String> parseContent(String content){
        List<String> ids = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements pmids = doc.getElementsByClass("docsum-pmid");
        for (Element pmid : pmids){
            ids.add(pmid.text());
        }
        return ids;
    }

    private String getPubmedContent (String term, int page){
        String content = null;
        String pubmed = "https://pubmed.ncbi.nlm.nih.gov/";
        String url = pubmed + "?" + "term=" + this.urlEncoder(term) + "&filter=simsearch1.fha&page=" + page;
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                .setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
                .setResponseTimeout(10, TimeUnit.SECONDS).build();
        HttpGet get = new HttpGet(url);
        get.setConfig(config);
        CloseableHttpClient client = HttpClients.createDefault();
        get.setHeader("Accept","text/html, application/xhtml+xml, application/xml; q=0.9, */*; q=0.8");
        get.setHeader("Accept-Encoding","gzip, deflate, br");
        get.setHeader("Accept-Language","zh-Hans-CN, zh-Hans; q=0.5");
        get.setHeader("Cookie",this.cookie);
        get.setHeader("Host","pubmed.ncbi.nlm.nih.gov");
        get.setHeader("Referer","https://pubmed.ncbi.nlm.nih.gov/?" +
                "term=" + this.urlEncoder(term) + "&filter=simsearch1.fha");
        get.setHeader("Upgrade-Insecure-Requests","1");
        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362");
        try{
            CloseableHttpResponse response = client.execute(get);
            content = EntityUtils.toString(response.getEntity());
            response.close();
            get.clear();
            client.close();
        }catch (Exception e){
            System.out.println("An error has taken place in getting PMID." +
                    "It is the function of getPubmedID in MainPubmedPage class.");
            e.printStackTrace();
        }
        return content;
    }

    public List<String> getPubmedIDs(String term, int page){
        String content = this.getPubmedContent(term,page);
        return this.parseContent(content);
    }

    public void getPubmedIDsUsingThreads(String term, String filePath ,int beginPage, int endPage, int threadsForWorking){
        ExecutorService fixedThreadsPool = Executors.newFixedThreadPool(threadsForWorking);
        for (int i = beginPage; i <= endPage; i++){
            int finalI = i;
            fixedThreadsPool.execute(() -> {
                System.out.println("Current Thread in " + finalI);
                MainPubmedPage currentSpider = new MainPubmedPage();
                List<String> ids = currentSpider.getPubmedIDs(term, finalI);
                for (String id : ids){
                    try {
                        FileWriter writer = new FileWriter(new File(filePath),true);
                        writer.write(id);
                        writer.write("\n");
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("An error has taken place in writing.");
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep((long)(Math.random() * 3000 + 2000));
                }catch (Exception e){
                    System.out.println("An error has taken place in thread sleep.");
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep((long)(Math.random() * 3000 + 2000));
            }catch (Exception e){
                System.out.println("An error has taken place in thread sleep.");
                e.printStackTrace();
            }
        }
        fixedThreadsPool.shutdown();
    }

    public static void main(String [] args) {
        MainPubmedPage test = new MainPubmedPage();
        int finalPage = 94453;
        String filePath = "./PMIDs.txt";
        test.getPubmedIDsUsingThreads("(mouse[MeSH Terms]) AND ((\"2000/01/01\"[Date - Publication] : \"2020/03/26\"[Date - Publication]))",
                filePath,1,50000,6);

    }

}
