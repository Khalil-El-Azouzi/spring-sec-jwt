package org.sid.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
//@Data @AllArgsConstructor @NoArgsConstructor
public class AppUser {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String username;
	
	//on veut que si un user est register, le system ne retourne pas son password pour protection  
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)//pour charger les roles de chaque utilisateur authentifier
	private Collection<AppRole> roles = new ArrayList<>();
	
	public AppUser() {
		super();
	}

	public AppUser(Long id, String username, String password, Collection<AppRole> roles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@JsonIgnore //ignorer le password dans la déserialisation
	public String getPassword() {
		return password;
	}
	
	@JsonSetter //prondre le password quand il vient de json client
	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<AppRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<AppRole> roles) {
		this.roles = roles;
	}
	
}
