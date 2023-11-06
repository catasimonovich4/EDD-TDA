package Clases;

import Exceptions.*;
import Interfaces.*;

/** El estado interno del mapeo esta representado con una lista S doblemente enlazada
 * de entradas (clave,valor) donde clave tiene tipo K y valor tiene tipo V.
 */
public class MapeoConLista<K, V> implements Map<K,V> {
	protected ListaDoblementeEnlazada<Entrada<K,V>> s;
	
	//El constructor del mapeo crea una lista de entradas vacia
	public MapeoConLista() {
		s = new ListaDoblementeEnlazada<Entrada<K,V>>();
	}
	
	public int size() {
		return s.size();
	}
 	
	public boolean isEmpty() {
		return s.size() == 0;
	}
	
	public V get(K key) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("get(): invalid key");
		
		//Para cada posicion de p de la lista s hacer:
		for (Position<Entrada<K,V>> p: s.positions()) {
			//Si la clave de la entrada actual es key:
			if (p.element().getKey().equals(key)) 
				//Retornar el valor de la entrada actual:
				return p.element().getValue();
		}
		//Si sali del for-each, quiere decir que no encontre ninguna entrada con clave key
		return null;
	}
	
	public V put(K key, V value) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("put(): invalid key");
		
		//Para cada posicion p de la lista s hacer:
		for(Position<Entrada<K,V>> p: s.positions()) {
			//Si la clave de la entrada en la posicion p es key:
			if (p.element().getKey().equals(key)) {
				//Salvar el valor de la entrada en aux
				V aux = p.element().getValue();
				//Setear el nuevo valor de la entrada a value
				p.element().setValue(value);
				//Retornar el viejo valor
				return aux;
			}
		}
		//Si sali del for-each entonces no encontre una entrada con clave key
		s.addLast(new Entrada<K,V>(key,value));
		return null;
	}
	
	public V remove(K key) throws InvalidKeyException {
		try {
				if (key == null) 
					throw new InvalidKeyException("remove(): invalid key");
				
				for (Position<Entrada<K,V>> p: s.positions()) {
					if (p.element().getKey() == key) {
						V aux = p.element().getValue();
						s.remove(p);
						return aux;
					}
				}
		} catch (InvalidPositionException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null; 
	}
	
	public Iterable<K> keys() {
		PositionList<K> listaKeys = new ListaDoblementeEnlazada<K>();
		for (Position<Entrada<K,V>> p: s.positions()) {
			listaKeys.addLast(p.element().getKey());
		}
		return listaKeys;
	}
	
	public Iterable<V> values() {
		PositionList<V> listaValues = new ListaDoblementeEnlazada<V>();
		for (Position<Entrada<K,V>> p: s.positions()) {
			listaValues.addLast(p.element().getValue());
		}
		return listaValues;
	}
	
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> listaEntries = new ListaDoblementeEnlazada<Entry<K,V>>();
		for (Position<Entrada<K,V>> p: s.positions()) {
			listaEntries.addLast(p.element());
		}
		return listaEntries;
	}
}
