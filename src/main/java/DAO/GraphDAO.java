package DAO;


import org.neo4j.driver.*;

import java.util.ArrayList;

public class GraphDAO implements AutoCloseable
{
    private final Driver driver;

    //图数据库的地址，用户名，密码
    public GraphDAO()
    {
        driver = GraphDatabase.driver( "bolt://localhost:11003", AuthTokens.basic( "neo4j", "pwd" ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    /** NEO4j官方模板
     * public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String trans = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute(Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                            "SET a.message = $message " +
                            "RETURN a.message + ', from node ' + id(a)",
                        parameters( "message", message ) );
                    Result result = tx.run( "MATCH  (a:Greeting) " );
                    return result.single().get( 0 ).asString();

                }
            } );
            System.out.println( trans );
        }
    }**/

    //建立药品节点
    public void createMedNode(String chinese,String  english){
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MERGE(a:med{chinese:'"+chinese+"',english:'"+english+"'})";
                    transaction.run(cql);
                    return null;
                }
            });
        }
    }


    //建立适应症节点
    public void createSymNode(String symptom){
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MERGE(b:sym{chinese:'"+symptom+"'})";
                    transaction.run(cql);
                    return null;
                }
            });
        }
    }

    //建立禁忌症结点
    public void createFbdNode(String forbid){
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MERGE(c:fbd{chinese:'"+forbid+"'})";
                    transaction.run(cql);
                    return null;
                }
            });

        }
    }


    //建立适应症关系
    public void createSymRelation(String med,String symptom){
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MATCH (m:med{chinese:\""+med+"\"})\n"+
                        "MATCH (s:sym{chinese:\""+symptom+"\"})\n"+
                        "MERGE(m)-[:isSymptom]->(s)";
                    transaction.run(cql);
                    return null;
                }
            });
        }
    }

    //建立禁忌症关系
    public void createFbdRelation(String med,String forbid){
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MATCH (m:med{chinese:\""+med+"\"})\n"+
                        "MATCH (f:fbd{chinese:\""+forbid+"\"})\n"+
                        "MERGE(m)-[:isForbidden]->(f)";
                    transaction.run(cql);
                    return null;
                }
            });
        }
    }


    //知识图谱中检索适应症
    public ArrayList<String> SearchSym(String med){
        ArrayList<String> resultList=new ArrayList<>();
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MATCH (n)-[r:isSymptom]->(m) where n.chinese='"+med+"'RETURN m.chinese";
                    Result result=transaction.run(cql);
                    while ( result.hasNext() )
                    {
                        Record record = result.next();
                        resultList.add(record.get("m.chinese").toString());
                        //System.out.println(record.get("m.chinese"));
                    }
                    return null;
                }
            });

        }
        return resultList;
    }

    //知识图谱中检索禁忌症
    public ArrayList<String> SearchFbd(String med){
        ArrayList<String> resultList=new ArrayList<>();
        try(Session session=driver.session()){
            String trans=session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    String cql="MATCH (n)-[r:isForbidden]->(m) where n.chinese='"+med+"'RETURN m.chinese";
                    Result result=transaction.run(cql);
                    while ( result.hasNext() )
                    {
                        Record record = result.next();
                        resultList.add(record.get("m.chinese").toString());
                        //System.out.println(record.get("m.chinese"));
                    }
                    return null;
                }
            });

        }
        return resultList;
    }

    //测试用主函数
    /*public static void main( String[] args ) throws Exception
    {
        String med="降纤酶";
        String symptom="血栓";
        String cql="MATCH (m:med{chinese:\""+med+"\"})\n"+
            "MATCH (s:sym{chinese:\""+symptom+"\"})\n"+
            "CREATE(m)-[:isSymptom]->(s)";
        System.out.println(cql);
        try ( GraphDAO dao = new GraphDAO())
        {
            ArrayList<String>list=dao.SearchSym("降纤酶");
            System.out.println(list);
        }
    }*/
}