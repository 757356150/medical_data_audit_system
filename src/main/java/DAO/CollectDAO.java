package DAO;

import org.neo4j.register.Register;

import java.sql.*;
import java.util.*;

public class CollectDAO {
    private static Connection conn;

    public CollectDAO() throws SQLException {
        try {
            conn = DBHelper.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void closeconn() throws SQLException {
        if(conn!= null)
            conn.close();
    }

    //构建药品-属性表
    public int createMSTable() throws Exception{
        String sql = "CREATE TABLE IF NOT EXISTS `MedicineSymptom`(\n"
            +"`id` INT,\n"
            +"`chineseName` VARCHAR(2000),\n"
            +"`EnglishName` VARCHAR(2000),\n"
            +"`symptom` VARCHAR(2000),\n"
            +"`attention` VARCHAR(2000),\n"
            +"`Contraindication` VARCHAR(2000),\n"
            +"`SideEffects` VARCHAR(2000),\n"
            +"`administration` VARCHAR(2000),\n"
            +"`Preparation` VARCHAR(2000)\n"
            +")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        PreparedStatement pst = conn.prepareStatement(sql);
        int n = pst.executeUpdate();
        return  n;
    }


    //将从处方集中读取的药品文本存入表中
    public  int addMSData(ArrayList<ArrayList<String>> resultlist,int j) throws Exception{      //将从处方集中读取的药品文本存入表中
        String sql = "insert into MedicineSymptom values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        for(int i=0;i<9;i++) {
            pst.setObject(i+1, resultlist.get(j).get(i), Types.VARCHAR);
        }
        int n = pst.executeUpdate();
        return  n;
    }


    // 创建适应症知识存储表
    public int createSymTable() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS `SymptomList`(\n"
            + "`id` INT,\n"
            + "`chineseName` VARCHAR(500),\n"
            + "`EnglishName` VARCHAR(500),\n"
            + "`symptom1` VARCHAR(500),\n"
            + "`symptom2` VARCHAR(500),\n"
            + "`symptom3` VARCHAR(500),\n"
            + "`symptom4` VARCHAR(500),\n"
            + "`symptom5` VARCHAR(500),\n"
            + "`symptom6` VARCHAR(500),\n"
            + "`symptom7` VARCHAR(500),\n"
            + "`symptom8` VARCHAR(500),\n"
            + "`symptom9` VARCHAR(500),\n"
            + "`symptom10` VARCHAR(500),\n"
            + "`symptom11` VARCHAR(500),\n"
            + "`symptom12` VARCHAR(500),\n"
            + "`symptom13` VARCHAR(500),\n"
            + "`symptom14` VARCHAR(500),\n"
            + "`symptom15` VARCHAR(500),\n"
            + "`symptom16` VARCHAR(500),\n"
            + "`symptom17` VARCHAR(500),\n"
            + "`symptom18` VARCHAR(500),\n"
            + "`symptom19` VARCHAR(500),\n"
            + "`symptom20` VARCHAR(500)\n"
            +")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        PreparedStatement pst = conn.prepareStatement(sql);
        int n = pst.executeUpdate();
        return n;
    }


    //从药品表中获取适应症文本进行分词
    public Map<String, Object> getSymptom(int medId) throws Exception {
        String sql = "select md.id, md.chineseName, md.englishName, md.symptom\n"
            + " from MedicineSymptom md  where md.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst = conn.prepareStatement(sb.toString());
        pst.setInt(1, medId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("medId", rs.getString("id"));
            map.put("chineseName", rs.getString("chineseName"));
            map.put("englishName",rs.getString("englishName"));
            if (rs.getString("symptom") != null) map.put("symptom", rs.getString("symptom"));
            return map;
        }
        else return null;
    }
    //分词结果入表
    public int addSymData(ArrayList<String> resultlist) throws Exception {
        String sql = "insert into SymptomList (id,chineseName,EnglishName,symptom1,symptom2,symptom3,symptom4,symptom5,symptom6,symptom7,symptom8,symptom9,symptom10,symptom11,symptom12,symptom13,symptom14,symptom15,symptom16,symptom17,symptom18,symptom19,symptom20) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setObject(1,Integer.valueOf(resultlist.get(0)));
        for (int i = 1; i < 23; i++) {
            pst.setObject(i + 1, resultlist.get(i));
        }
        int n = pst.executeUpdate();
        return n;
    }

    //获取分词后的适应症
    public Map<String,Object> findSymFeature (int id)throws Exception{
        Connection conn= DBHelper.getConnection();
        String sql = "select sl.id, sl.chineseName, sl.englishName, sl.symptom1, sl.symptom2, sl.symptom3, sl.symptom4, sl.symptom5, sl.symptom6, sl.symptom7, sl.symptom8, sl.symptom9, sl.symptom10, sl.symptom11, sl.symptom12, sl.symptom13, sl.symptom14, sl.symptom15, sl.symptom16, sl.symptom17, sl.symptom18, sl.symptom19, sl.symptom20\n"
            + " from symptomlist sl  where sl.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();
        List<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
        if (rs.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs.getInt("id"));
            map.put("chinese", rs.getString("chineseName"));
            map.put("english", rs.getString("englishName"));
            if (rs.getString("symptom1") != null) map.put("symptom1", rs.getString("symptom1"));
            if (rs.getString("symptom2") != null) map.put("symptom2", rs.getString("symptom2"));
            if (rs.getString("symptom3") != null) map.put("symptom3", rs.getString("symptom3"));
            if (rs.getString("symptom4") != null) map.put("symptom4", rs.getString("symptom4"));
            if (rs.getString("symptom5") != null) map.put("symptom5", rs.getString("symptom5"));
            if (rs.getString("symptom6") != null) map.put("symptom6", rs.getString("symptom6"));
            if (rs.getString("symptom7") != null) map.put("symptom7", rs.getString("symptom7"));
            if (rs.getString("symptom8") != null) map.put("symptom8", rs.getString("symptom8"));
            if (rs.getString("symptom9") != null) map.put("symptom9", rs.getString("symptom9"));
            if (rs.getString("symptom10") != null) map.put("symptom10", rs.getString("symptom10"));
            if (rs.getString("symptom11") != null) map.put("symptom11", rs.getString("symptom11"));
            if (rs.getString("symptom12") != null) map.put("symptom12", rs.getString("symptom12"));
            if (rs.getString("symptom13") != null) map.put("symptom13", rs.getString("symptom13"));
            if (rs.getString("symptom14") != null) map.put("symptom14", rs.getString("symptom14"));
            if (rs.getString("symptom15") != null) map.put("symptom15", rs.getString("symptom15"));
            if (rs.getString("symptom16") != null) map.put("symptom16", rs.getString("symptom16"));
            if (rs.getString("symptom17") != null) map.put("symptom17", rs.getString("symptom17"));
            if (rs.getString("symptom18") != null) map.put("symptom18", rs.getString("symptom18"));
            if (rs.getString("symptom19") != null) map.put("symptom19", rs.getString("symptom19"));
            if (rs.getString("symptom20") != null) map.put("symptom20", rs.getString("symptom20"));
            return map;
        }
        else return null;
    }

    //建立筛选结果表
    public int createNewSymTable() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS `SymptomNewList`(\n"
            + "`id` INT,\n"
            + "`chineseName` VARCHAR(500),\n"
            + "`EnglishName` VARCHAR(500),\n"
            + "`symptom1` VARCHAR(500),\n"
            + "`symptom2` VARCHAR(500),\n"
            + "`symptom3` VARCHAR(500),\n"
            + "`symptom4` VARCHAR(500),\n"
            + "`symptom5` VARCHAR(500),\n"
            + "`symptom6` VARCHAR(500),\n"
            + "`symptom7` VARCHAR(500),\n"
            + "`symptom8` VARCHAR(500),\n"
            + "`symptom9` VARCHAR(500),\n"
            + "`symptom10` VARCHAR(500),\n"
            + "`symptom11` VARCHAR(500),\n"
            + "`symptom12` VARCHAR(500),\n"
            + "`symptom13` VARCHAR(500),\n"
            + "`symptom14` VARCHAR(500),\n"
            + "`symptom15` VARCHAR(500),\n"
            + "`symptom16` VARCHAR(500),\n"
            + "`symptom17` VARCHAR(500),\n"
            + "`symptom18` VARCHAR(500),\n"
            + "`symptom19` VARCHAR(500),\n"
            + "`symptom20` VARCHAR(500)\n"
            +")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        PreparedStatement pst = conn.prepareStatement(sql);
        int n = pst.executeUpdate();
        return n;
    }

    //筛选结果入表
    public int addSymData2(Map<String,Object> medMap) throws Exception {
        String sql = "insert into SymptomNewList (id,chineseName,EnglishName,symptom1,symptom2,symptom3,symptom4,symptom5,symptom6,symptom7,symptom8,symptom9,symptom10,symptom11,symptom12,symptom13,symptom14,symptom15,symptom16,symptom17,symptom18,symptom19,symptom20) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setObject(1,medMap.get("id"));
        pst.setObject(2,medMap.get("chinese"));
        pst.setObject(3,medMap.get("english"));
        String symptom = "symptom";
        for (int i = 1; i <=20; i++) {
            String symptomI = symptom + String.valueOf(i);
            pst.setObject(i + 3, medMap.get(symptomI));
        }
        int n = pst.executeUpdate();
        return n;
    }

    //获取symptom存入知识图谱
    public Map<String,Object> findSymToGraph (int id)throws Exception{
        Connection conn= DBHelper.getConnection();
        String sql = "select sl.id, sl.chineseName, sl.englishName, sl.symptom1, sl.symptom2, sl.symptom3, sl.symptom4, sl.symptom5, sl.symptom6, sl.symptom7, sl.symptom8, sl.symptom9, sl.symptom10, sl.symptom11, sl.symptom12, sl.symptom13, sl.symptom14, sl.symptom15, sl.symptom16, sl.symptom17, sl.symptom18, sl.symptom19, sl.symptom20\n"
            + " from symptomnewlist sl  where sl.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();
        List<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
        if (rs.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs.getInt("id"));
            map.put("chinese", rs.getString("chineseName"));
            map.put("english", rs.getString("englishName"));
            if (rs.getString("symptom1") != null) map.put("symptom1", rs.getString("symptom1"));
            if (rs.getString("symptom2") != null) map.put("symptom2", rs.getString("symptom2"));
            if (rs.getString("symptom3") != null) map.put("symptom3", rs.getString("symptom3"));
            if (rs.getString("symptom4") != null) map.put("symptom4", rs.getString("symptom4"));
            if (rs.getString("symptom5") != null) map.put("symptom5", rs.getString("symptom5"));
            if (rs.getString("symptom6") != null) map.put("symptom6", rs.getString("symptom6"));
            if (rs.getString("symptom7") != null) map.put("symptom7", rs.getString("symptom7"));
            if (rs.getString("symptom8") != null) map.put("symptom8", rs.getString("symptom8"));
            if (rs.getString("symptom9") != null) map.put("symptom9", rs.getString("symptom9"));
            if (rs.getString("symptom10") != null) map.put("symptom10", rs.getString("symptom10"));
            if (rs.getString("symptom11") != null) map.put("symptom11", rs.getString("symptom11"));
            if (rs.getString("symptom12") != null) map.put("symptom12", rs.getString("symptom12"));
            if (rs.getString("symptom13") != null) map.put("symptom13", rs.getString("symptom13"));
            if (rs.getString("symptom14") != null) map.put("symptom14", rs.getString("symptom14"));
            if (rs.getString("symptom15") != null) map.put("symptom15", rs.getString("symptom15"));
            if (rs.getString("symptom16") != null) map.put("symptom16", rs.getString("symptom16"));
            if (rs.getString("symptom17") != null) map.put("symptom17", rs.getString("symptom17"));
            if (rs.getString("symptom18") != null) map.put("symptom18", rs.getString("symptom18"));
            if (rs.getString("symptom19") != null) map.put("symptom19", rs.getString("symptom19"));
            if (rs.getString("symptom20") != null) map.put("symptom20", rs.getString("symptom20"));
            return map;
        }
        else return null;
    }

    //建立禁忌症表
    public int createFbdTable() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS `ForbiddenList`(\n"
            + "`id` INT,\n"
            + "`chineseName` VARCHAR(700),\n"
            + "`EnglishName` VARCHAR(700),\n"
            + "`forbidden1` VARCHAR(700),\n"
            + "`forbidden2` VARCHAR(700),\n"
            + "`forbidden3` VARCHAR(700),\n"
            + "`forbidden4` VARCHAR(700),\n"
            + "`forbidden5` VARCHAR(700),\n"
            + "`forbidden6` VARCHAR(700),\n"
            + "`forbidden7` VARCHAR(700),\n"
            + "`forbidden8` VARCHAR(700),\n"
            + "`forbidden9` VARCHAR(700),\n"
            + "`forbidden10` VARCHAR(700),\n"
            + "`forbidden11` VARCHAR(700),\n"
            + "`forbidden12` VARCHAR(700),\n"
            + "`forbidden13` VARCHAR(700),\n"
            + "`forbidden14` VARCHAR(700),\n"
            + "`forbidden15` VARCHAR(700),\n"
            + "`forbidden16` VARCHAR(700),\n"
            + "`forbidden17` VARCHAR(700),\n"
            + "`forbidden18` VARCHAR(700),\n"
            + "`forbidden19` VARCHAR(700),\n"
            + "`forbidden20` VARCHAR(700)\n"
            +")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        PreparedStatement pst = conn.prepareStatement(sql);
        int n = pst.executeUpdate();
        return n;
    }

    //获取禁忌症进行分词
    public Map<String, Object> getForbidden(int medId) throws Exception {
        String sql = "select md.id, md.chineseName, md.englishName, md.contraindication\n"
            + " from MedicineSymptom md  where md.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst = conn.prepareStatement(sb.toString());
        pst.setInt(1, medId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("medId", rs.getString("id"));
            map.put("chineseName", rs.getString("chineseName"));
            map.put("englishName",rs.getString("englishName"));
            if (rs.getString("contraindication") != null) map.put("forbidden", rs.getString("contraindication"));
            return map;
        }
        else return null;
    }

    //分词结果入表
    public int addFbdData(ArrayList<String> resultlist) throws Exception {
        String sql = "insert into ForbiddenList (id,chineseName,EnglishName,forbidden1,forbidden2,forbidden3,forbidden4,forbidden5,forbidden6,forbidden7,forbidden8,forbidden9,forbidden10,forbidden11,forbidden12,forbidden13,forbidden14,forbidden15,forbidden16,forbidden17,forbidden18,forbidden19,forbidden20) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setObject(1,Integer.valueOf(resultlist.get(0)));
        for (int i = 1; i < 23; i++) {
            pst.setObject(i + 1, resultlist.get(i));
        }
        int n = pst.executeUpdate();
        return n;
    }

    //获取分词后的禁忌症
    public Map<String,Object> findFbdFeature (int id)throws Exception{
        Connection conn= DBHelper.getConnection();
        String sql = "select sl.id, sl.chineseName, sl.englishName, sl.forbidden1, sl.forbidden2, sl.forbidden3, sl.forbidden4, sl.forbidden5, sl.forbidden6, sl.forbidden7, sl.forbidden8, sl.forbidden9, sl.forbidden10, sl.forbidden11, sl.forbidden12, sl.forbidden13, sl.forbidden14, sl.forbidden15, sl.forbidden16, sl.forbidden17, sl.forbidden18, sl.forbidden19, sl.forbidden20\n"
            + " from forbiddenlist sl  where sl.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();
        List<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
        if (rs.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs.getInt("id"));
            map.put("chinese", rs.getString("chineseName"));
            map.put("english", rs.getString("englishName"));
            if (rs.getString("forbidden1") != null) map.put("forbidden1", rs.getString("forbidden1"));
            if (rs.getString("forbidden2") != null) map.put("forbidden2", rs.getString("forbidden2"));
            if (rs.getString("forbidden3") != null) map.put("forbidden3", rs.getString("forbidden3"));
            if (rs.getString("forbidden4") != null) map.put("forbidden4", rs.getString("forbidden4"));
            if (rs.getString("forbidden5") != null) map.put("forbidden5", rs.getString("forbidden5"));
            if (rs.getString("forbidden6") != null) map.put("forbidden6", rs.getString("forbidden6"));
            if (rs.getString("forbidden7") != null) map.put("forbidden7", rs.getString("forbidden7"));
            if (rs.getString("forbidden8") != null) map.put("forbidden8", rs.getString("forbidden8"));
            if (rs.getString("forbidden9") != null) map.put("forbidden9", rs.getString("forbidden9"));
            if (rs.getString("forbidden10") != null) map.put("forbidden10", rs.getString("forbidden10"));
            if (rs.getString("forbidden11") != null) map.put("forbidden11", rs.getString("forbidden11"));
            if (rs.getString("forbidden12") != null) map.put("forbidden12", rs.getString("forbidden12"));
            if (rs.getString("forbidden13") != null) map.put("forbidden13", rs.getString("forbidden13"));
            if (rs.getString("forbidden14") != null) map.put("forbidden14", rs.getString("forbidden14"));
            if (rs.getString("forbidden15") != null) map.put("forbidden15", rs.getString("forbidden15"));
            if (rs.getString("forbidden16") != null) map.put("forbidden16", rs.getString("forbidden16"));
            if (rs.getString("forbidden17") != null) map.put("forbidden17", rs.getString("forbidden17"));
            if (rs.getString("forbidden18") != null) map.put("forbidden18", rs.getString("forbidden18"));
            if (rs.getString("forbidden19") != null) map.put("forbidden19", rs.getString("forbidden19"));
            if (rs.getString("forbidden20") != null) map.put("forbidden20", rs.getString("forbidden20"));
            return map;
        }
        else return null;
    }

    //建立筛选结果表
    public int createNewFbdTable() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS `ForbiddenNewList`(\n"
            + "`id` INT,\n"
            + "`chineseName` VARCHAR(700),\n"
            + "`EnglishName` VARCHAR(700),\n"
            + "`forbidden1` VARCHAR(700),\n"
            + "`forbidden2` VARCHAR(700),\n"
            + "`forbidden3` VARCHAR(700),\n"
            + "`forbidden4` VARCHAR(700),\n"
            + "`forbidden5` VARCHAR(700),\n"
            + "`forbidden6` VARCHAR(700),\n"
            + "`forbidden7` VARCHAR(700),\n"
            + "`forbidden8` VARCHAR(700),\n"
            + "`forbidden9` VARCHAR(700),\n"
            + "`forbidden10` VARCHAR(700),\n"
            + "`forbidden11` VARCHAR(700),\n"
            + "`forbidden12` VARCHAR(700),\n"
            + "`forbidden13` VARCHAR(700),\n"
            + "`forbidden14` VARCHAR(700),\n"
            + "`forbidden15` VARCHAR(700),\n"
            + "`forbidden16` VARCHAR(700),\n"
            + "`forbidden17` VARCHAR(700),\n"
            + "`forbidden18` VARCHAR(700),\n"
            + "`forbidden19` VARCHAR(700),\n"
            + "`forbidden20` VARCHAR(700)\n"
            +")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        PreparedStatement pst = conn.prepareStatement(sql);
        int n = pst.executeUpdate();
        return n;
    }

    //筛选结果入表
    public int addFbdData2(Map<String,Object> medMap) throws Exception {
        String sql = "insert into ForbiddenNewList (id,chineseName,EnglishName,forbidden1,forbidden2,forbidden3,forbidden4,forbidden5,forbidden6,forbidden7,forbidden8,forbidden9,forbidden10,forbidden11,forbidden12,forbidden13,forbidden14,forbidden15,forbidden16,forbidden17,forbidden18,forbidden19,forbidden20) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setObject(1,medMap.get("id"));
        pst.setObject(2,medMap.get("chinese"));
        pst.setObject(3,medMap.get("english"));
        String fbd = "forbidden";
        for (int i = 1; i <=20; i++) {
            String fbdI = fbd + String.valueOf(i);
            pst.setObject(i + 3, medMap.get(fbdI));
        }
        int n = pst.executeUpdate();
        return n;
    }

    //获取forbidden存入知识图谱
    public Map<String,Object> findFbdToGraph (int id)throws Exception{
        Connection conn= DBHelper.getConnection();
        String sql = "select sl.id, sl.chineseName, sl.englishName, sl.forbidden1, sl.forbidden2, sl.forbidden3, sl.forbidden4, sl.forbidden5, sl.forbidden6, sl.forbidden7, sl.forbidden8, sl.forbidden9, sl.forbidden10, sl.forbidden11, sl.forbidden12, sl.forbidden13, sl.forbidden14, sl.forbidden15, sl.forbidden16, sl.forbidden17, sl.forbidden18, sl.forbidden19, sl.forbidden20\n"
            + " from forbiddenNewlist sl  where sl.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs.getInt("id"));
            map.put("chinese", rs.getString("chineseName"));
            map.put("english", rs.getString("englishName"));
            if (rs.getString("forbidden1") != null) map.put("forbidden1", rs.getString("forbidden1"));
            if (rs.getString("forbidden2") != null) map.put("forbidden2", rs.getString("forbidden2"));
            if (rs.getString("forbidden3") != null) map.put("forbidden3", rs.getString("forbidden3"));
            if (rs.getString("forbidden4") != null) map.put("forbidden4", rs.getString("forbidden4"));
            if (rs.getString("forbidden5") != null) map.put("forbidden5", rs.getString("forbidden5"));
            if (rs.getString("forbidden6") != null) map.put("forbidden6", rs.getString("forbidden6"));
            if (rs.getString("forbidden7") != null) map.put("forbidden7", rs.getString("forbidden7"));
            if (rs.getString("forbidden8") != null) map.put("forbidden8", rs.getString("forbidden8"));
            if (rs.getString("forbidden9") != null) map.put("forbidden9", rs.getString("forbidden9"));
            if (rs.getString("forbidden10") != null) map.put("forbidden10", rs.getString("forbidden10"));
            if (rs.getString("forbidden11") != null) map.put("forbidden11", rs.getString("forbidden11"));
            if (rs.getString("forbidden12") != null) map.put("forbidden12", rs.getString("forbidden12"));
            if (rs.getString("forbidden13") != null) map.put("forbidden13", rs.getString("forbidden13"));
            if (rs.getString("forbidden14") != null) map.put("forbidden14", rs.getString("forbidden14"));
            if (rs.getString("forbidden15") != null) map.put("forbidden15", rs.getString("forbidden15"));
            if (rs.getString("forbidden16") != null) map.put("forbidden16", rs.getString("forbidden16"));
            if (rs.getString("forbidden17") != null) map.put("forbidden17", rs.getString("forbidden17"));
            if (rs.getString("forbidden18") != null) map.put("forbidden18", rs.getString("forbidden18"));
            if (rs.getString("forbidden19") != null) map.put("forbidden19", rs.getString("forbidden19"));
            if (rs.getString("forbidden20") != null) map.put("forbidden20", rs.getString("forbidden20"));

            return map;
        }
        else return null;
    }


    /**
     * 以下是医疗数据的数据库操作
     */

    //取出病人id
    public ArrayList<String> getId() throws Exception {
        String sql = "select r.ClinicRegisterCode\n"
            + " from registration r  ";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> list = new ArrayList<>();
        while(rs.next()) {
            list.add(rs.getString("ClinicRegisterCode"));
        }
        return list;
    }

    //获取病人诊疗数据
    public ArrayList<Map<String,Object>> getMedicalData(String id) throws Exception{
        String sql="select r.diseaseName, r.Age, p.ItemName "+
            "from  registration r, prescription p where r.ClinicRegisterCode = ? and  r.ClinicRegisterCode=p.ClinicRegisterCode  and  CollectFeesCategoryName=\"西药费\" ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setString(1,id);
        ResultSet rs = pst.executeQuery();
        ArrayList<Map<String,Object>> resultList = new ArrayList<>();
        while (rs.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("disease", rs.getString("diseaseName"));
            map.put("Age", rs.getInt("Age"));
            map.put("med", rs.getString("ItemName"));
            resultList.add(map);
        }
        return resultList;
    }

    //获取药品禁忌症数据
    public Map<String,Object> getMedDescription(int id) throws Exception{
        String sql="select ms.preparation, ms.chineseName "+
            "from  medicinesymptom ms where ms.id=? ";
        StringBuilder sb = new StringBuilder(sql);
        PreparedStatement pst=conn.prepareStatement(sb.toString());
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();
        Map<String, Object> map = new HashMap<>();
        while(rs.next()) {

            map.put("name", rs.getString("chinesename"));
            map.put("preparation", rs.getString("preparation"));

        }
        return map;
    }

}