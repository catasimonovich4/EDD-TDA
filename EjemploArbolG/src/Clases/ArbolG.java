package Clases;

import Interfaces.Position;
import Interfaces.Tree;

import java.util.Iterator;
import Exceptions.*;
import TDALista.ListaDoblementeEnlazada;
import TDALista.PositionList;


public class ArbolG<E> implements Tree<E>{
	protected int size;
	protected TNodo<E> raiz;
	
	public ArbolG() {
		size = 0;
		raiz = null;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> l = new ListaDoblementeEnlazada<Position<E>>();
		if (!isEmpty()) 
			pre(raiz, l);
		return l;
	}
	
	public Iterator<E> iterator() {
		Iterable<Position<E>> it = positions();
		PositionList<E> iterator = new ListaDoblementeEnlazada<E>();
		for (Position<E> pos: it) {
			iterator.addLast(pos.element());
		}
		return iterator.iterator();
	}
	
	public E replace(Position<E> p, E e) throws InvalidPositionException {
		TNodo<E> nodo = checkPosition(p);
		E rotulo = nodo.element();
		nodo.setElement(e);
		return rotulo;
	}
	
	public Position<E> root() throws EmptyTreeException {
		if (isEmpty()) 
			throw new EmptyTreeException("root(): empty tree");
		return raiz;
	}
	
	public Position<E> parent(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		TNodo<E> nodo = checkPosition(p);
		if (nodo == raiz) 
			throw new BoundaryViolationException("parent(): el nodo es una raiz");
		return nodo.getPadre();
	}
	
	public Iterable<Position<E>> children(Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = checkPosition(p);
		PositionList<Position<E>> listaHijos = new ListaDoblementeEnlazada<Position<E>>();
		for (Position<E> hijo: nodo.getHijos())
			listaHijos.addLast(hijo);
		return listaHijos;
	}
	
	public boolean isInternal(Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = checkPosition(p);
		return !nodo.getHijos().isEmpty();	//Pregunto si NO esta vacio
	}
	
	public boolean isExternal(Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = checkPosition(p);
		return nodo.getHijos().isEmpty();	//Pregunto si esta vacio
	}
	
	public boolean isRoot(Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = checkPosition(p);
		return nodo.element() == raiz;
	}
	
	public void createRoot(E e) throws InvalidOperationException {
		if (!isEmpty()) 
			throw new InvalidOperationException("createRoot(): ya existe raiz");
		raiz = new TNodo<E>(e,null);
		size++;
	}
	
	public Position<E> addFirstChild(Position<E> p, E e) throws	InvalidPositionException {
		if (isEmpty()) 
			throw new InvalidPositionException("addFirst(): emtpy tree");
		TNodo<E> nodo = checkPosition(p);
		TNodo<E> nuevo = new TNodo<E>(e, nodo);
		nodo.getHijos().addFirst(nuevo);
		size ++;
		return nuevo;
	}
	
	public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException {
		if (isEmpty())
			throw new InvalidPositionException("addLast(): empty tree");
		TNodo<E> nodo = checkPosition(p);
		TNodo<E> nuevo = new TNodo<E>(e, nodo);
		nodo.getHijos().addLast(nuevo);
		size++;
		return nuevo;
	}
	
	//import tdalista.Position;           //Necesito usar posiciones de lista por lista de hijos
	public Position<E> addBefore(Position<E> p, Position<E> rb, E e) throws InvalidPositionException {
		if (isEmpty()) 
			throw new InvalidPositionException("addBefore(): empty tree");
		TNodo<E> padre = checkPosition(p);
		TNodo<E> hermanoDerecho = checkPosition(rb);
		TNodo<E> nuevo = new TNodo<E>(e, padre);
		if (hermanoDerecho.getPadre() != padre) 
			throw new InvalidPositionException("p no es padre de rb");
		try {
			//Buscar donde esta rb en la lista de hijos de p 
			boolean encontre = false;
			//TDALista.Position<TNodo<E>
			Position<TNodo<E>> primero = padre.getHijos().first(); 
			while (primero != null && !encontre) {
				//Testeo si el elemento corriente de la lista de hijos de p es rb
				if (primero.element() == hermanoDerecho) //Si, es! => termine el bucle
					encontre = true;
				else {									//No es, avanzo
					if (primero != padre.getHijos().last())			//Si primero no es el ultimo, next, sino null
						primero = padre.getHijos().next(primero);
					else
						primero = null;
				}
			}
			if (encontre) {
				padre.getHijos().addBefore(primero, nuevo);
				size++;
			}
		}  catch (EmptyListException | InvalidPositionException | BoundaryViolationException e1) {
			e1.printStackTrace();
		}
		return nuevo;
	}
	
	public Position<E> addAfter (Position<E> p, Position<E> lb, E e) throws InvalidPositionException {
		if (isEmpty())
			throw new InvalidPositionException("empty tree");
		TNodo<E> padre = checkPosition(p);
		TNodo<E> hermanoIzquierdo = checkPosition(lb);
		TNodo<E> nuevo = new TNodo<E>(e, padre);
		if (hermanoIzquierdo.getPadre() != padre)
			throw new InvalidPositionException("p no es el padre de lb");
		try {
			//TDALista.Position<TNodo<E>> 
			Position<TNodo<E>> primero = padre.getHijos().first();
			boolean encontre = false;
			while (primero != null && !encontre) {
				if (primero.element() == hermanoIzquierdo) 
					encontre = true;
				else {
					if (primero != padre.getHijos().last())
						primero = padre.getHijos().next(primero);
					else 
						primero = null;
				}
			}
			if (encontre) {
				padre.getHijos().addAfter(primero, nuevo);
				size++;
			}
		} catch (EmptyListException | InvalidPositionException | BoundaryViolationException e1) {
			e1.printStackTrace();
		}
		return nuevo;
	}
	
	public void removeExternalNode (Position<E> p) throws InvalidPositionException {
		if (size == 0)
			throw new InvalidPositionException("Arbol vacio");
		if (isInternal(p))
			throw new InvalidPositionException("El nodo es interno");
		if (isExternal(p))
			removeNode(p);
	}
	
	public void removeInternalNode (Position<E> p) throws InvalidPositionException {
		if (isEmpty())
			throw new InvalidPositionException("arbol vacio");
		if (isExternal(p))
			throw new InvalidPositionException("el nodo es externo");
		if (isInternal(p)) {
			TNodo<E> nodo = checkPosition(p);
			if (nodo == raiz) {
				if (nodo.getHijos().size() > 1)
					throw new InvalidPositionException("raiz con mas de un hijo");
				else 
					removeNode(p);
			} else
				removeNode(p);
		}
	}
	
	public void removeNode (Position<E> p) throws InvalidPositionException {
		try { 
			if (size == 0)	//Chequea que el arbol no este vacio
				throw new InvalidPositionException("arbol vacio");
			TNodo<E> n = checkPosition(p);	//Chequea el nodo que queremos eliminar
			if (n == raiz) {	//Pregunta si el nodo es la raiz
				if (n.getHijos().size() == 1) {		//Si es la raiz, pregunta si tiene un hijo
					Position<TNodo<E>> newRoot;
					newRoot = n.getHijos().first();  //Toma la posicion de ese unico hijo
					raiz = newRoot.element();		 //La raiz del arbol ahora es la raiz nueva
					raiz.setPadre(null);			 //La referencia al nodo que queremos eliminar sigue estando como padre del nodo que hicimos raiz, la eliminamos
					size--;
				} else { 
					if (size() == 1) {       //No tenia un unico hijo, preguntamos si no tiene hijos
						raiz = null;
						size--;
					} else 
						throw new InvalidPositionException("No es hoja, tiene mas de un hijo");
				}
			} else {  //Si no es una raiz	
				TNodo<E> padre = n.getPadre();	//Obtengo el padre del nodo a eliminar
				//No agregar casteos cuando copie en papel
				PositionList<TNodo<E>> hijosDeN = (PositionList<TNodo<E>>) n.getHijos();  //Obtengo los hijos del nodo a eliminar
				PositionList<TNodo<E>> hijosDelPadre = (PositionList<TNodo<E>>) padre.getHijos();  //Obtengo los hijos del padre del nodo a eliminar
				Position<TNodo<E>> primero = (Position<TNodo<E>>) hijosDelPadre.first();  //Obtengo el primer hijo de los hijos del padre del nodo a eliminar
				while (primero.element() != n) {
					primero = hijosDelPadre.next(primero);
				}
				while (!hijosDeN.isEmpty()) {
					Position<TNodo<E>> hijoAInsertar = hijosDeN.first();
					hijosDelPadre.addBefore(primero, hijoAInsertar.element());
					hijoAInsertar.element().setPadre(padre);
					hijosDeN.remove(hijoAInsertar);
				}
				hijosDelPadre.remove(primero);
				size--;
			}
			
		} catch (EmptyListException | InvalidPositionException | BoundaryViolationException e) {
			throw new InvalidPositionException("Hay corruptos");
		}
	}
	
	
	
//----------------------Metodos privados--------------------------------------------------
	//Pre-orden
	private void pre(TNodo<E> nodo, PositionList<Position<E>> l) {
		l.addLast(nodo);   //Visita
		for (TNodo<E> hijo: nodo.getHijos())
			pre(hijo, l);
	}
	
	private TNodo<E> checkPosition(Position<E> p) throws InvalidPositionException {
		try {
			if (p == null)
				throw new InvalidPositionException("chekP(): null position");
			return (TNodo<E>) p;
		} catch (ClassCastException e) {
			throw new InvalidPositionException("Clase casteada no es de tipo Position");
		}
	}
} 
