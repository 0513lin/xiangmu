package com.bjpowernode;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.model.Person;
import com.bjpowernode.model.School;
import com.bjpowernode.model.Student;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyTest {

    // json序列化： 把java对象转为json格式的数据字符串
    @Test
    public void test01(){

        Student student  = new Student();
        student.setId(1001);
        student.setName("李四");
        student.setAge(29);

        //使用fastjson  {"age":29,"id":1001,"name":"李四"}
        String json = JSONObject.toJSONString(student);
        System.out.println("student json序列化的结果："+json);
    }

    @Test
    public void test02(){
        List<Student> list = new ArrayList<Student>();
        Student student  = new Student();
        student.setId(1001);
        student.setName("李四");
        student.setAge(29);
        list.add(student);

        student  = new Student();
        student.setId(1002);
        student.setName("张三");
        student.setAge(28);
        list.add(student);

        //[{"age":29,"id":1001,"name":"李四"},{"age":28,"id":1002,"name":"张三"}]
        String jsonarray  = JSONObject.toJSONString(list);
        System.out.println("List 序列化="+jsonarray);

    }

    //把json字符串转为 java对象
    @Test
    public void test03(){
        String json = "{\"age\":30,\"id\":1005,\"name\":\"李四\"}";
        // json的反序列化， 把json转java对象
        Student student = JSONObject.parseObject(json, Student.class);
        System.out.println("student="+student);
    }

    @Test
    public void test04(){
        String json = "{\"age\":30,\"id\":1005,\"name\":\"李四\"}";
        // json的反序列化，
        JSONObject jsonObject = JSONObject.parseObject(json);
        String name = jsonObject.getString("name");
        System.out.println("name = " + name);

        int age = jsonObject.getIntValue("age");
        Integer age1 = jsonObject.getInteger("age");
        String age2 = jsonObject.getString("age");
        System.out.println("age="+age);


    }


    @Test
    public void test05(){
        School school = new School();
        school.setName("北京大学");
        school.setAddress("北京的海淀区");

        Person p = new Person();
        p.setAge(20);
        p.setName("郑强");
        p.setPhone("136000000");
        p.setSchool(school);
//{"age":20,"name":"郑强","phone":"136000000",
// "school":{"address":"北京的海淀区","name":"北京大学"}}
        String str  = JSONObject.toJSONString(p);
        System.out.println("str = " + str);

    }

    @Test
    public void test06(){
        String str="{\"age\":20,\"name\":\"李响\",\"phone\":\"1350000000\",\"school\":{\"address\":\"北京的海淀区\",\"name\":\"北京大学\"}}";
        Person person = JSONObject.parseObject(str, Person.class);
        System.out.println("person="+person);

        System.out.println(person.getSchool().getName());

    }

    @Test
    public void test07(){
        String str="{\"age\":20,\"name\":\"李响\",\"phone\":\"1350000000\",\"school\":{\"address\":\"北京的海淀区\",\"name\":\"北京大学\"}}";
        JSONObject obj = JSONObject.parseObject(str);

        String json = obj.getString("school");
        System.out.println("school json="+json);

        JSONObject obj2 = JSONObject.parseObject(json);
        System.out.println("obj2. shcool="+obj2.getString("name"));


        String name2 = JSONObject.parseObject(str).getJSONObject("school").getString("name");
        System.out.println("name2 = " + name2);


    }

    @Test
    public void test08(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for( int i = 1;i<10;i++){
            //System.out.println(random.nextInt(30));
            System.out.println(random.nextInt(30,50));
        }
    }

    @Test
    public void test09(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for( int i = 1;i<10;i++){
            System.out.println(random.nextInt(1000,10000));
        }
    }

    @Test
    public void test10(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuffer buffer = new StringBuffer();
        for( int i = 1;i<20;i++){

            for(int m=0;m<4;m++){
                buffer.append( random.nextInt(10));
            }
            System.out.println(buffer.toString());
            buffer = new StringBuffer("");
        }
    }

    @Test
    public void testGET(){
        //使用HttpClient通过http协议，访问其他应用（第三方接口）
        // 获取一个HttpClient对象  发起请求，传递参数， 接收返回结果
        CloseableHttpClient client = HttpClients.createDefault();

        //创建一个请求对象 Request
        String uri="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13660000000";


        //发起请求
        try {
            HttpGet get = new HttpGet(uri);
            //CloseableHttpResponse访问uri之后，对方给我们程序的应答。
            CloseableHttpResponse response = client.execute(get);
            //判断状态码
            StatusLine httpStatus = response.getStatusLine();
            System.out.println("http 状态码="+httpStatus.getStatusCode());
            // httpStatus.getStatusCode() == 200
            if( httpStatus.getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity  = response.getEntity(); // 获取返回结果

                //应答head信息
                String contentType = entity.getContentType().toString();
                System.out.println("contentType = " + contentType);

                InputStream in = entity.getContent();//返回的结果内容,数据
                BufferedReader bin = new BufferedReader( new InputStreamReader( in,"GBK")) ;
                String line = null;
                StringBuilder builder = new StringBuilder();
                while( (line= bin.readLine()) != null){
                    builder.append(line);
                }

                System.out.println("数据："+builder.toString());

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPOST(){
        String uri = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
        CloseableHttpClient client = HttpClients.createDefault();

        try {

            //创建Post
            HttpPost post  = new HttpPost(uri);
            //form   <input type="text" name="age" value="20"/>
            //使用HttpEntity构建请求的数据
            //NameValuePair 就是 key=value格式，保存请求的参数
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add( new BasicNameValuePair("tel","13800000000"));
            //其他参数
            HttpEntity entity = new UrlEncodedFormEntity(pairs);
            post.setEntity(entity);

            //执行请求
            CloseableHttpResponse response = client.execute(post);

            //获取结果
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
                System.out.println("result ="+result);

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
