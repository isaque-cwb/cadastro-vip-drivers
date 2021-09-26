package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Parceiro implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String telefone;
	private String email;
	private Date birthDate;
	private String endereco;
	
	private String contatoFamiliar;
	
	
	private Department department;
	
	public Parceiro() {
	}

	public Parceiro(Integer id, String nome, String telefone, String email, Date birthDate, String endereco, String contatoFamiliar,  Department department) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.birthDate = birthDate;
		this.endereco = endereco;
		this.department = department;
		
		this.contatoFamiliar = contatoFamiliar;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getContatoFamiliar() {
		return contatoFamiliar;
	}

	public void setContatoFamiliar(String contatoFamiliar) {
		this.contatoFamiliar = contatoFamiliar;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Parceiro other = (Parceiro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Parceiro [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + ", birthDate="
				+ birthDate + ", endereco=" + endereco + ", contatoFamiliar="
				+ contatoFamiliar + ", department=" + department + "]";
	}

		
	
}
