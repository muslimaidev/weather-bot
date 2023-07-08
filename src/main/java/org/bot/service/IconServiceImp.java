package org.bot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class IconServiceImp implements IconService {

    @Override
    public String getEmoji(String emojiCode) {
        File file = new File("src/main/resources/icons.json");
        InputStream inputStream = null;
        Map<String, String> icons = new HashMap<>();
        try{
            inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();

            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row);
            }

            Type typeToken = new TypeToken<Map<String, String>>() {
            }.getType();

            Gson gson = new Gson();
            icons = gson.fromJson(stringBuilder.toString(), typeToken);


        }catch (Exception e){
            System.out.println(e.toString());
        }
        return icons.get(emojiCode);
    }
}
