package src.co.edu.uptc.services;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;
import src.co.edu.uptc.models.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServicesParcial {
    
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private JSONArray jsonArray;
    private JSONParser parser;
    private File file;
    private String path;
    private List<Person> people;


    public ServicesParcial(){
        people= new ArrayList<Person>();
    }

    public void setPath(String path){
        this.path=path;
    }
    
    private int mayorSalary(){
        int salaryProm=0;
        int count=0;
        for (Person person : people) {
            salaryProm=salaryProm+person.getSalary();
            count=count+1;
        }
        salaryProm=salaryProm/count;
        salaryProm=Math.abs(salaryProm);                                                                                                                                                        
        return salaryProm;
    }

    private void actuFile(String path){
        file= new File(path);
    }
    private void createWrite() throws IOException{
        fileWriter= new FileWriter(file);
        bufferedWriter= new BufferedWriter(fileWriter);
    }

    private void closeWrite() throws IOException{
        bufferedWriter.close();
    }

    private void createJson() throws Exception  {
        parser = new JSONParser();
        jsonArray = (JSONArray) parser.parse(new FileReader(path));
    }

    private void jsonToArray(){
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject person = (JSONObject)jsonArray.get(i);
            Person person1= new Person();
            person1.setAge(Math.toIntExact((long) person.get("edad")));
            person1.setName((String) person.get("nombre"));
            person1.setLastname((String) person.get("apellido"));
            person1.setSalary(Math.toIntExact((long) person.get("salario")));
            people.add(person1);
        }
    }

    private String organiceMayorSalaryJson(JSONArray persoJsonArray){
        int promSalary=mayorSalary();
        String text="[";
        for (int i = 0; i < persoJsonArray.size(); i++) {
            JSONObject person = (JSONObject)persoJsonArray.get(i);
            if (Math.toIntExact((long) person.get("salario"))>=promSalary) {
                text=text+persoJsonArray.get(i)+","+"\n"; 
            }
        }
        text=text.substring(0, (text.length()-2))+"]";
        return text;
    }

    private String organiceMenorSalaryJson(JSONArray persoJsonArray){
        int promSalary=mayorSalary();
        String text="[";
        for (int i = 0; i < persoJsonArray.size(); i++) {
            JSONObject person = (JSONObject)persoJsonArray.get(i);
            if (Math.toIntExact((long) person.get("salario"))<promSalary) {
                text=text+persoJsonArray.get(i)+","+"\n";   
            }
        }
        text=text.substring(0, (text.length()-2))+"]";
        return text;
    }

    public void readyJson() throws Exception{
        createJson();
        jsonToArray();
        writeJsonMayor();
        writeJsonMenor();
    }

    private void writeJsonMenor() throws IOException{
        actuFile(".\\jsonPersonMenor.JSON");
        createWrite();
        writeFile(organiceMenorSalaryJson(jsonArray));
        closeWrite();
    }

    private void writeJsonMayor() throws IOException{
        actuFile(".\\jsonPersonMayor.JSON");
        createWrite();
        writeFile(organiceMayorSalaryJson(jsonArray));
        closeWrite();
    }

    private void writeFile(String contended) throws IOException {
        bufferedWriter.write(contended);
    }
}
