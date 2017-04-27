package servers;

import java.util.Date;

public class Patient {
	
	public static class PatientBuilder {
		
		private int paId;
		private int hoId;
		private String vorname;
		private String nachname;
		private Date geburtsDatum;
		private String fachbereich;
		private Date aufnahmeDatum;
		private Date entlassDatum;

		public PatientBuilder(int paId, int hoId) {
			this.paId = paId;
			this.hoId = hoId;
		}
		
		public PatientBuilder name(String vorname, String nachname) {
			this.vorname = vorname;
			this.nachname = nachname;
			return this;
		}
		
		public PatientBuilder geburtsDatum(Date geburtsdatum) {
			this.geburtsDatum = geburtsdatum;
			return this;
		}
		
		public PatientBuilder fachbereich(String fachbereich) {
			this.fachbereich = fachbereich;
			return this;
		}
		
		public PatientBuilder aufnahmeDatum(Date aufnahmeDatum) {
			this.aufnahmeDatum = aufnahmeDatum;
			return this;
		}
		
		public PatientBuilder entlassDatum(Date entlassDatum) {
			this.entlassDatum = entlassDatum;
			return this;
		}
		
		public Patient build() {
			Patient patient = new Patient(this);
			return patient;
		}
	}
	
	private int paId;
	private int hoId;
	private String vorname;
	private String nachname;
	private Date geburtsDatum;
	private String fachbereich;
	private Date aufnahmeDatum;
	private Date entlassDatum;
	
	private Patient(PatientBuilder patientBuilder) {
		this.paId =          patientBuilder.paId;
		this.hoId =          patientBuilder.hoId;
		this.vorname =       patientBuilder.vorname;
		this.nachname =      patientBuilder.nachname;
		this.geburtsDatum =  patientBuilder.geburtsDatum;
		this.fachbereich =   patientBuilder.fachbereich;
		this.aufnahmeDatum = patientBuilder.aufnahmeDatum;
		this.entlassDatum =  patientBuilder.entlassDatum;
	}

	public int getPaId() {
		return paId;
	}

	public int getHoId() {
		return hoId;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public Date getGeburtsDatum() {
		return geburtsDatum;
	}

	public String getFachbereich() {
		return fachbereich;
	}

	public Date getAufnahmeDatum() {
		return aufnahmeDatum;
	}

	public Date getEntlassDatum() {
		return entlassDatum;
	}
	
	

}
