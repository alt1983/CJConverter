

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        Type listType = new TypeToken<List<Type>>() {
        }.getType();
        String json = listToJson(list, listType);
        writeString(json, "data.json");

    }

    public static void writeString(String s, String fileName) {

        File res = new File(fileName);
        if (!res.exists()) {
            try {
                res.createNewFile();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(s);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


    }


    public static String listToJson(List<Employee> list, Type listType) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(list, listType);
        return s;

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping[0], columnMapping[1], columnMapping[2], columnMapping[3], columnMapping[4]);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return staff;

    }


}