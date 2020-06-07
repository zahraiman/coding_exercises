package com.stripe.interview.model;

public class BlockingQueue<T> {
  int size;
  int capacity;
  int head;
  int tail;
  T[] array;

  Object lock  = new Object();

  public BlockingQueue(int capacity) {
    array = (T[]) new Object[capacity];
    this.capacity = capacity;
  }

  public void enqueue(T item) throws InterruptedException {
    synchronized (lock) {
      while (size == capacity) {
        lock.wait();
      }
      if(tail == capacity)
        tail = 0;
      array[tail] = item;
      tail++;
      size++;
      lock.notifyAll();
    }
  }

  public T dequeue() throws InterruptedException {
    T obj = null;
    synchronized (lock) {
      while (size == 0) {
        lock.wait();
      }
      if(head == capacity)
        head = 0;
      obj = array[head];
      array[head] = null;
      head++;
      size--;
      lock.notify();
    }
    return obj;
  }
}
