package Clases;
import Interfaces.*;
import Exceptions.*;

public class DicHAbierto<K, V> implements Dictionary<K,V>{
	protected Dictionary<K,V> [] A; 
	protected int n;								//Cantidad de entradas del diccionario
	protected int N;								//Tamaño del arreglo
	protected static final float factor = 0.9f;
	
	public DicHAbierto() {
		N = 11;
		A = (Dictionary<K,V> []) new DiccionarioConLista[N];
		for (int i = 0; i<N; i++) 
			A[i] = new DiccionarioConLista<K,V>();
		n = 0;
	}
	
	//Consulta el numero de entradas del diccionario.
	public int size() {
		return n;
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public Entry<K,V> find(K key) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("find(): null key");
		return A[hashKey(key)].find(key);
	}
	
	public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("findAll(): null key");
		return A[hashKey(key)].findAll(key);
	}
	
	public Entry<K,V> insert(K key, V value) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("insert(): null key");
		int i = hashKey(key);
		Entry<K,V> entradaNueva = A[i].insert(key, value);
		if (entradaNueva == null) 
			n++;
		if (!(n/N < factor)) 
			reHash();
		return entradaNueva;
	}
	
	public Entry<K,V> remove(Entry<K,V> e) throws InvalidEntryException {
		Entry<K,V> eRemovida = null;
		try { 
			if (e == null || find(e.getKey()) == null) 
				throw new InvalidEntryException("remove(): invalid entry");
			int i = hashKey(e.getKey());
			eRemovida = A[i].remove(e);
		} catch (InvalidKeyException error) {
			System.out.println("Error: " + error.getMessage());
		}
		return eRemovida;
	}
	
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> listaEntries = new ListaDoblementeEnlazada<Entry<K,V>>();
		for (int i =0; i < N; i++) {
			for (Entry<K,V> e: A[i].entries())
				listaEntries.addLast(e);
		}
		return listaEntries;
	}
	
	
	//--------------------------------------------------------------------------------------------
	private int hashKey(K key) throws InvalidKeyException {
		 if (key == null)
			 throw new InvalidKeyException("hashKey(): null key");
		 return Math.abs(key.hashCode() % N);
	}
	
	private void reHash() {
		try {
			Iterable<Entry<K,V>> entradas = entries();
			N = nextPrimo(N);
			n = 0;
			A = new DiccionarioConLista[N];
			for (int i = 0; i < N; i++) 
				A[i] = new DiccionarioConLista<K,V>();
			for (Entry<K,V> e: entradas) 
				this.insert(e.getKey(), e.getValue());
		} catch (InvalidKeyException error) {
			System.out.println("Error: " + error.getMessage());
		}
	}
	
	private int nextPrimo(int num) {
		int next = num++;
		while (!esPrimo(num))
			next++;
		return next;
	}
	
	private boolean esPrimo(int num) {
		boolean primo = true;
		if (num <= 1)
			primo = false;
		else {
			for (int i = 2; i < Math.sqrt(num); i++) {
				if(num % i == 0)
					primo = false;
			}
		}
		return primo;
	}
	
	
}
