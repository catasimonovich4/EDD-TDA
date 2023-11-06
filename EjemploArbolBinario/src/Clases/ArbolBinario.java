package Clases;

import Interfaces.*;
import Exceptions.*;
import java.util.Iterator;
import TDALista.ListaDoblementeEnlazada;
import TDALista.PositionList;

public class ArbolBinario<E> implements BinaryTree<E>{
	private int size;
	private BTPosition<E> root;
	
	public ArbolBinario() {
		root = null;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public Iterator<E> iterator() {
		 Iterable<Position<E>> l=this.positions();
		 PositionList<E> lista = new ListaDoblementeEnlazada<E>(); //no cast
		for (Position<E> p: l) {
			lista.addLast(p.element());
		}
		return lista.iterator();
	}
	public Iterable<Position<E>> positions(){
		PositionList<Position<E>> lista = (PositionList<Position<E>>) new ListaDoblementeEnlazada<Position<E>>(); //no cast
		if (size!=0) {
			try {
				preposicion(lista,root);
			}catch(InvalidPositionException e) {
				e.getMessage();
			}
		}
		return lista;
	}
	
	
	
	public E replace(Position<E> v, E e) throws InvalidPositionException{
		BTNode<E> n=checkPosition(v);
		E aux=n.element();
		n.setElement(e);
		return aux;
	}
	
	public Position<E> root() throws EmptyTreeException{
		if (size==0) {
			throw new EmptyTreeException("Árbol vacío");
		}
		return root;
	}
	
	public Position<E> parent(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
		BTNode<E> n=checkPosition(v);
		if (n==root) {
			throw new BoundaryViolationException("La raiz no tiene padre");
		}
		return n.getParent();
	}
	
	public Iterable<Position<E>> children(Position<E> v) throws InvalidPositionException{
		PositionList<Position<E>> lista = (PositionList<Position<E>>) new ListaDoblementeEnlazada<Position<E>>(); //no cast
		BTNode<E> n=checkPosition(v);
		if (this.hasLeft(v)) {
			lista.addLast(n.getLeft());
		}
		if (this.hasRight(v)) {
			lista.addLast(n.getRight());
		}
		return lista;
	}
	
	public boolean isInternal(Position<E> v) throws InvalidPositionException{
		BTNode<E> n=checkPosition(v);
		return (n.getLeft()!=null || n.getRight()!=null);
	}
	
	public boolean isExternal(Position<E> v) throws InvalidPositionException{
		BTNode<E> n= checkPosition(v);
		return (n.getLeft()==null && n.getRight()==null);
	}
	
	public boolean isRoot(Position<E> v) throws InvalidPositionException{
		checkPosition(v);
		Position<E> n=null;
		try {
			n=root();
		}catch(EmptyTreeException e) {
			e.getMessage();
		}
		return v==n;
	}
	
	public boolean hasLeft(Position<E> v) throws InvalidPositionException{
		BTNode<E> n=checkPosition(v);
		return (n.getLeft()!=null);
	}
	
	public boolean hasRight(Position<E> v) throws InvalidPositionException{
		BTNode<E> n=checkPosition(v);
		return (n.getRight()!=null);
	}
	
	public Position<E> left(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
		BTNode<E> n=checkPosition(v);
		if (n.getLeft()==null)
			throw new BoundaryViolationException("No tiene hijo izquierdo");
		return n.getLeft();
	}
	
	public Position<E> right(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
		BTNode<E> n=checkPosition(v);
		if (n.getRight()==null)
			throw new BoundaryViolationException("No tiene hijo derecho");
		return n.getRight();
	}
	
	public Position<E> createRoot(E e) throws InvalidOperationException{
		if (root!=null) {
			throw new InvalidOperationException("El arbol ya tiene raiz");
		}
		BTNode<E> nuevar= new BTNode<E>(e, null, null, null);
		root=nuevar;
		size++;
		return root;	
	}
	
	public Position<E> addLeft(Position<E> v, E e) throws InvalidPositionException, InvalidOperationException{
		BTNode<E> n=checkPosition(v);
		if (n.getLeft()!=null) {
			throw new InvalidOperationException("Ya tiene hijo izquierdo");
		}
		BTPosition<E> nuevohi=new BTNode<E>(e, null,null,n);
		n.setLeft(nuevohi);
		size++;
		return nuevohi;
	}
	
	public Position<E> addRight(Position<E> v, E e) throws InvalidPositionException, InvalidOperationException{
		BTNode<E> n=checkPosition(v);
		if (n.getRight()!=null)
			throw new InvalidOperationException("Ya tiene hijo derecho");
		BTPosition<E> nuevohd=new BTNode<E>(e,null,null,n);
		n.setRight(nuevohd);
		size++;
		return nuevohd;
	}
	
	public E remove(Position<E> v) throws InvalidPositionException, InvalidOperationException{
		BTNode<E> n=checkPosition(v);
		if (n.getLeft()!=null && n.getRight()!=null) {
			throw new InvalidOperationException("Tiene dos hijos");
		}
		BTPosition<E> hijo=null;
		if (n.getLeft()!=null) {
			hijo=n.getLeft();
		}
		if (n.getRight()!=null) {
			hijo=n.getRight();
		}
		if (root==n) {
			if (hijo!=null) {
				hijo.setParent(null);
				size--;
			}
			else {
				root=hijo;
				size--;
			}
		}else {
			BTPosition<E> padre=n.getParent();
			if (padre.getLeft()==n) {
				padre.setLeft(hijo);
				size--;
			}
			if(padre.getRight()==n) {
				padre.setRight(hijo);
				size--;
			}
			if (hijo!=null) {
				hijo.setParent(padre);
			}
		}
		return n.element();
	}
	
	public void attach(Position<E> v, BinaryTree<E> t1, BinaryTree<E> t2) throws InvalidPositionException{
		BTNode<E> n=checkPosition(v);
		if (n.getLeft()!=null || n.getRight()!=null) {
			throw new InvalidPositionException("...");
		}
		try {
			if (!t1.isEmpty()) {
				BTNode<E> r1=checkPosition(t1.root());
				n.setLeft(r1);
				r1.setParent(n);
				size+=t1.size();
			}
			if (!t2.isEmpty()) {
				BTNode<E> r2=checkPosition(t2.root());
				n.setRight(r2);
				r2.setParent(n);
				size+=t2.size();
			}
		}catch(EmptyTreeException e) {
			e.getMessage();
		}
	}
	
	private BTNode<E> checkPosition(Position<E> v) throws InvalidPositionException{
		if (v==null || !(v instanceof BTPosition )) {
			throw new InvalidPositionException("");
		}
		return (BTNode<E>) v;
	}

	private void preposicion(PositionList<Position<E>> l, Position<E> n) throws InvalidPositionException{
		l.addLast(n);
		try {
			if (this.hasLeft(n)) {
				BTNode<E> hi=checkPosition(left(n));
				preposicion(l, hi);
			}
			if (this.hasRight(n)) {
				BTNode<E> hd=checkPosition(right(n));
				preposicion(l,hd);
			}
		}catch(BoundaryViolationException e) {
			e.getMessage();
		}
	}
}
