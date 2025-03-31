package services;

import dao.AdminDaoImpl;
import dao.IDao;
import dm.Admin;
import java.util.Map;

public class AdminService {
    private final IDao<String, Admin> adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDaoImpl();
    }

    public void addAdmin(String id, Admin admin) {
        adminDAO.add(id, admin);
    }

    public void deleteAdmin(String id) {
        adminDAO.delete(id);
    }

    public void updateAdminData(String id, Admin admin) {
        adminDAO.update(id, admin);
    }

    public Admin getAdmin(String id) {
        return adminDAO.get(id);
    }

    public Map<String, Admin> getAllAdminsMap() {
        return adminDAO.getAll();
    }
}
