package com.globant.academy.api.utils;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonFileReader <T> {
    public T getObjectByJson(String jsonFileName, Class<T> type) {
        T obj = null;
        try (Reader reader = new FileReader(jsonFileName)) {
            Gson gson = new Gson();
            obj = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
