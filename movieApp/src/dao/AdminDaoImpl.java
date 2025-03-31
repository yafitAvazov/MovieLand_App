package dao;

import dm.Admin;
import dm.Movie;

import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class AdminDaoImpl implements IDao<String, Admin> {
    private final String FILE_PATH = "src/resources/Admins.dat";
    private Map<String, Admin> adminsMap;

    public AdminDaoImpl() {
        adminsMap = new HashMap<>();
        loadDataFromFile();
        if (adminsMap.isEmpty() || !adminsMap.containsKey("admin")) {
            Admin superAdmin = new Admin("Super Admin", "admin", "admin123");
            adminsMap.put("admin", superAdmin);
            saveDataToFile();
        }
        System.out.println("Loaded Admins: " + adminsMap);
    }



    @Override
    public void add(String id, Admin admin) {
        if ("admin".equals(id) && !adminsMap.containsKey("admin")) {
            admin = new Admin("Super Admin", "admin", "admin123");
        }
        adminsMap.put(id, admin);
        saveDataToFile();
        System.out.println("Admin added: " + admin);
    }

    @Override
    public void delete(String id) {
        adminsMap.remove(id);
        saveDataToFile();
    }

    @Override
    public void update(String id, Admin admin) {
        if (adminsMap.containsKey(id)) {
            adminsMap.put(id, admin);
            saveDataToFile();
        }
    }

    @Override
    public Admin get(String id) {
        return adminsMap.get(id);
    }

    @Override
    public Map<String, Admin> getAll() {
        return adminsMap;
    }






    private void loadDataFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            adminsMap = (Map<String, Admin>) in.readObject();
        } catch (FileNotFoundException e) {
            adminsMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    private void saveDataToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(adminsMap);
            System.out.println("Admin data saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}