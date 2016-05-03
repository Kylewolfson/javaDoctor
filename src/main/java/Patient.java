import java.util.List;
import org.sql2o.*;
import java.util.Date;

public class Patient {
  private String name;
  private int doctor_id;
  private Date birthdate;
  private int id;

  public Patient(String name, Date birthdate) {
    this.name = name;
    this.birthdate = birthdate;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public Date getBirthdate() {
    return birthdate;
  }

  public int getDoctor() {
    return doctor_id;
  }

  public void assignDoctor(int doctor_id) {
    try(Connection con = DB.sql2o.open()) {
      int id = this.getId();
      String sql = "UPDATE patients SET doctor_id = :doctor_id WHERE id = :patientId";
      con.createQuery(sql)
      .addParameter("doctor_id", doctor_id)
      .addParameter("patientId", id)
      .executeUpdate();
      }
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO Patients(name, birthdate) VALUES (:name, :birthdate)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("birthdate", this.birthdate)
      .executeUpdate()
      .getKey();
    }
  }

    public static List<Patient> all() {
      String sql = "SELECT id, name, birthdate, doctor_id FROM patients";
      try(Connection con = DB.sql2o.open()) {
        return con.createQuery(sql).executeAndFetch(Patient.class);
      }
    }
    @Override
    public boolean equals(Object otherPatient) {
      if (!(otherPatient instanceof Patient)) {
        return false;
      } else {
        Patient newPatient = (Patient) otherPatient;
        return this.getName().equals(newPatient.getName()) &&
               this.getId() == newPatient.getId();
      }
    }




}
