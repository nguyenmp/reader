package com.nguyenmp.reader.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialHelper {
    public static String toString(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            return new String(outputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public static Object fromString(String input) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}
