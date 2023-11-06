package Clases;

import Interfaces.*;

public class BTNode<E> implements BTPosition<E> {
	private E element;
	private BTPosition<E> left, right, parent;
	
	public BTNode(E element, BTPosition<E> left, BTPosition<E> right, BTPosition<E> parent) {
		this.element = element;
		this.parent = parent;
		this.left = left;
		this.right = right;
	}
	
	public E element() {
		return element;
	}
	
	public BTPosition<E> getLeft() {
		return left;
	}
	
	public BTPosition<E> getRight() {
		return right;
	}
	
	public BTPosition<E> getParent() {
		return parent;
	}
	
	public void setElement(E elem) {
		element = elem;
	}
	
	public void setLeft(BTPosition<E> l) {
		left = l;
	}
	
	public void setRight(BTPosition<E> r) {
		right = r;
	}
	
	public void setParent(BTPosition<E> p) {
		parent = p;
	}
}
