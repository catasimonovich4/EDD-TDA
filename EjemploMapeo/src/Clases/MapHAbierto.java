package Clases;
import Interfaces.*;
import Exceptions.*;

public class MapHAbierto<K, V> implements Map<K,V>{
	protected Map<K,V> [] A; 	 //Arreglo de listas
	protected int n; 	//Cantidad de entradas
	protected int N;   	//Size del arreglo
	protected static final float factor = 0.9F;   //Factor de carga 
	
	public MapHAbierto() {
		N = 11;
		A = (Map<K,V>[]) new MapeoConLista[N];
		for (int i = 0; i < N; i++) 
			A[i] = new MapeoConLista<K,V>();
		n = 0;
	}
	
	//Consulta el numero de entradas del mapeo.
	public int size() {
		return n;
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public V get(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("get(): null key");
		return A[hashThisKey(key)].get(key);    //Delega en el get del mapeo implementado con lista.
	}
	
	public V put(K key, V value) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("put(): null key");
		V val = A[hashThisKey(key)].put(key, value);   //Delega en el put del mapeo implementado con lista
		if (val == null)     	//K es una nueva clave
			n++;			 	//Incremento la cantidad de entradas en el mapeo
		if (!(n/N < factor))	//Si se supera el factor de carga luego de agregar una nueva entrada, debemos hacer un rehash
			reHash();
		return val;
	}
	
	public V remove(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("remove(): null key");
		V val = A[hashThisKey(key)].remove(key);	//Delega en el mapeo implementado con lista
		if (val != null) 		//k es una nueva clave
			n--;				//decremento la cantidad de entradas del mapeo
		return val;
	}
	
	public Iterable<K> keys() {
		PositionList<K> listaKeys = new ListaDoblementeEnlazada<K>();
		for (int i = 0; i < A.length; i++) {
			 for (K k: A[i].keys())
				 listaKeys.addLast(k);
		}
		return listaKeys;
	}
	
	public Iterable<V> values() {
		PositionList<V> listaValues = new ListaDoblementeEnlazada<V>();
		for (int i = 0; i < A.length; i++) {
			for (V v: A[i].values())
				listaValues.addLast(v);
		}
		return listaValues;
	}
	
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> listaEntries = new ListaDoblementeEnlazada<Entry<K,V>>();
		for (int i = 0; i < A.length; i++) {
			for (Entry<K,V> e: A[i].entries()) 
				listaEntries.addLast(e);
		}
		return listaEntries; 
	}
	 
	//-----------------------------------------------------------------------------------------------------------------------------
	private int hashThisKey(K key) throws InvalidKeyException {
		if (key == null) 
			throw new InvalidKeyException("hashKey(): invalid key");
		return Math.abs(key.hashCode() % N);	//Por si acaso pasan un negativo
	}
	
	
	private void reHash() {
		try {			
			Iterable<Entry<K,V>> entradas = entries();
			N = nextPrimo(N);
			n = 0;
			A = new MapeoConLista[N];
			for (int i=0; i<N; i++) {
				A[i] = new MapeoConLista<K,V>();
			}
			for (Entry<K,V> e: entradas) {
				this.put(e.getKey(), e.getValue());
			}
		} catch (InvalidKeyException error) {
			System.out.println("Error: " + error.getMessage());
		}
	}
	
	private int nextPrimo(int num) {
		int next = num++;
		while (!esPrimo(next)) {
			next++;
		}
		return next;
	}
	
	private boolean esPrimo(int num) {
		boolean primo = true;
		if (num <= 1)
			primo = false;
		for (int i=2; i <= Math.sqrt(num); i++) {
			if (num % i == 0)
				primo = false;
		}
		return primo;
	}
}
