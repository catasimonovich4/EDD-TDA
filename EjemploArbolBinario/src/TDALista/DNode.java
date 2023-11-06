package TDALista;

import Interfaces.*;

public class DNode<E> implements Position<E>{
	/**
	 * Elemento almacenado en esta posicion.
	 */
	private E element;						
	/**
	 * Referencia a los nodos previo y siguiente.
	 */
	private DNode<E> prev, next;				
	
	
	/**
	 * Crea un nodo que se inicializa con los parametros ingresados.
	 * @param newPrev Nodo que se quiere determinar como previo.
	 * @param newNext Nodo que se quiere determinar como siguiente.
	 * @param elem Elemento a almacenar en el nodo.
	 */
	public DNode(DNode<E> newPrev, DNode<E> newNext, E elem) {		
		prev = newPrev;
		next = newNext;
		element = elem; 
	}
	
	/**
	 * Consulta el elemento almacenado en el nodo.
	 * @return Elemento almacenado en el nodo.
	 */
	public E element() {
		return element;
	}
	
	/**
	 * Consulta el nodo siguiente de un nodo.
	 * @return Nodo siguiente.
	 */
	public DNode<E> getNext() {
		return next;
	} 
	/**
	 * Consulta el nodo previo de un nodo.
	 * @return Nodo previo.
	 */
	public DNode<E> getPrev() {
		return prev;
	}
	
	/**
	 * Cambia la referencia al nodo siguiente de un nodo, por el pasado por parametro.
	 * @param newNext Nodo que sera la nueva referencia del nodo siguiente.
	 */
	public void setNext(DNode<E> newNext) {
		next = newNext;
	}
	/**
	 * Cambia la referencia al nodo previo de un nodo, por el pasado por parametro.
	 * @param newPrev Nodo que sera la nueva referencia del nodo previo.
	 */
	public void setPrev(DNode<E> newPrev) {
		prev = newPrev;
	} 
	/**
	 * Cambia el elemento almacenado en esta posicion, por el pasado por parametro.
	 * @param elem Elemento que sera el nuevo valor almacenado en esta posicion.
	 */
	public void setElement(E elem) {
		element = elem;
	}
}
