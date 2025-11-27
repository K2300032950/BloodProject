package com.example.demo.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artist_details {
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    private String username;
	    @Column(unique = true)
	    private String email;
	    private String password;

	    
	    

	    public Artist_details(Long id, String username, String email, String password) {
	        this.id = id;
	        this.username = username;
	        this.email = email;
	        this.password = password;
	    }

	    
	    
	    public Artist_details() {
			super();
			// TODO Auto-generated constructor stub
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

	    public String getEmail() {   // Correct naming
	        return email;
	    }

	    public void setEmail(String email) { // Correct naming
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    @Override
	    public String toString() {
	        return "Admin_details [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + "]";
	    }
}
