package Classes;

import java.util.Iterator;
import TDALista.*;
import Exceptions.*;

public class ListaSimplementeEnlazada<E> implements PositionList<E> {
	protected Nodo<E> first;
	protected int size;
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Position<E> first() throws EmptyListException {
		if(size == 0)
			throw new EmptyListException("Lista vacia.");
		return first;
	}

	public Position<E> last() throws EmptyListException {
		Nodo<E> aux;
		if(size == 0)
			throw new EmptyListException("Lista vacia.");
		aux = first;
		while(aux.getSiguiente() != null) {
			aux = aux.getSiguiente();
		}
		return aux;
	}

	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		Nodo<E> nodo = checkPosition(p);
		if(nodo.getSiguiente() == null)
			throw new BoundaryViolationException("La posicion corresponde al utlimo nodo de la lista.");
		return nodo.getSiguiente();
	}

	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		Nodo<E> nodo = checkPosition(p);
		Nodo<E> aux;
		if(nodo.element() == first.element())
			throw new BoundaryViolationException("La posicion corresponde al primer nodo de la lista.");
		aux = first;
		if(aux != null) {
			while(aux.getSiguiente() != null && aux.getSiguiente() != nodo) {
				aux = aux.getSiguiente();
			}
		}
		return aux;
	}

	public void addFirst(E element) {
		first = new Nodo<E>(element, first);
		size++;
	}

	public void addLast(E element) {
		Nodo<E> aux;
		if(size == 0)
			first = new Nodo<E>(element);
		else {
			aux = first;
			while(aux.getSiguiente() != null) {
				aux = aux.getSiguiente();
			}
			aux.setSiguiente(new Nodo<E>(element));
		}
		size++;
	}

	public void addAfter(Position<E> p, E element) throws InvalidPositionException {
		Nodo<E> nodo = checkPosition(p);
		Nodo<E> insert = new Nodo<E>(element, nodo.getSiguiente());
		nodo.setSiguiente(insert);
		size++;
	}

	public void addBefore(Position<E> p, E element) throws InvalidPositionException {
		Nodo<E> nodo = checkPosition(p);
		Nodo<E> insert;
		Nodo<E> prev;
		try {
			if(nodo == first) {
				addFirst(element);
			}
			else {
				insert = new Nodo<E>(element, nodo);				
				prev = (Nodo<E>) prev(p);
				prev.setSiguiente(insert);
				size++;
			}
		}
		catch (BoundaryViolationException e) {
			System.out.println(e.getMessage());
		}
	}

	public E remove(Position<E> p) throws InvalidPositionException {
		if(isEmpty()) {
			throw new InvalidPositionException("Lista vacia");
		}
		Nodo<E> remove = checkPosition(p);
		Nodo<E> previo;
		E elem;
		try {
			if(remove == first) {
				first = first.getSiguiente();
			}
			else {
				previo = (Nodo<E>) prev(remove);
				previo.setSiguiente(remove.getSiguiente());				
			}
			size--;
		}
		catch(BoundaryViolationException e) {
			System.out.println(e.getMessage());
		}
		elem = remove.element();
		remove.setElement(null);
		return elem;
	}

	public E set(Position<E> p, E element) throws InvalidPositionException {
		if(isEmpty()) {
			throw new InvalidPositionException("Lista vacia");
		}
		Nodo<E> set = checkPosition(p);
		E remove = set.element();
		set.setElement(element);
		return remove;
	}

	public Iterator<E> iterator() {
		return new ElementIterator<E>(this);
	}

	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> toReturn = new ListaSimplementeEnlazada<Position<E>>();
		Nodo<E> actual = first;
		while (actual != null) {
			toReturn.addLast(actual);
			actual = actual.getSiguiente();
		}
		return toReturn;
	}
	
	private Nodo<E> checkPosition(Position<E> p) throws InvalidPositionException {
		try {
			if( p == null )
				throw new InvalidPositionException("Posicion nula");
			if( p.element() == null )
				throw new InvalidPositionException("Posicion eliminada previamente");
			return (Nodo<E>) p;
		} catch (ClassCastException e) {
			throw new InvalidPositionException("Posicion no es de tipo Nodo E");
		}
	}

	/**
	 * Clase Nodo anidada, permite evitar modificar la lista mediante casting
	 * explicito desde la clase cliente.
	 * Cliente no debe conocer como funciona la estructura, solamente debe saber
	 * que hace.
	 * @param <E> generico.
	 */
	private class Nodo<E> implements Position<E> {
		private E element;
		private Nodo<E> siguiente;

		public Nodo(E element, Nodo<E> siguiente) {
			this.element = element;
			this.siguiente = siguiente;
		}

		public Nodo(E element) {
			this(element, null);
		}

		public E element() {
			return element;
		}

		public void setElement(E element) {
			this.element = element;
		}

		public Nodo<E> getSiguiente() {
			return siguiente;
		}

		public void setSiguiente(Nodo<E> siguiente) {
			this.siguiente = siguiente;
		}
	}
}