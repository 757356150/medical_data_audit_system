package DAO;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class Process {

    //读取《处方集》文本
    public static ArrayList read() throws IOException {
        String path="D:\\graphdata\\Set.txt";
        BufferedReader b= null;
        b= new BufferedReader(new FileReader(path));
        ArrayList<String> lines=new ArrayList<String>();
        for(String line=b.readLine();line!=null;line=b.readLine()){
            if (line.equals("")){

            }else lines.add(line);
        }
        return lines;
    }

    //处方集文本归类并存入mysql
    public static void CFtoMySQL() throws Exception {
        int id=1;
        ArrayList<String> testcase=read();
        ArrayList<ArrayList<String>> resultlist=new ArrayList<>();
        for(int i=0;i<testcase.size();i++){
            if(testcase.get(i).length()>3){
                if (testcase.get(i).substring(0, 2).compareTo("【适") == 0) {
                    ArrayList<String> medcine = new ArrayList<>();
                    for (int j = 0; j < 9; j++) {
                        medcine.add("");
                    }
                    medcine.set(0,String.valueOf(id));
                    id=id+1;
                    medcine.set(1, testcase.get(i - 1));
                    medcine.set(3, testcase.get(i));
                    for (int j = 1; j < 12; j++) {
                        if(testcase.get(i+j).length()>3) {
                            if (testcase.get(i + j).substring(0, 2).compareTo("【注") == 0)
                                medcine.set(4, testcase.get(i + j));
                            if (testcase.get(i + j).substring(0, 2).compareTo("【禁") == 0)
                                medcine.set(5, testcase.get(i + j));
                            if (testcase.get(i + j).substring(0, 2).compareTo("【不") == 0)
                                medcine.set(6, testcase.get(i + j));
                            if (testcase.get(i + j).substring(0, 2).compareTo("【用") == 0)
                                medcine.set(7, testcase.get(i + j));
                            if (testcase.get(i + j).substring(0, 2).compareTo("【制") == 0)
                                medcine.set(8, testcase.get(i + j));
                        }
                    }
                    //System.out.println(medcine);
                    resultlist.add(medcine);
                }
            }
        }
        //System.out.println(resultlist.size());
        for(int i=0;i<resultlist.size();i++) {
            String a=resultlist.get(i).get(1);
            String [] arr = a.split("\\s+");
            resultlist.get(i).set(1,arr[0]);
            if(arr.length!=1) {
                resultlist.get(i).set(2, arr[1]);
            }
        }
        for(int i=0;i<resultlist.size();i++){
            if(!resultlist.get(i).get(3).isEmpty()&&resultlist.get(i).get(3).length()>5)resultlist.get(i).set(3,resultlist.get(i).get(3).substring(5,resultlist.get(i).get(3).length()));
            if(!resultlist.get(i).get(4).isEmpty()&&resultlist.get(i).get(4).length()>6)resultlist.get(i).set(4,resultlist.get(i).get(4).substring(6,resultlist.get(i).get(4).length()));
            if(!resultlist.get(i).get(5).isEmpty()&&resultlist.get(i).get(5).length()>5)resultlist.get(i).set(5,resultlist.get(i).get(5).substring(5,resultlist.get(i).get(5).length()));
            if(!resultlist.get(i).get(6).isEmpty()&&resultlist.get(i).get(6).length()>6)resultlist.get(i).set(6,resultlist.get(i).get(6).substring(6,resultlist.get(i).get(6).length()));
            if(!resultlist.get(i).get(7).isEmpty()&&resultlist.get(i).get(7).length()>7)resultlist.get(i).set(7,resultlist.get(i).get(7).substring(7,resultlist.get(i).get(7).length()));
            if(!resultlist.get(i).get(8).isEmpty()&&resultlist.get(i).get(8).length()>7)resultlist.get(i).set(8,resultlist.get(i).get(8).substring(7,resultlist.get(i).get(8).length()));
            System.out.println(i+"done");
        }

        CollectDAO dao=new CollectDAO();
        int n1=dao.createMSTable();
        for(int i=0;i<resultlist.size();i++) {
            int n2 = dao.addMSData(resultlist,i);
        }
    }

    //适应症分词，加工，筛选
    public static void symProcess(String cn,String en,String sym)throws Exception{
        List<Term> med= HanLP.segment(sym);
        ArrayList<String> medList=new ArrayList<String>();
        medList.add(cn);
        medList.add(en);
        for(int i=0;i<med.size();i++){
            if(med.get(i).nature.equals(Nature.n)||med.get(i).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(i).nature.equals(Nature.ns)||med.get(i).nature.equals(Nature.nsf)||med.get(i).nature.equals(Nature.nt)||med.get(i).nature.equals(Nature.ntc)||med.get(i).nature.equals(Nature.ntcf)||med.get(i).nature.equals(Nature.ntcb)||med.get(i).nature.equals(Nature.ntch)||med.get(i).nature.equals(Nature.nto)||med.get(i).nature.equals(Nature.ntu)||med.get(i).nature.equals(Nature.nts)||med.get(i).nature.equals(Nature.nth)||med.get(i).nature.equals(Nature.nh)||med.get(i).nature.equals(Nature.nhm)||med.get(i).nature.equals(Nature.nhd)||med.get(i).nature.equals(Nature.nn)||med.get(i).nature.equals(Nature.nnt)||med.get(i).nature.equals(Nature.nnd)||med.get(i).nature.equals(Nature.Ng)||med.get(i).nature.equals(Nature.nf)||med.get(i).nature.equals(Nature.ni)||med.get(i).nature.equals(Nature.nit)||med.get(i).nature.equals(Nature.nic)||med.get(i).nature.equals(Nature.nis)||med.get(i).nature.equals(Nature.nm)||med.get(i).nature.equals(Nature.nmc)||med.get(i).nature.equals(Nature.nb)||med.get(i).nature.equals(Nature.nba)||med.get(i).nature.equals(Nature.nbc)||med.get(i).nature.equals(Nature.nbp)||med.get(i).nature.equals(Nature.nz)||med.get(i).nature.equals(Nature.d)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vi)){
                String word=med.get(i).word;
                for(int j=i+1;j<med.size();j++){
                    if(med.get(j).nature.equals(Nature.n)||med.get(j).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(j).nature.equals(Nature.ns)||med.get(j).nature.equals(Nature.nsf)||med.get(j).nature.equals(Nature.nt)||med.get(j).nature.equals(Nature.ntc)||med.get(j).nature.equals(Nature.ntcf)||med.get(j).nature.equals(Nature.ntcb)||med.get(j).nature.equals(Nature.ntch)||med.get(j).nature.equals(Nature.nto)||med.get(j).nature.equals(Nature.ntu)||med.get(j).nature.equals(Nature.nts)||med.get(j).nature.equals(Nature.nth)||med.get(j).nature.equals(Nature.nh)||med.get(j).nature.equals(Nature.nhm)||med.get(j).nature.equals(Nature.nhd)||med.get(j).nature.equals(Nature.nn)||med.get(j).nature.equals(Nature.nnt)||med.get(j).nature.equals(Nature.nnd)||med.get(j).nature.equals(Nature.Ng)||med.get(j).nature.equals(Nature.nf)||med.get(j).nature.equals(Nature.ni)||med.get(j).nature.equals(Nature.nit)||med.get(j).nature.equals(Nature.nic)||med.get(j).nature.equals(Nature.nis)||med.get(j).nature.equals(Nature.nm)||med.get(j).nature.equals(Nature.nmc)||med.get(j).nature.equals(Nature.nb)||med.get(j).nature.equals(Nature.nba)||med.get(j).nature.equals(Nature.nbc)||med.get(j).nature.equals(Nature.nbp)||med.get(j).nature.equals(Nature.nz)||med.get(j).nature.equals(Nature.v)||med.get(j).nature.equals(Nature.l)||med.get(j).nature.equals(Nature.a)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vg)) {
                        word=word+med.get(j).word;
                    }else {
                        i=j;
                        break;
                    }
                }
                medList.add(word);
            }
        }



        //入知识图谱
        try (GraphDAO Gdao = new GraphDAO()) {
            Gdao.createMedNode(medList.get(0), medList.get(1));
            for (int j = 2; j < medList.size(); j++) {
                    Gdao.createSymNode(medList.get(j));
                    Gdao.createSymRelation(medList.get(0), medList.get(j));
            }
        }
    }

    //禁忌症分词，加工，筛选
    public static void fbdProcess(String cn,String en,String sym)throws Exception{
        List<Term> med= HanLP.segment(sym);
        ArrayList<String> medList=new ArrayList<String>();
        medList.add(cn);
        medList.add(en);
        for(int i=0;i<med.size();i++){
            if(med.get(i).nature.equals(Nature.n)||med.get(i).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(i).nature.equals(Nature.ns)||med.get(i).nature.equals(Nature.nsf)||med.get(i).nature.equals(Nature.nt)||med.get(i).nature.equals(Nature.ntc)||med.get(i).nature.equals(Nature.ntcf)||med.get(i).nature.equals(Nature.ntcb)||med.get(i).nature.equals(Nature.ntch)||med.get(i).nature.equals(Nature.nto)||med.get(i).nature.equals(Nature.ntu)||med.get(i).nature.equals(Nature.nts)||med.get(i).nature.equals(Nature.nth)||med.get(i).nature.equals(Nature.nh)||med.get(i).nature.equals(Nature.nhm)||med.get(i).nature.equals(Nature.nhd)||med.get(i).nature.equals(Nature.nn)||med.get(i).nature.equals(Nature.nnt)||med.get(i).nature.equals(Nature.nnd)||med.get(i).nature.equals(Nature.Ng)||med.get(i).nature.equals(Nature.nf)||med.get(i).nature.equals(Nature.ni)||med.get(i).nature.equals(Nature.nit)||med.get(i).nature.equals(Nature.nic)||med.get(i).nature.equals(Nature.nis)||med.get(i).nature.equals(Nature.nm)||med.get(i).nature.equals(Nature.nmc)||med.get(i).nature.equals(Nature.nb)||med.get(i).nature.equals(Nature.nba)||med.get(i).nature.equals(Nature.nbc)||med.get(i).nature.equals(Nature.nbp)||med.get(i).nature.equals(Nature.nz)||med.get(i).nature.equals(Nature.d)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vi)){
                String word=med.get(i).word;
                for(int j=i+1;j<med.size();j++){
                    if(med.get(j).nature.equals(Nature.n)||med.get(j).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(j).nature.equals(Nature.ns)||med.get(j).nature.equals(Nature.nsf)||med.get(j).nature.equals(Nature.nt)||med.get(j).nature.equals(Nature.ntc)||med.get(j).nature.equals(Nature.ntcf)||med.get(j).nature.equals(Nature.ntcb)||med.get(j).nature.equals(Nature.ntch)||med.get(j).nature.equals(Nature.nto)||med.get(j).nature.equals(Nature.ntu)||med.get(j).nature.equals(Nature.nts)||med.get(j).nature.equals(Nature.nth)||med.get(j).nature.equals(Nature.nh)||med.get(j).nature.equals(Nature.nhm)||med.get(j).nature.equals(Nature.nhd)||med.get(j).nature.equals(Nature.nn)||med.get(j).nature.equals(Nature.nnt)||med.get(j).nature.equals(Nature.nnd)||med.get(j).nature.equals(Nature.Ng)||med.get(j).nature.equals(Nature.nf)||med.get(j).nature.equals(Nature.ni)||med.get(j).nature.equals(Nature.nit)||med.get(j).nature.equals(Nature.nic)||med.get(j).nature.equals(Nature.nis)||med.get(j).nature.equals(Nature.nm)||med.get(j).nature.equals(Nature.nmc)||med.get(j).nature.equals(Nature.nb)||med.get(j).nature.equals(Nature.nba)||med.get(j).nature.equals(Nature.nbc)||med.get(j).nature.equals(Nature.nbp)||med.get(j).nature.equals(Nature.nz)||med.get(j).nature.equals(Nature.v)||med.get(j).nature.equals(Nature.l)||med.get(j).nature.equals(Nature.a)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vg)) {
                        word=word+med.get(j).word;
                    }else {
                        i=j;
                        break;
                    }
                }
                medList.add(word);
            }
        }



        //入知识图谱
        try (GraphDAO Gdao = new GraphDAO()) {
            Gdao.createMedNode(medList.get(0), medList.get(1));
            for (int j = 2; j < medList.size(); j++) {
                Gdao.createFbdNode(medList.get(j));
                Gdao.createFbdRelation(medList.get(0), medList.get(j));
            }
        }
    }

    /**
     * 批量处理
     *
     */
    //建立适应症表
    public static void buildSymTable(CollectDAO dao) throws Exception {
        int n1=dao.createSymTable();                //建立症状实体表
        for(int id=1;id<1159;id++) {             //将症状实体表进行分词
            Map<String, Object> medMap = dao.getSymptom(id);
            String symptom=medMap.get("symptom").toString();
            NLPTokenizer.ANALYZER.enableCustomDictionary(false);
            List<Term> med= HanLP.segment(symptom);
            ArrayList<String> medList=new ArrayList<String>();
            medList.add(String.valueOf(id));
            medList.add(medMap.get("chineseName").toString());
            medList.add(medMap.get("englishName").toString());
            for(int i=0;i<med.size();i++){
                if(med.get(i).nature.equals(Nature.n)||med.get(i).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(i).nature.equals(Nature.ns)||med.get(i).nature.equals(Nature.nsf)||med.get(i).nature.equals(Nature.nt)||med.get(i).nature.equals(Nature.ntc)||med.get(i).nature.equals(Nature.ntcf)||med.get(i).nature.equals(Nature.ntcb)||med.get(i).nature.equals(Nature.ntch)||med.get(i).nature.equals(Nature.nto)||med.get(i).nature.equals(Nature.ntu)||med.get(i).nature.equals(Nature.nts)||med.get(i).nature.equals(Nature.nth)||med.get(i).nature.equals(Nature.nh)||med.get(i).nature.equals(Nature.nhm)||med.get(i).nature.equals(Nature.nhd)||med.get(i).nature.equals(Nature.nn)||med.get(i).nature.equals(Nature.nnt)||med.get(i).nature.equals(Nature.nnd)||med.get(i).nature.equals(Nature.Ng)||med.get(i).nature.equals(Nature.nf)||med.get(i).nature.equals(Nature.ni)||med.get(i).nature.equals(Nature.nit)||med.get(i).nature.equals(Nature.nic)||med.get(i).nature.equals(Nature.nis)||med.get(i).nature.equals(Nature.nm)||med.get(i).nature.equals(Nature.nmc)||med.get(i).nature.equals(Nature.nb)||med.get(i).nature.equals(Nature.nba)||med.get(i).nature.equals(Nature.nbc)||med.get(i).nature.equals(Nature.nbp)||med.get(i).nature.equals(Nature.nz)||med.get(i).nature.equals(Nature.d)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vi)){
                    String word=med.get(i).word;
                    for(int j=i+1;j<med.size();j++){
                        if(med.get(j).nature.equals(Nature.n)||med.get(j).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(j).nature.equals(Nature.ns)||med.get(j).nature.equals(Nature.nsf)||med.get(j).nature.equals(Nature.nt)||med.get(j).nature.equals(Nature.ntc)||med.get(j).nature.equals(Nature.ntcf)||med.get(j).nature.equals(Nature.ntcb)||med.get(j).nature.equals(Nature.ntch)||med.get(j).nature.equals(Nature.nto)||med.get(j).nature.equals(Nature.ntu)||med.get(j).nature.equals(Nature.nts)||med.get(j).nature.equals(Nature.nth)||med.get(j).nature.equals(Nature.nh)||med.get(j).nature.equals(Nature.nhm)||med.get(j).nature.equals(Nature.nhd)||med.get(j).nature.equals(Nature.nn)||med.get(j).nature.equals(Nature.nnt)||med.get(j).nature.equals(Nature.nnd)||med.get(j).nature.equals(Nature.Ng)||med.get(j).nature.equals(Nature.nf)||med.get(j).nature.equals(Nature.ni)||med.get(j).nature.equals(Nature.nit)||med.get(j).nature.equals(Nature.nic)||med.get(j).nature.equals(Nature.nis)||med.get(j).nature.equals(Nature.nm)||med.get(j).nature.equals(Nature.nmc)||med.get(j).nature.equals(Nature.nb)||med.get(j).nature.equals(Nature.nba)||med.get(j).nature.equals(Nature.nbc)||med.get(j).nature.equals(Nature.nbp)||med.get(j).nature.equals(Nature.nz)||med.get(j).nature.equals(Nature.v)||med.get(j).nature.equals(Nature.l)||med.get(j).nature.equals(Nature.a)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vg)) {
                            word=word+med.get(j).word;
                        }else {
                            i=j;
                            break;
                        }
                    }
                    medList.add(word);
                }
            }
            if(medList.size()<23){
                for(int k=medList.size();k<23;k++){
                    medList.add("");
                }
            }

            int n2=dao.addSymData(medList);
            System.out.println(id+"finish");
        }
    }

    ///适应症筛选
    public static void screenSym(CollectDAO dao)throws Exception{
        int n3=dao.createNewSymTable();
        //int wrong = 0;
        //int total = 0;
        ArrayList<String> wl = keyword.createList();
        for(int id=1;id<1159;id++) {
            Map<String, Object> medMap = dao.findSymFeature(id);
            String symptom = "symptom";
            for (int i = 1; i <= 20; i++) {
                String symptomI = symptom + String.valueOf(i);
                if (medMap.get(symptomI) != null) {
                    if (medMap.get(symptomI).toString().length() < 2 || medMap.get(symptomI).toString().length() > 12) {
                        //if (medMap.get(symptomI).toString().length() != 0) wrong = wrong + 1;
                        medMap.put(symptomI, null);
                    }
                }
                for (int j = 0; j < wl.size(); j++) {
                    if (medMap.get(symptomI) != null) {
                        if (medMap.get(symptomI).toString().equals(wl.get(j))) {
                            medMap.put(symptomI, null);
                            //wrong = wrong + 1;
                        }
                    }
                }
                if(medMap.get(symptomI)!=null) {
                    for (int k = 0; k <medMap.get(symptomI).toString().length(); k++) {
                        if (medMap.get(symptomI).toString().substring(k, k + 1).equals("，") || medMap.get(symptomI).toString().substring(k, k + 1).equals("。") || medMap.get(symptomI).toString().substring(k, k + 1).equals("、")) {
                            medMap.put(symptomI, null);
                            //wrong = wrong + 1;
                            break;
                        }
                    }
                }
            }
            int nothing = 0;
            for (int i = 1; i <= 20; i++) {
                String symptomI = symptom + String.valueOf(i);
                if (medMap.get(symptomI) == null) nothing++;
            }
            //total =total+ 20 - nothing;
            int n4=dao.addSymData2(medMap);
            System.out.println(id+"done");

        }
        //System.out.println(wrong);
        //System.out.println(total);
    }

    public  static void buildFbdTable(CollectDAO dao) throws  Exception{
        int n5=dao.createFbdTable();                //建立禁忌症实体表
        for(int id=1;id<1159;id++) {             //将禁忌症实体表进行分词
            Map<String, Object> medMap = dao.getForbidden(id);
            String forbid=medMap.get("forbidden").toString();
            NLPTokenizer.ANALYZER.enableCustomDictionary(false);
            List<Term> med= HanLP.segment(forbid);
            ArrayList<String> medList=new ArrayList<String>();
            medList.add(String.valueOf(id));
            medList.add(medMap.get("chineseName").toString());
            medList.add(medMap.get("englishName").toString());
            for(int i=0;i<med.size();i++){
                if(med.get(i).nature.equals(Nature.n)||med.get(i).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(i).nature.equals(Nature.ns)||med.get(i).nature.equals(Nature.nsf)||med.get(i).nature.equals(Nature.nt)||med.get(i).nature.equals(Nature.ntc)||med.get(i).nature.equals(Nature.ntcf)||med.get(i).nature.equals(Nature.ntcb)||med.get(i).nature.equals(Nature.ntch)||med.get(i).nature.equals(Nature.nto)||med.get(i).nature.equals(Nature.ntu)||med.get(i).nature.equals(Nature.nts)||med.get(i).nature.equals(Nature.nth)||med.get(i).nature.equals(Nature.nh)||med.get(i).nature.equals(Nature.nhm)||med.get(i).nature.equals(Nature.nhd)||med.get(i).nature.equals(Nature.nn)||med.get(i).nature.equals(Nature.nnt)||med.get(i).nature.equals(Nature.nnd)||med.get(i).nature.equals(Nature.Ng)||med.get(i).nature.equals(Nature.nf)||med.get(i).nature.equals(Nature.ni)||med.get(i).nature.equals(Nature.nit)||med.get(i).nature.equals(Nature.nic)||med.get(i).nature.equals(Nature.nis)||med.get(i).nature.equals(Nature.nm)||med.get(i).nature.equals(Nature.nmc)||med.get(i).nature.equals(Nature.nb)||med.get(i).nature.equals(Nature.nba)||med.get(i).nature.equals(Nature.nbc)||med.get(i).nature.equals(Nature.nbp)||med.get(i).nature.equals(Nature.nz)||med.get(i).nature.equals(Nature.d)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vi)){
                    String word=med.get(i).word;
                    for(int j=i+1;j<med.size();j++){
                        if(med.get(j).nature.equals(Nature.n)||med.get(j).nature.equals(Nature.nr)||med.get(i).nature.equals(Nature.nrj)||med.get(i).nature.equals(Nature.nrf)||med.get(j).nature.equals(Nature.ns)||med.get(j).nature.equals(Nature.nsf)||med.get(j).nature.equals(Nature.nt)||med.get(j).nature.equals(Nature.ntc)||med.get(j).nature.equals(Nature.ntcf)||med.get(j).nature.equals(Nature.ntcb)||med.get(j).nature.equals(Nature.ntch)||med.get(j).nature.equals(Nature.nto)||med.get(j).nature.equals(Nature.ntu)||med.get(j).nature.equals(Nature.nts)||med.get(j).nature.equals(Nature.nth)||med.get(j).nature.equals(Nature.nh)||med.get(j).nature.equals(Nature.nhm)||med.get(j).nature.equals(Nature.nhd)||med.get(j).nature.equals(Nature.nn)||med.get(j).nature.equals(Nature.nnt)||med.get(j).nature.equals(Nature.nnd)||med.get(j).nature.equals(Nature.Ng)||med.get(j).nature.equals(Nature.nf)||med.get(j).nature.equals(Nature.ni)||med.get(j).nature.equals(Nature.nit)||med.get(j).nature.equals(Nature.nic)||med.get(j).nature.equals(Nature.nis)||med.get(j).nature.equals(Nature.nm)||med.get(j).nature.equals(Nature.nmc)||med.get(j).nature.equals(Nature.nb)||med.get(j).nature.equals(Nature.nba)||med.get(j).nature.equals(Nature.nbc)||med.get(j).nature.equals(Nature.nbp)||med.get(j).nature.equals(Nature.nz)||med.get(j).nature.equals(Nature.v)||med.get(j).nature.equals(Nature.l)||med.get(j).nature.equals(Nature.a)||med.get(i).nature.equals(Nature.vn)||med.get(i).nature.equals(Nature.b)||med.get(i).nature.equals(Nature.vg)) {
                            word=word+med.get(j).word;
                        }else {
                            i=j;
                            break;
                        }
                    }
                    medList.add(word);
                }
            }
            if(medList.size()<23){
                for(int k=medList.size();k<23;k++){
                    medList.add("");
                }
            }

            int n6=dao.addFbdData(medList);
            System.out.println(id+"finish");
        }
    }

    //禁忌症筛选
    public static void screenFbd(CollectDAO dao)throws  Exception{
        int n0=dao.createNewFbdTable();
        //int wrong = 0;
        //int total = 0;
        ArrayList<String> wl = keyword.createList();
        for(int id=1;id<1159;id++) {
            Map<String, Object> medMap = dao.findFbdFeature(id);
            String fbd = "forbidden";
            for (int i = 1; i <= 20; i++) {
                String fbdI = fbd + String.valueOf(i);
                if (medMap.get(fbdI) != null) {
                    if (medMap.get(fbdI).toString().length() < 2 || medMap.get(fbdI).toString().length() > 12) {
                        //if (medMap.get(fbdI).toString().length() != 0) wrong = wrong + 1;
                        medMap.put(fbdI, null);
                    }
                }
                for (int j = 0; j < wl.size(); j++) {
                    if (medMap.get(fbdI) != null) {
                        if (medMap.get(fbdI).toString().equals(wl.get(j))) {
                            medMap.put(fbdI, null);
                            //wrong = wrong + 1;
                        }
                    }
                }
                if(medMap.get(fbdI)!=null) {
                    for (int k = 0; k <medMap.get(fbdI).toString().length(); k++) {
                        if (medMap.get(fbdI).toString().substring(k, k + 1).equals("，") || medMap.get(fbdI).toString().substring(k, k + 1).equals("。") || medMap.get(fbdI).toString().substring(k, k + 1).equals("、")) {
                            medMap.put(fbdI, null);
                            //wrong = wrong + 1;
                            break;
                        }
                    }
                }
            }
            int nothing = 0;
            for (int i = 1; i <= 20; i++) {
                String fbdI = fbd + String.valueOf(i);
                if (medMap.get(fbdI) == null) nothing++;
            }
            //total =total+ 20 - nothing;
            int n9=dao.addFbdData2(medMap);
            System.out.println(id+"done");

        }
        //System.out.println(wrong);
        //System.out.println(total);
    }

    public static void symToGraph(CollectDAO dao)throws Exception{
        //适应症入知识图谱
        for(int i=1;i<1159;i++) {
            Map<String, Object> symmap = dao.findSymToGraph(i);
            try (GraphDAO Gdao = new GraphDAO()) {
                Gdao.createMedNode(symmap.get("chinese").toString(), symmap.get("english").toString());
                for (int j = 1; j <= 20; j++) {
                    String symptom = "symptom";
                    String symptomI = symptom + String.valueOf(j);
                    if (symmap.get(symptomI) != null) {
                        Gdao.createSymNode(symmap.get(symptomI).toString());
                        Gdao.createSymRelation(symmap.get("chinese").toString(), symmap.get(symptomI).toString());
                    }
                }
                System.out.println(i+"finish");
            }
        }
    }
    //禁忌症入图谱
    public static void fbdToGraph(CollectDAO dao)throws Exception{
        for(int i=1;i<1159;i++) {
            Map<String, Object> fbdmap = dao.findFbdToGraph(i);
            try (GraphDAO Gdao = new GraphDAO()) {
                Gdao.createMedNode(fbdmap.get("chinese").toString(), fbdmap.get("english").toString());
                for (int j = 1; j <= 20; j++) {
                    String fbd = "forbidden";
                    String fbdI = fbd + String.valueOf(j);
                    if (fbdmap.get(fbdI) != null) {
                        Gdao.createFbdNode(fbdmap.get(fbdI).toString());
                        Gdao.createFbdRelation(fbdmap.get("chinese").toString(), fbdmap.get(fbdI).toString());
                    }
                }
                System.out.println(i+"finish");
            }
        }
    }



}
