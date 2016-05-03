import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Date;

public class PatientTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM patients *;";
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
    }
  }

  @Test
  public void patient_instantiatesCorectly_true() {
    Date birthdate = new Date(81, 11, 18);
    Patient newPatient = new Patient("Patient McPatientface", birthdate);
    assertEquals(true, newPatient instanceof Patient);
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Date birthdate = new Date(81, 11, 18);
    Patient myPatient = new Patient("Bobby Jones", birthdate);
    myPatient.save();
    assertTrue(Patient.all().get(0).equals(myPatient));
  }

  @Test
  public void assignDoctor_properlyAssignsDoctorIDtoPatient_true() {
    Date birthdate = new Date(81, 11, 18);
    Patient myPatient = new Patient("NeedsADoctor Jones", birthdate);
    myPatient.save();
    Doctor myDoctor = new Doctor("Allen Jones", "Oncology");
    myDoctor.save();
    myPatient.assignDoctor(1);
    assertEquals(Patient.all().get(0).getDoctor(), 1);
  }
}
