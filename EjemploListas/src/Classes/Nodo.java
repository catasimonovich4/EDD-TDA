package Classes;
import TDALista.*;

public class Nodo<E> implements Position<E>{
	private E elemento;
	private Nodo<E> siguiente;
	
	//Constructores
	public Nodo(E item, Nodo<E> sig) {
		elemento = item;
		siguiente = sig;
	}
	
	public Nodo(E item) {
		elemento = item;
		siguiente = null;
	}
	
	//Setters
	public void setElemento(E item) {
		elemento = item;
	}
	
	public void setSiguiente(Nodo<E> sig) {
		siguiente = sig;
	}
	
	//Getters
	public E getElemento() {
		return elemento;
	}
	
	public Nodo<E> getSiguiente() {
		return siguiente;
	}
	
	//Metodo de Position<E>
	//Retorna el valor del elemento ubicado en la posicion
	public E element() {
		return elemento;
	}
}
