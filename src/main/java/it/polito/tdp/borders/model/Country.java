package it.polito.tdp.borders.model;

public class Country {
	
	private String abb;
	private int codice;
	private String nome;
	private int confini;
	
	public Country(String abb, int codice, String nome) {
		this.abb = abb;
		this.codice = codice;
		this.nome = nome;
	}
	
	public String getAbb() {
		return abb;
	}
	public void setAbb(String abb) {
		this.abb = abb;
	}
	public int getCodice() {
		return codice;
	}
	public void setCodice(int codice) {
		this.codice = codice;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codice;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (codice != other.codice)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return nome;
	}


	
	

}
