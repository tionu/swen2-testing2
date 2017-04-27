package servers;

public class Arzt {
	
	public static class ArztBuilder {
		
		private int id;
		private String vorname;
		private String nachname;
		private String titel;
		private Fachbereich fachbereich;
		private String username;
		private String password;
		
		public ArztBuilder id(int id) {
			this.id = id;
			return this;
		}
		
		public ArztBuilder vorname(String vorname) {
			this.vorname = vorname;
			return this;
		}
		
		public ArztBuilder nachname(String nachname) {
			this.nachname = nachname;
			return this;
		}
		
		public ArztBuilder titel(String titel) {
			this.titel = titel;
			return this;
		}
		
		public ArztBuilder fachbereich(Fachbereich fachbereich) {
			this.fachbereich = fachbereich;
			return this;
		}
		
		public ArztBuilder username(String username) {
			this.username = username;
			return this;
		}
		
		public ArztBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		public Arzt build() {
			Arzt arzt = new Arzt(this);
			return arzt;
		}
		
	}
	
	private int id;
	private String vorname;
	private String nachname;
	private String titel;
	private Fachbereich fachbereich;
	private String username;
	private String password;
	
	private Arzt(ArztBuilder arztBuilder) {
		this.id = arztBuilder.id;
		this.nachname = arztBuilder.nachname;
		this.vorname = arztBuilder.vorname;
		this.titel = arztBuilder.titel;
		this.fachbereich = arztBuilder.fachbereich;
		this.username = arztBuilder.username;
		this.password = arztBuilder.password;
	}

	public int getId() {
		return id;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getTitel() {
		return titel;
	}

	public Fachbereich getFachbereich() {
		return fachbereich;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	
}
