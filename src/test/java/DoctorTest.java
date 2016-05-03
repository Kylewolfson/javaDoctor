import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

public class DoctorTest {

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
  public void Doctor_instantiatesCorrectly_true() {
    Doctor myDoctor = new Doctor("Allen Jones", "Oncology");
    assertEquals(true, myDoctor instanceof Doctor);
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Doctor.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Doctor firstDoctor = new Doctor("Allen Jones", "Oncology");
    Doctor secondDoctor = new Doctor("Allen Jones", "Oncology");
    assertTrue(firstDoctor.equals(secondDoctor));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Doctor myDoctor = new Doctor("Allen Jones", "Oncology");
    myDoctor.save();
    assertTrue(Doctor.all().get(0).equals(myDoctor));
  }

  @Test
  public void save_assignsIdToObject() {
    Doctor myDoctor = new Doctor("Allen Jones", "Oncology");
    myDoctor.save();
    Doctor savedDoctor = Doctor.all().get(0);
    assertEquals(myDoctor.getId(), savedDoctor.getId());
  }

  @Test
  public void find_findsDoctorInDatabase_true() {
    Doctor myDoctor = new Doctor("Allen Jones", "Oncology");
    myDoctor.save();
    Doctor savedDoctor = Doctor.find(myDoctor.getId());
    assertTrue(myDoctor.equals(savedDoctor));
  }

  @Test
  public void getPatients_retrievesAllPatientsOfDoctorFromDataBase_patients() {
    Doctor myDoctor = new Doctor ("Allen Jones", "Oncology");
    myDoctor.save();
    Date birthdate = new Date(81, 11, 18);
    Patient firstPatient = new Patient("Kyle", birthdate);
    firstPatient.save();
    firstPatient.assignDoctor(myDoctor.getId());
    System.out.println("firstpatient doc" + firstPatient.getDoctor());//1
    birthdate = new Date(81, 6, 9);
    Patient secondPatient = new Patient("Second patient", birthdate);
    secondPatient.save();
    secondPatient.assignDoctor(myDoctor.getId());
    System.out.println("mydoc get patients"  + myDoctor.getPatients());
    Patient[] patients = new Patient[] {firstPatient, secondPatient};
    System.out.println(patients[0].getDoctor());
    System.out.println(patients[1].getDoctor());
    System.out.println(myDoctor.getId());
    assertTrue(myDoctor.getPatients().containsAll(Arrays.asList(patients)));
  }
}
