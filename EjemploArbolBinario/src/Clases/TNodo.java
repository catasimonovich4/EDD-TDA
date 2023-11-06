package Clases;

import Interfaces.*;
import TDALista.ListaDoblementeEnlazada;

public class TNodo<E> implements Position<E>{
	protected E elem;							//Rotulo del nodo
	protected TNodo<E> padre;
	protected PositionList<TNodo<E>> hijos;
	
	public TNodo(E elemento, TNodo<E> padre) {
		elem = elemento;
		this.padre = padre;
		hijos = (PositionList<TNodo<E>>) new ListaDoblementeEnlazada<TNodo<E>>(); 
	}
	
	//Getters 
	public E element() {
		return elem;
	}
	
	public TNodo<E> getPadre() {
		return padre;
	}
	
	public PositionList<TNodo<E>> getHijos() {
		return hijos;
	}
	
	//Setters
	public void setPadre(TNodo<E> p) {
		padre = p;
	}
	
	public void setElement(E elem) {
		this.elem = elem;
	}
}
