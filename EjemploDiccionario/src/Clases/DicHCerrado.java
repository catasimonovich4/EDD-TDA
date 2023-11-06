package Clases;

import Interfaces.*;
import Exceptions.*;

public class DicHCerrado<K,V> implements Dictionary<K,V> {
	private Entry<K,V>[] a;
	private int size;
	private int capacidad;
	private static final float factor=0.5F;
	private Entry<K, V> disponible = new Entrada<K,V>(null,null);
	
	public DicHCerrado() {
		capacidad = 11;
		a = new Entrada[capacidad];
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public Entry<K,V> find (K key) throws InvalidKeyException{
		if(key==null) 
			throw new InvalidKeyException("Clave nula");
		
		Entry<K,V> aux=null;
		int bucket= hashThisKey(key);
		
		if(a[bucket]!=null) {
			if(a[bucket] != disponible && a[bucket].getKey().equals(key)) {
				aux = a[bucket];
			}else {
				boolean encontre=false;
				for(int i=1; i<capacidad && !encontre; i++) {
					if(a[(bucket+i)%capacidad]!=null ) {
						if(a[(bucket+i)%capacidad] != disponible && a[(bucket+i)%capacidad].getKey().equals(key)) {
							aux=a[(bucket+i)%capacidad];
							encontre=true;
						}
					} else {
						encontre = true;
					}
				}
			}
			
		}
		return aux;
	}
	
	public Iterable<Entry<K,V>> findAll(K key)throws InvalidKeyException{
		if(key==null) {
			throw new InvalidKeyException("Clave nula");
		}
		PositionList<Entry<K,V>> lista= new ListaDoblementeEnlazada<Entry<K,V>>();
		int bucket=hashThisKey(key);
		for(int i=0;i<capacidad;i++) {
			if(a[(bucket+i)%capacidad]!=null && a[(bucket+i)%capacidad]!= disponible ) {
				if( a[(bucket+i)%capacidad].getKey().equals(key)) {
					lista.addLast(a[(bucket+i)%capacidad]);
				}
			}
		}
		return lista;
		
	}
	
	public Entry<K, V> insert(K key, V value)throws InvalidKeyException{
		if(key==null) {
			throw new InvalidKeyException("Clave nula");
		}
		Entrada<K, V> aux=null;
		if(!((size/capacidad)<factor)) {
			reHash();
		}
		int bucket=hashThisKey(key);
		if(a[bucket]==null || a[bucket]== disponible){
			aux= new Entrada<K,V>(key,value);
			a[bucket]=aux;
			size++;
		}else {
			boolean insertado=false;
			for(int i=1; i<capacidad && insertado==false;i++) {
				if(a[(bucket+i)%capacidad]==null || a[(bucket+i)%capacidad]== disponible){
					aux= new Entrada<K,V>(key,value);
					a[(bucket+i)%capacidad]=aux;
					size++;
					insertado=true;
				}
			}
		}
		return aux;
	}
	
	public Entry<K,V> remove(Entry<K,V> e)throws InvalidEntryException{
		Entry<K,V> aux=null;
		try {
			if(e==null || find(e.getKey()) == null) 
				throw new InvalidEntryException("Entrada nula");
			
			int bucket = hashThisKey(e.getKey());

			if(a[bucket]!=null) {
				if(a[bucket]!=disponible && a[bucket]==e) {
					aux=a[bucket];
					a[bucket]=disponible;
					size--;
				}else {
					boolean eliminado=false;
					for(int i=1;i<capacidad && !eliminado;i++) {
						if(a[(bucket+i)%capacidad]!=null) {
							if(a[(bucket+i)%capacidad]!=disponible && a[(bucket+i)%capacidad]==e) {
								aux=a[(bucket+i)%capacidad];
								a[(bucket+i)%capacidad]=disponible;
								size--;
								eliminado=true;
							}
						}
					}}
				}
			} catch (InvalidKeyException e1) {
					e1.printStackTrace();
				}
		return aux;
	}
	
	public Iterable<Entry<K,V>> entries(){
		PositionList<Entry<K,V>> lista=new ListaDoblementeEnlazada<Entry<K,V>>();
		for(Entry<K,V> ent :a) {
			if(ent!=null && ent!=disponible) {
				lista.addLast(ent);
			}
		}
		return lista;
	}
	
	//----------------------------------------------------------------------------------
	
	private int hashThisKey(K key) throws InvalidKeyException {
		if(key==null) {
			throw new InvalidKeyException("Clave nula");
		}
		return Math.abs(key.hashCode()%capacidad);
	}
	
	private void reHash() {
		try {
			Iterable<Entry<K,V>> entradas= entries();
			capacidad=nextPrimo(capacidad);
			size=0;
			a=new Entrada[capacidad];
			for(Entry<K,V> e:entradas) {
				this.insert(e.getKey(), e.getValue());
			}
		} catch (InvalidKeyException e1) {
			e1.getMessage();
		}
	}
	
	private int nextPrimo(int N) {
		int next = N + 1;
		while (!esPrimo(next)) {
			next++;
		}
		return next;
	}
	
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