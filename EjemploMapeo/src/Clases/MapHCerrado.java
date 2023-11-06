package Clases;

import Interfaces.*;
import Exceptions.*;

public class MapHCerrado<K,V> implements Map<K,V>{
	private Entrada<K,V> disponible= new Entrada<K,V>(null,null);
	private Entrada<K,V> [] a;
	private int tamaño;
	private int N;
	private static final float factor= 0.5F;
	
	
	public MapHCerrado() {
		a=new Entrada[7];
		tamaño=0;
		N=a.length;
	}
	
	@Override
	public int size() {
		return tamaño;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	@Override
	public V get(K key) throws InvalidKeyException {
		V aux=null;
		if (key==null) {
			throw new InvalidKeyException("Clave nula");
		}
		int bucket=h(key);
		if (a[bucket]!=null) {
			if (a[bucket]!=disponible && a[bucket].getKey().equals(key)) {
				aux=a[bucket].getValue();
			}else{
					boolean salir=false;
					for (int i=1; i<N && !salir ;i++) {
						if (a[(bucket+i)%N]!=null) {
							if (a[(bucket+i)%N]!=disponible && a[(bucket+i)%N].getKey().equals(key)) {
								aux=a[(bucket+i)%N].getValue();
								salir=true;
							}
						}else
							salir=true;
					}
			}
		}
		return aux;	
	}

	@Override
	public V put (K key, V value) throws InvalidKeyException{
		V aux=null;
		if (key==null)
			throw new InvalidKeyException("Clave nula");
		if (!(tamaño/N <factor)){
			reHash();
		}
		Entrada<K,V> nueva=new Entrada<K,V>(key,value);
		if (a[h(key)]==null){
			a[h(key)]=nueva;
			tamaño++;
		}else{
			if(a[h(key)]!=disponible && a[h(key)].getKey().equals(key)){
				aux=a[h(key)].getValue();
				a[h(key)].setValue(value);
			}else{
				boolean existe=false;
				boolean salir=false;
				for (int i=1; i<N && !existe && !salir; i++){
					if (a[(h(key)+i)%N]!=null){
						if(a[(h(key)+i)%N]!=disponible && a[(h(key)+i)%N].getKey().equals(key)){
							existe=true;
							aux=a[(h(key)+i)%N].getValue();
							a[(h(key)+i)%N].setValue(value);
						}
					}else{
						salir=true;
					}
				}
				if (existe!=true){
					for (int i=1; i<N && !existe; i++){
						if (a[(h(key)+i)%N]==null || a[(h(key)+i)%N]==disponible) {
							a[(h(key)+i)%N]=nueva;
							existe=true;
							tamaño++;
						}
					}
				}

			}
		}
		return aux;
	}
	
	@Override
	public V remove(K key) throws InvalidKeyException {
		V aux=null;
		if (key==null) {
			throw new InvalidKeyException("Clave nula");
		}
		boolean eliminado=false;
		if (a[h(key)]!=null) {
			if (a[h(key)]!=disponible && a[h(key)].getKey().equals(key)) {
				aux=a[h(key)].getValue();
				a[h(key)]= disponible;
				eliminado=true;
				tamaño--;
			}
			else {
				for (int i=1; i<N && !eliminado; i++) {
					if (a[(h(key)+i)%N]!=null) {
						if (a[(h(key)+i)%N]!=disponible && a[(h(key)+i)%N].getKey().equals(key)) {
							aux=a[(h(key)+i)%N].getValue();
							a[(h(key)+i)%N]=disponible;
							eliminado=true;
							tamaño--;
						}
					}
					else {
						eliminado=true;
					}
				}
			}
		}
		return aux;
	}

	@Override
	public Iterable<K> keys() {
		PositionList<K> lClaves= new ListaDoblementeEnlazada<K>();
		for (Entrada<K,V> entrada: a) {
			if(entrada!=null && entrada!=disponible) {
				lClaves.addLast(entrada.getKey());
			}
		}
		return lClaves;
	}

	@Override
	public Iterable<V> values() {
		PositionList<V> lValores= new ListaDoblementeEnlazada<V>();
		for (Entrada<K,V> entrada: a) {
			if (entrada!=null && entrada!=disponible) {
				lValores.addLast(entrada.getValue());
			}
		}
		return lValores;
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K,V>> lEntradas= new ListaDoblementeEnlazada<Entry<K,V>>();
		for (Entrada<K,V> entrada: a) {
			if (entrada!=null && entrada!=disponible) {
				lEntradas.addLast(entrada);
			}
		}
		return lEntradas;
	}
	
	/**
	* Retorna el bucket donde almacenar la entrada con clave key
	* @param key Clave de la entrada a almacenar
	* @return Entero correspondiente al bucket donde se insertará la entrada de llave key
	* @throws InvalidKeyException si la clave que pasa por parametro es nula
	*/
	private int h(K key) throws InvalidKeyException{
		int hc;
		if(key==null) {
			throw new InvalidKeyException("Clave nula");
		}else {
			hc= Math.abs(key.hashCode() % N);
		}
		return hc;
	}
	
	
	
	/**
	* Redimensiona la tabla de hash y reorganizar los elementos almacenados en esta
	*/
	private void reHash() {
		try {
			Iterable<Entry<K,V>> entradas= entries();
			N=nextPrimo(N);
			tamaño=0;
			a=new Entrada[N];
			for(Entry<K,V> e:entradas) {
				this.put(e.getKey(), e.getValue());
			}
		} catch (InvalidKeyException e1) {
			e1.getMessage();
		}
	}
	
	/**
	* Metodo para determinar el número primo más cercano a N
	* @param N Número a partir del cual se debe buscar el siguiente primo
	* @return El primer número primo luego de N
	*/
	private int nextPrimo(int N) {
	   int next = N + 1;
	   while (!esPrimo(next)) {
	       next++;
	   }
	   return next;
	}
	
	/**
	* Testea si el número parasado por parametro es primo
	* @param numero Número a consultar si es primo
	* @return Retorna verdadero si numero es primo y falso en caso contrario
	*/
	private static boolean esPrimo(int numero) {
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