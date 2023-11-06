package Clases;

import Interfaces.*;
import Exceptions.*;


public class MapHALista<K,V> implements Map<K,V> {
	protected int tamaño;
	protected PositionList<Entrada<K,V>>[] a;
	protected int N;
	protected static final float factor=0.9F;
	
	public MapHALista() {
		tamaño=0;
		N=11;
		a=(PositionList<Entrada<K,V>> []) new ListaDoblementeEnlazada[N];
		for (int i=0; i<N; i++) {
			a[i]=new ListaDoblementeEnlazada<Entrada<K,V>>();
		}
	}
	
	public int size() {
		return tamaño;
	}
	
	public boolean isEmpty() {
		return tamaño==0;
	}
	
	public V put(K key, V value) throws InvalidKeyException {
		checkKey(key);
		V aux=null;
		int bucket=hashThisKey(key);
		boolean insert = false;
		for (Entrada<K,V> e: a[bucket]) {
			if (e.getKey().equals(key)) {
				aux=e.getValue();
				e.setValue(value);
				insert = true;
			}
		}
		if (!insert) {
			a[bucket].addLast(new Entrada<K,V>(key,value));
			tamaño++;			
		}
		if (!(tamaño/N <factor))
			reHash();
		return aux;
	}
	
	public V get(K key) throws InvalidKeyException{
		checkKey(key);
		V aux=null;
		int bucket=hashThisKey(key);
		for (Entrada<K,V> e: a[bucket])
			if (e.getKey().equals(key)) {
				aux=e.getValue();
			}
		return aux;
	}
	
	public V remove(K key) throws InvalidKeyException{
		checkKey(key);
		V aux=null;
		int bucket=hashThisKey(key);
		for (Position<Entrada<K,V>> p: a[bucket].positions()) {
			if (p.element().getKey().equals(key)) {
				try {
					aux=p.element().getValue();
					a[bucket].remove(p);
					tamaño--;
				} catch (InvalidPositionException e) {
					e.printStackTrace();
				}
			}
		}
		return aux;
	}
	
	public Iterable<K> keys(){
		PositionList<K> lclaves= new ListaDoblementeEnlazada<K>();
		for (PositionList<Entrada<K,V>> l: a) {
			for(Entrada<K,V> e: l) {
				lclaves.addLast(e.getKey());
			}
		}
		return lclaves;
	}
	
	public Iterable<V> values(){
		PositionList<V> lvalores=new ListaDoblementeEnlazada<V>();
		for (PositionList<Entrada<K,V>> l: a) {
			for (Entrada<K,V> e: l)
				lvalores.addLast(e.getValue());
		}
		return lvalores;
	}
	
	public Iterable<Entry<K,V>> entries(){
		PositionList<Entry<K,V>> lista=new ListaDoblementeEnlazada<Entry<K,V>>();
		for (PositionList<Entrada<K,V>> l: a) {
			for (Entry<K,V> e: l) {
				lista.addLast(e);
			}
		}
		return lista;
	}
	
	private void checkKey(K k) throws InvalidKeyException{
		if (k==null) {
			throw new InvalidKeyException("Clave nula");
		}
	}
	
	private int hashThisKey(K k) throws InvalidKeyException{
		checkKey(k);
		return Math.abs(k.hashCode() % N);
	}
	
	private void reHash() {
		try {
		Iterable<Entry<K,V>> entradas=entries();
		N=nextPrimo(N);
		tamaño=0;
		a=new PositionList[N];
		for (int i=0; i<N; i++) {
			a[i]= new ListaDoblementeEnlazada<Entrada<K,V>>();
		}
		for (Entry<K,V> e: entradas)
			this.put(e.getKey(), e.getValue());
		} catch (InvalidKeyException e1) {
				e1.printStackTrace();
		}
		
	}
	
	private int nextPrimo(int num) {
		num=num+1;
		while(!esPrimo(num)) {
			num=num+1;
		}
		return num;
	}
	
	private boolean esPrimo(int numero) {
		if (numero <= 1) {
		       return false;
		   }
		   for (int i = 2; i <= Math.sqrt(numero); i++) {
		       if (numero % i == 0) {
		           return false;
		       }
		   }
		   return true;
	}
}	