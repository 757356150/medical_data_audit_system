package DAO;



import javax.naming.event.ObjectChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Audit {

    public static boolean similarity(String preparation,String disease){                        //计算二者文本相似度
        for(int i=0;i<disease.length();i++){
            if (disease.substring(i,i+1).equals("(")||disease.substring(i,i+1).equals("[")) {
                disease=disease.substring(0,i);
                break;
            }
        }
        if(disease.length()>preparation.length()&&preparation.length()!=0){
            for(int i=0;i<=disease.length()-preparation.length();i++){
                if(disease.substring(i,i+preparation.length()).equals(preparation)) return true;
                else continue;
            }
        }else{
            for(int i=0;i<=preparation.length()-disease.length();i++){
                if(preparation.substring(i,i+disease.length()).equals(disease)) return true;
                else continue;
            }
        }

        return false;
    }

    public static boolean checkSym(ArrayList<String> symList,String symptomInData){                                 //检查适应症是否冲突
        for(int i=0;i<symList.size();i++){
            String symInGraph=symList.get(i);
            int count=0;
            for(int j=0;j<symInGraph.length();j++){
                for(int k=0;k<symptomInData.length();k++){
                    if(symInGraph.substring(j,j+1).equals(symptomInData.substring(k,k+1))) count++;
                }
            }
            float rate;
            if(symInGraph.length()>symptomInData.length()) {
                rate=(float)count/symInGraph.length();
            }else {
                rate=(float)count/symptomInData.length();
            }
            if(rate>0.2) return true;
        }
        return false;
    }

    public static boolean checkAge(ArrayList<String> fbdList,int age){                              //检查年龄是否冲突
        String ageGroup="";
        if(age>55)ageGroup="old";
        else if(age<12)ageGroup="child";
        for(int i=0;i<fbdList.size();i++){
            for(int k=0;k<fbdList.get(i).length();k++) {
                if (fbdList.get(i).substring(k,k+1).equals("老")&&ageGroup.equals("old"))  return false;
                else if(fbdList.get(i).substring(k,k+1).equals("童")&&ageGroup.equals("child")) return false;
            }
        }
        return true;
    }

    public static boolean checkFbd(ArrayList<String> fbdList,String special){                               //检查禁忌症是否冲突
        for(int i=0;i<fbdList.size();i++){
            String fbdInGraph=fbdList.get(i);
            int count=0;
            for(int j=0;j<fbdInGraph.length();j++){
                for(int k=0;k<special.length();k++){
                    if(fbdInGraph.substring(j,j+1).equals(special.substring(k,k+1))) count++;
                }
            }
            float rate;
            if(fbdInGraph.length()>special.length()) {
                rate=(float)count/fbdInGraph.length();
            }else rate=(float)count/special.length();

            if(rate>0.2) return false;
        }
        return true;
    }

    public static String findMed(String medNameInData) throws Exception {                   //从《处方集》中寻找药品
        String medCF="null";
        for(int j=1;j<1159;j++){
            CollectDAO dao=new CollectDAO();
            Map<String,Object> medicine=dao.getMedDescription(j);
            String pre=medicine.get("name").toString();
            if(similarity(pre,medNameInData)) {
                medCF=pre;
                break;
            }
        }
        return medCF;
    }

    public static ArrayList<Map<String, Object>> batchAudit(String id) throws Exception {           //批量审核
        CollectDAO dao=new CollectDAO();
        ArrayList<Map<String,Object>> resultList=new ArrayList<>();
        ArrayList<Map<String,Object>> medList=dao.getMedicalData(id);    //医疗数据中用药集
        System.out.println(medList);
        if(medList.size()!=0) {
            //for(int i=0;i<medList.size();i++){
            for(int i = 0;i<medList.size();i++) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("id", id);
                resultMap.put("result", null);
                resultMap.put("explanation", null);
                //if(medList.get(i).get("med").equals("氯化钠注射液")) continue;
                //else {
                String disease = medList.get(i).get("disease").toString();
                String medData = medList.get(i).get("med").toString();
                resultMap.put("med", medData);
                int age = Integer.valueOf(medList.get(i).get("Age").toString());
                String medChuFang = findMed(medData);             //找到医疗数据中所开药品对应《处方集》中药品类
                if (medChuFang.equals("null")) {
                    resultMap.put("result", "可疑");
                    resultMap.put("explanation", "查无此药");
                } else {
                    try (GraphDAO Gdao = new GraphDAO()) {
                        ArrayList<String> symList = Gdao.SearchSym(medChuFang);
                        ArrayList<String> fbdList = Gdao.SearchFbd(medChuFang);
                        boolean checksym = checkSym(symList, disease);
                        if (checksym) {
                            boolean checkage = checkAge(fbdList, age);
                            if (checkage) {
                                resultMap.put("result", "合理");
                                resultMap.put("explanation", "无");

                            } else {
                                resultMap.put("result", "可疑");
                                resultMap.put("explanation", "禁忌症年龄范围与药品不符合；" + "病人年龄：" + age + "药品对应禁忌症" + fbdList);
                            }
                        } else {
                            resultMap.put("result", "可疑");
                            resultMap.put("explanation", "适应症与药品不符合；" + "病人病症：" + disease + "；药品对应适应症：" + symList);

                        }
                    }
                    resultList.add(resultMap);
                }
            }
        }else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", id);
            resultMap.put("med", null);
            resultMap.put("explanation", null);
            resultMap.put("result","病人未开西药");
        }

        return resultList;
    }

    public static Map<String, Object> singleAudit(Map<String,Object> profile) throws Exception {            //单个病人审核

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", profile.get("id"));
        resultMap.put("result", null);
        resultMap.put("explanation", null);
        String disease = profile.get("symptom").toString();
        String medData = profile.get("med").toString();
        resultMap.put("med", medData);
        int age = Integer.parseInt(profile.get("Age").toString());
        String medChuFang = findMed(medData);             //找到医疗数据中所开药品对应《处方集》中药品类
        if (medChuFang.equals("null")) {
            resultMap.put("result", "可疑");
            resultMap.put("explanation", "查无此药");
        } else {
            try (GraphDAO Gdao = new GraphDAO()) {
                ArrayList<String> symList = Gdao.SearchSym(medChuFang);
                ArrayList<String> fbdList = Gdao.SearchFbd(medChuFang);
                boolean checksym = checkSym(symList, disease);
                System.out.println(checksym);
                if (checksym) {
                    boolean checkage = checkAge(fbdList, age);
                    if (checkage) {
                        boolean checkfbd=checkFbd(fbdList,profile.get("special").toString());
                        if(checkfbd){
                            resultMap.put("result", "合理");
                            resultMap.put("explanation", "无");
                        }else {
                            resultMap.put("result","可疑");
                            resultMap.put("explanation", "病人特征与药品禁忌症不符合；" + "病人特征：" + profile.get("special") + "；药品对应禁忌症" + fbdList);
                        }

                    } else {
                        resultMap.put("result", "可疑");
                        resultMap.put("explanation", "禁忌症年龄范围与药品不符合；" + "病人年龄：" + age + "；药品对应禁忌症" + fbdList);
                    }
                } else {
                    resultMap.put("result", "可疑");
                    resultMap.put("explanation", "适应症与药品不符合；" + "病人病症：" + disease + "；药品对应适应症：" + symList);
                }
            }

        }
        return resultMap;
    }



    public static Map<String,Object>  makeProfile(String id,String sym,String special,String med,String age){       //制作病人档案
        Map<String,Object> profile=new HashMap<>();
        profile.put("id",id);
        profile.put("symptom",sym);
        profile.put("special",special);
        profile.put("med",med);
        profile.put("Age",age);
        return profile;
    }

    /*public static void main( String[] args ) throws Exception {

        Map<String,Object> profile=makeProfile();
        System.out.println(profile);
        Map<String,Object> result=singleAudit(profile);
        System.out.println(result);

    }*/
}
