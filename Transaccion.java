package com.jaime.movimientodebanco;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)

public class Transaccion {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Long id;
	@Persistent
	int numerodecliente;
	@Persistent
	String nombredecliente;
	@Persistent
	String concepto;
	@Persistent
	Double monto;
	
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	public int getNumerodecliente(){
		return numerodecliente;
	}
	public void setNumerodecliente(int numerodecliente){
		this.numerodecliente = numerodecliente;
	}
	public String getNombredeCliente(){
		return nombredecliente;
	}
	public void setNombredeCliente(String nombredecliente){
		this.nombredecliente = nombredecliente;
	}
	public String getConcepto(){
		return concepto;
	}
	public void setConcepto(String concepto){
		this.concepto = concepto;
	}
	public Double getMonto(){
		return monto;
	}
	public void setMonto(Double monto){
		this.monto = monto;
	}	

}
