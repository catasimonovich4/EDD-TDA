package Clases;
import Interfaces.*;
import Exceptions.*;

public class DiccionarioConLista<K, V> implements Dictionary<K,V>{
	protected ListaDoblementeEnlazada<Entry<K,V>> d;
	
	public int size() {
		return d.size();
	}
	
	public boolean isEmpty() {
		return d.size() == 0;
	}
	
	public Entry<K,V> find(K key) throws InvalidKeyException {
		Entry<K,V> retornar = null;
		if (key == null) 
			throw new InvalidKeyException("find(): invalid key");
		try {	
				boolean encontre = false;
				Position<Entry<K,V>> p;
				if (!d.isEmpty()) {
					p = d.first();
					while (p != null && !encontre) {
						if (p.element().getKey().equals(key)) {
							encontre = true;
							retornar = p.element();
						} else {
								 if (p != d.last())
									 p = d.next(p);
								 else 
									 p = null;
						}
					}
				}
		} catch (EmptyListException | BoundaryViolationException | InvalidPositionException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return retornar;
	}
	
	public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("findAll(): invalid key");
		PositionList<Entry<K,V>> l = new ListaDoblementeEnlazada<Entry<K,V>>();
		for (Position<Entry<K,V>> p: d.positions()) {
			if (p.element().getKey().equals(key)) {
				l.addLast(p.element());
			}
		}
		return l;
	}
	
	public Entry<K,V> insert(K key, V value) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("insert(): invalid key");
		Entrada<K,V> e = new Entrada<K,V>(key,value);
		d.addLast(e);
		return e;
	}
	
	//Consultar. En las diapositivas esta hecho con un for-each para d.positions() 
	public Entry<K,V> remove(Entry<K,V> e) throws InvalidEntryException {
		Entry<K,V> retorno = null;
		try {
				if (e == null || find(e.getKey()) == null) 
					throw new InvalidEntryException("remove(): invalid entry");
				else {
						if (!d.isEmpty()) {
							boolean encontre = false;
							Position<Entry<K,V>> p = d.first();
							while (p != null && !encontre) {
								if (p.element() == e) {
									encontre = true;
									d.remove(p);
								} else {
										 if (p != d.last()) 
											 p = d.next(p);
										 else
											 p = null;
								}
							}
						}
				}
		} catch (InvalidKeyException | EmptyListException | InvalidPositionException | BoundaryViolationException error) {
			System.out.println("Error: " + error.getMessage());
		}
		return retorno;
	}
	
	public Iterable<Entry<K,V>> entries() {
		return d;
	}
}
