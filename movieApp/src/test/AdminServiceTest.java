import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dm.Admin;
import services.AdminService;

import java.util.HashMap;
import java.util.Map;

public class AdminServiceTest {
    private AdminService adminService;
    private Map<String, Admin> adminMap;

    @BeforeEach
    void setUp() {
        adminMap = new HashMap<>();
        adminService = new AdminService();
    }

    @Test
    void testAddAdmin() {
        Admin admin = new Admin("Alice Cohen", "alicec", "AlicePass123");
        adminService.addAdmin("alicec", admin);
        assertEquals(admin, adminService.getAdmin("alicec"));
    }

    @Test
    void testDeleteAdmin() {
        Admin admin = new Admin("David Levi", "davidl", "SecurePass456");
        adminService.addAdmin("davidl", admin);
        adminService.deleteAdmin("davidl");
        assertNull(adminService.getAdmin("davidl"));
    }

    @Test
    void testUpdateAdmin() {
        Admin admin = new Admin("Ethan Green", "ethang", "MyPassword789");
        adminService.addAdmin("ethang", admin);
        admin.setPassword("UpdatedPass987"); // שינוי סיסמה
        adminService.updateAdminData("ethang", admin);
        assertEquals("UpdatedPass987", adminService.getAdmin("ethang").getPassword());
    }

    @Test
    void testGetAdmin() {
        Admin admin = new Admin("Noa Feldman", "noaf", "NoaPass001");
        adminService.addAdmin("noaf", admin);
        assertNotNull(adminService.getAdmin("noaf"));
        assertEquals("noaf", adminService.getAdmin("noaf").getUsername());
    }

    @Test
    void testGetAllAdmins() {
        Admin admin1 = new Admin("Lior Shaked", "liors", "LiorSuper123");
        Admin admin2 = new Admin("Maya Tal", "mayat", "MayaSafePass");

        adminService.addAdmin("liors", admin1);
        adminService.addAdmin("mayat", admin2);

        Map<String, Admin> retrievedAdmins = adminService.getAllAdminsMap();
        assertEquals(retrievedAdmins.size(), retrievedAdmins.size());
        assertTrue(retrievedAdmins.containsKey("liors"));
        assertTrue(retrievedAdmins.containsKey("mayat"));
    }
}
