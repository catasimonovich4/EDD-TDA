package Clases;

import java.util.Iterator;
import Exceptions.*;
import Interfaces.*;

/**
 * Clase DictHashAbierto que implementa la interfaz Dictionary.
 * @author Catalina Simonovich y Valentina Beron.
 */
public class DicHALista<K, V> implements Dictionary<K,V> {
	/**
	 * Arreglo de listas que almacenan entradas.
	 */
	protected PositionList<Entrada<K,V>> [] arregloBucket;
	/**
	 * Tamaño o numero de entradas del diccionario.
	 */
	protected int nDic; 											 
	/**
	 * Tamaño del arreglo de listas.
	 */
	protected int N;					
	/**
	 * Factor de carga para mantener la distribucion uniforme de las entradas del arreglo.
	 */
	protected static final float factor = 0.9f;					 
	
	/**
	 * Constructor de clase que crea un arreglo de Listas Doblemente Enlazadas vacias.
	 */
	public DicHALista() {
		N = 11;
		arregloBucket = (PositionList<Entrada<K,V>> []) new ListaDoblementeEnlazada[N];
		for (int i=0; i<N; i++) {
			arregloBucket[i] = new ListaDoblementeEnlazada<Entrada<K,V>>();
		}
		nDic = 0; 
	}
	
	
	/**
	 * Consulta el numero de entradas del diccionario.
	 * @return Numero de entradas del diccionario.
	 */
	public int size() {
		return nDic;
	}
	
	/**
	 * Consulta si el diccionario est� vac�o.
	 * @return Verdadero si el diccionario est� vac�o, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return nDic == 0;
	}
	
	/**
	 * Busca una entrada con clave igual a una clave dada y la devuelve, si no existe retorna nulo.
	 * @param key Clave a buscar.
	 * @return Entrada encontrada.
	 * @throws InvalidKeyException si la clave pasada por par�metro es inv�lida.
	 */
	public Entry<K,V> find(K key) throws InvalidKeyException {
		Entry<K,V> retornar = null;
		if (key == null)
			throw new InvalidKeyException("find(): invalid key");
		try {
			boolean encontre = false;
			int i = hashK(key);
			PositionList<Entrada<K,V>> bucket = arregloBucket[i];
			Position<Entrada<K,V>> p;
			Position<Entrada<K,V>> entrada = null;
			if (bucket.isEmpty())
					p = null;
			else { 
					p = bucket.first();
					while (p != null && !encontre) {
						if (p.element().getKey().equals(key)) {
							encontre = true;
							entrada = p;	
						} else {
							if (p != bucket.last())
								p = bucket.next(p);
							else 
								p = null;
						}	
					}
			}
			if(entrada != null)
				retornar = (Entry<K,V>) entrada.element();
		} catch ( EmptyListException | BoundaryViolationException | InvalidPositionException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return retornar;
	}
	
	/**
	 * Retorna una colecci�n iterable que contiene todas las entradas con clave igual a una clave dada.
	 * @param key Clave de las entradas a buscar.
	 * @return Colecci�n iterable de las entradas encontradas.
	 * @throws InvalidKeyException si la clave pasada por par�metro es inv�lida.
	 */
	public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
		PositionList<Entry<K,V>> l = new ListaDoblementeEnlazada<Entry<K,V>>();
		if (key == null) 
			throw new InvalidKeyException("findAll(): null key");
		else {
				for (Entrada<K,V> p : arregloBucket[hashK(key)]) {
					if (p.getKey().equals(key)) 
						l.addLast(p);
				}
		}
		return l; 
	}

	/**
	 * Inserta una entrada con una clave y un valor dado en el diccionario y retorna la entrada creada.
	 * @param key Clave de la entrada a crear.
	 * @return value Valor de la entrada a crear.
	 * @throws InvalidKeyException si la clave pasada por par�metro es inv�lida.
	 */
	public Entry<K,V> insert(K key, V value) throws InvalidKeyException {
		Entrada<K,V> e;
		if (key == null)
			throw new InvalidKeyException("insert(): null key");
		else {
			e = new Entrada<K,V>(key,value);
			arregloBucket[hashK(key)].addLast(e);
			nDic++;
			if (nDic/N > factor) 
				reHash();
		}
		return e;
	}
	

	/**
	 * Remueve una entrada dada en el diccionario y devuelve la entrada removida.
	 * @param e Entrada a remover.
	 * @return Entrada removida.
	 * @throws InvalidEntryException si la entrada no est� en el diccionario o es inv�lida.
	 */
	public Entry<K,V> remove(Entry<K,V> e) throws InvalidEntryException {
		Entry<K,V> aux = null;
		try {
			if (e == null || find(e.getKey()) == null) { 
				throw new InvalidEntryException("remove(): invalid entry");
			} else {
				boolean encontre = false;
				int bucket = hashK(e.getKey());
				Iterable<Position<Entrada<K,V>>> bpos = arregloBucket[bucket].positions();
				Iterator<Position<Entrada<K, V>>> bIt = bpos.iterator();
				while (bIt.hasNext() && !encontre) {
					Position<Entrada<K,V>> pos = bIt.next();
					if(pos.element().equals(e)) {
						encontre = true;
						aux = arregloBucket[bucket].remove(pos);
						nDic--;
					}
				} 
			}
		} catch (InvalidPositionException | InvalidKeyException error) {
			System.out.println("Error: " + error.getMessage());
		}
		return aux;
	}
	
	/**
	 * Retorna una colecci�n iterable con todas las entradas en el diccionario.
	 * @return Colecci�n iterable de todas las entradas.
	 */
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> listaEntradas = new ListaDoblementeEnlazada<Entry<K,V>>();
		for(PositionList<Entrada<K,V>> bucket : arregloBucket) {
			for(Entrada<K,V> pos : bucket){
				listaEntradas.addLast(pos);
			}
		}
		return listaEntradas;
	}
	
	
	//------------------------Metodos privados--------------------------------------------------
	/**
	 * Retorna la posicion del arreglo en donde se encontrara la lista para almacenar la entrada con clave key.	
	 * @param key Clave de la entrada a almacenar.
	 * @return Entero correspondiente al bucket donde se insertará la entrada de llave key.
	 * @throws InvalidKeyException si la clave que pasa por parametro es nula.
	 */
	private int hashK(K key) throws InvalidKeyException{
		int hcode;
		if (key==null) {
			throw new InvalidKeyException("hashK(): null key");
		} else {
			hcode = Math.abs(key.hashCode() % N);
		}
		return hcode;
	}
	
	/**
	 * Aumenta las dimensiones de la tabla de hash y vuelve a colocar los elementos antes almacenados.
	 */
	private void reHash() {
		try {
			Iterable<Entry<K,V>> entradas= entries();
			N = nextPrimo(N);
			nDic = 0;
			arregloBucket = new PositionList[N];
			for(int i=0;i<N;i++) {
				arregloBucket[i] = new ListaDoblementeEnlazada<Entrada<K,V>>();
			}
			for(Entry<K,V> entries: entradas) {
				this.insert(entries.getKey(), entries.getValue());
			}
		} catch (InvalidKeyException e) {
				System.out.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Determina el número primo siguiente más cercano a N.
	 * @param N Número al que se le debe buscar el siguiente primo.
	 * @return El primer número primo luego de N.
	 */
	private int nextPrimo(int N) {
	    int next = N + 1;
	    while (!esPrimo(next)) {
	        next++;
	    }
	    return next;
	}
	
	/**
	 * Determina si el número pasado por parametro es primo.
	 * @param numero Número a consultar si es primo.
	 * @return Retorna verdadero si numero es primo y falso si no lo es.
	 */
	private static boolean esPrimo(int numero) {
		boolean primo = true;
		if (numero <= 1) {
			primo = false;
		}
		for (int i = 2; i <= Math.sqrt(numero); i++) {
			if (numero % i == 0) {
				primo = false;
			}
		}
		return primo;
	}
	
}
