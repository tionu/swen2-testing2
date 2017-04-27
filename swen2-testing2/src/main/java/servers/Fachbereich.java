package servers;

public enum Fachbereich {
	
	CHIRURGIE(1), INNERE(2);
	
	private int id;
	
	private Fachbereich(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Fachbereich getFachbereichById(int id) {
		for(Fachbereich fachbereich : Fachbereich.values())
			if(fachbereich.getId() == id)
				return fachbereich;
		return null;
	}

}
