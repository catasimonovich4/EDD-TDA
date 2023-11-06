package Interfaces;

public interface BTPosition<E> extends Position<E>{
	public E element();
	public BTPosition<E> getParent();
	public BTPosition<E> getLeft();
	public BTPosition<E> getRight();
	public void setElement(E elem);
	public void setParent(BTPosition<E> p);
	public void setLeft(BTPosition<E> l);
	public void setRight(BTPosition<E> r);
}
