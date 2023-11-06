package Classes;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Exceptions.*;
import TDALista.Position;
import TDALista.PositionList;

public class ElementIterator<E> implements Iterator<E> {
	/**
	 * Lista a iterar.
	 */
	protected PositionList<E> list;
	/**
	 * Marcador de la posicion a iterar 
	 */
	protected Position<E> cursor;
	
	/**
	 * Constructor de la clase que inicializa los atributos de instancia.
	 * @param l Lista a insertar en el atributo de instancia list.
	 */
	public ElementIterator(PositionList<E> l) {
		try {
			list = l;
			if (list.isEmpty())
				cursor = null;
			else 
				cursor = list.first();
		} catch (EmptyListException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Controla si hay elementos por visitar en la secuencia.
	 * @return verdadero si hay elementos por visitar y falso en caso contrario.
	 */
	public boolean hasNext() { 
		return cursor!=null;
	}
	
	/**
	 * Devuelve el próximmo elemento a visitar en la secuencia.
	 * @return El elemento a visitar en la secuencia.
	 * @throws NoSuchElementException si no hay un elemento siguiente.
	 */
	public E next() throws NoSuchElementException {
		E toReturn = null;
		try {
			if (cursor == null)
				throw new NoSuchElementException("next(): there's no next");
			toReturn = cursor.element();
			cursor = (cursor == list.last())? null : list.next(cursor);
		} catch (EmptyListException | BoundaryViolationException | InvalidPositionException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return toReturn;
	}
}
