package TDALista;

import java.util.Iterator;

import Exceptions.BoundaryViolationException;
import Exceptions.EmptyListException;
import Exceptions.InvalidPositionException;
import Interfaces.*;

/**
 * Clase Lista Doblemente Enlazada que implementa la interfaz PositionList.
 * @author Catalina Simonovich y Valentina Beron.
 */
public class ListaDoblementeEnlazada<E> implements PositionList<E>{
	/**
	 * Centinelas que marcan el inicio y el final de la lista, respectivamente.
	 */
	protected DNode<E> header, trailer;	
	/**
	 * Numero de elementos que contiene la lista.
	 */
	protected int size;												
	
	/**
	 * Constructor que crea una Lista Doblemente Enlazada vacia.
	 */
	public ListaDoblementeEnlazada() {
		header = new DNode(null, null, null);					
		trailer = new DNode(null, null, null);						
		header.setNext(trailer);
		trailer.setPrev(header);
		size = 0;
	}
	
	/**
	 * Consulta la cantidad de elementos de la lista.
	 * @return Cantidad de elementos de la lista.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Consulta si la lista est� vac�a.
	 * @return Verdadero si la lista est� vac�a, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Devuelve la posici�n del primer elemento de la lista. 
	 * @return Posici�n del primer elemento de la lista.
	 * @throws EmptyListException si la lista est� vac�a.
	 */
	public Position<E> first() throws EmptyListException {
		if (isEmpty())
			throw new EmptyListException("first(): empty list");
		return header.getNext();
	}
	
	/**
	 * Devuelve la posici�n del �ltimo elemento de la lista. 
	 * @return Posici�n del �ltimo elemento de la lista.
	 * @throws EmptyListException si la lista est� vac�a.
	 */
	public Position<E> last() throws EmptyListException {
		if (isEmpty())
			throw new EmptyListException("last(): empty list"); 
		return trailer.getPrev();
	}
	
	/**
	 * Devuelve la posici�n del elemento siguiente a la posici�n pasada por par�metro.
	 * @param p Posici�n a obtener su elemento siguiente.
	 * @return Posici�n del elemento siguiente a la posici�n pasada por par�metro.
	 * @throws InvalidPositionException si el posici�n pasada por par�metro es inv�lida o la lista est� vac�a.
	 * @throws BoundaryViolationException si la posici�n pasada por par�metro corresponde al �ltimo elemento de la lista.
	 */
	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNode<E> dn = checkPosition(p);
		if (dn.getNext() == trailer) 
			throw new BoundaryViolationException("next(): trailer's next");
		return dn.getNext(); 
	}
	
	/**
	 * Devuelve la posici�n del elemento anterior a la posici�n pasada por par�metro.
	 * @param p Posici�n a obtener su elemento anterior.
	 * @return Posici�n del elemento anterior a la posici�n pasada por par�metro.
	 * @throws InvalidPositionException si la posici�n pasada por par�metro es inv�lida o la lista est� vac�a.
	 * @throws BoundaryViolationException si la posici�n pasada por par�metro corresponde al primer elemento de la lista.
	 */
	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNode<E> dn = checkPosition(p);
		if (dn.getPrev() == header) 
			throw new BoundaryViolationException("prev(): header's prev");
		return dn.getPrev();	
	}
	
	/**
	 * Inserta un elemento al principio de la lista.
	 * @param element Elemento a insertar al principio de la lista.
	 */
	public void addFirst(E element) {
		DNode<E> dn = new DNode<E>(header, header.getNext(), element);
		DNode<E> sigDn = header.getNext();
		header.setNext(dn);
		sigDn.setPrev(dn);
		size++;
	}
	
	/**
	 * Inserta un elemento al final de la lista.
	 * @param element Elemento a insertar al final de la lista.
	 */
	public void addLast(E element) {
		if (isEmpty()) 
			addFirst(element);
		else {
			DNode<E> newDn = new DNode<E>(trailer.getPrev(), trailer, element);
			DNode<E> prevDn = trailer.getPrev();
			trailer.setPrev(newDn);
			prevDn.setNext(newDn);  
			size++;
		}
		
	}
	
	/**
	 * Inserta un elemento antes de la posici�n pasada como par�metro.
	 * @param p Posici�n en cuya posici�n anterior se insertar� el elemento pasado por par�metro. 
	 * @param element Elemento a insertar antes de la posici�n pasada como par�metro.
	 * @throws InvalidPositionException si la posici�n es inv�lida o la lista est� vac�a.
	 */
	public void addBefore(Position<E> p, E element) throws InvalidPositionException {
		DNode<E> dn = checkPosition(p);
		DNode<E> newDn = new DNode<E>(dn.getPrev(), dn, element);
		DNode<E> prevDn = dn.getPrev();
		dn.setPrev(newDn);			
		prevDn.setNext(newDn);
		size++;
	}
	
	/**
	 * Inserta un elemento luego de la posici�n pasada por par�matro.
	 * @param p Posici�n en cuya posici�n siguiente se insertar� el elemento pasado por par�metro.
	 * @param element Elemento a insertar luego de la posici�n pasada como par�metro.
	 * @throws InvalidPositionException si la posici�n es inv�lida o la lista est� vac�a.
	 */
	public void addAfter(Position<E> p, E element) throws InvalidPositionException {
		DNode<E> dn = checkPosition(p);
		DNode<E> newDn = new DNode<E>(dn, dn.getNext(), element);
		newDn.getNext().setPrev(newDn);
		dn.setNext(newDn);
		size++;
	} 
	
	/**
	 * Remueve el elemento que se encuentra en la posici�n pasada por par�metro.
	 * @param p Posici�n del elemento a eliminar.
	 * @return element Elemento removido.
	 * @throws InvalidPositionException si la posici�n es inv�lida o la lista est� vac�a.
	 */	
	public E remove(Position<E> p) throws InvalidPositionException {
		DNode<E> dn = checkPosition(p);
		E dnRemoved = dn.element();
		DNode<E> next = dn.getNext(); 
		DNode<E> prev = dn.getPrev(); 
		prev.setNext(next);
		next.setPrev(prev);
		size--;
		return dnRemoved;
	}
	
	/**
	 * Establece el elemento en la posici�n pasados por par�metro. Reemplaza el elemento que se encontraba anteriormente en esa posici�n y devuelve el elemento anterior.
	 * @param p Posici�n a establecer el elemento pasado por par�metro.
	 * @param element Elemento a establecer en la posici�n pasada por par�metro.
	 * @return Elemento anterior.
	 * @throws InvalidPositionException si la posici�n es inv�lida o la lista est� vac�a.	 
	 */
	public E set(Position<E> p, E element) throws InvalidPositionException {
		DNode<E> dn = checkPosition(p);
		E elemAnt = dn.element();
		dn.setElement(element);
		return elemAnt;
	}
	
	/**
	 * Devuelve un un iterador de todos los elementos de la lista.
	 * @return Un iterador de todos los elementos de la lista.
	 */
	public Iterator<E> iterator() {
		return (new ElementIterator<E>(this));
	}
	 
	/**
	 * Devuelve una colecci�n iterable de posiciones.
	 * @return Una colecci�n iterable de posiciones.
	 */
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> p = new ListaDoblementeEnlazada<Position<E>>();
		try { 
			if (!isEmpty()) {
				Position<E> pos = first();
				while (pos != last()) {
					p.addLast(pos);
					pos = next(pos);
				}
				p.addLast(pos);
			} 
		} catch (EmptyListException | BoundaryViolationException | InvalidPositionException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return p;
	}
	
	
	//------------------------------Metodo privado--------------------------------------------------
	/**
	 * Chequea que la posicion sea valida.
	 * @param p Posicion a chequear.
	 * @return DNodo Posicion casteada a DNodo.
	 * @throws InvalidPositionException si la posicion es invalida o fue eliminada previamente.
	 * @throws ClassCastException si el DNodo no es una posicion de la lista.	 
	 */
	private DNode<E> checkPosition(Position<E> p) throws InvalidPositionException{
		if (isEmpty())
			throw new InvalidPositionException("checkPosition(): empty list");
		try {
			if (p == null)
				throw new InvalidPositionException("Null position");
			if (p.element() == null)
				throw new InvalidPositionException("Previously deleted position"); 
			return (DNode<E>) p;
		} catch (ClassCastException e) {          
			throw new InvalidPositionException("p doesn't belong to this list"); 
		}
	}

}
