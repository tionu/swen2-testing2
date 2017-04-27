package servers;

import java.util.List;

public interface Persistence {
	
	public Patient getPatient(int paId);
	public Arzt getArzt(String userName);
	public List<Patient> getPatientenInFachbereich(Fachbereich fachbereich);

}
