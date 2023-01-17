package edu.ust.cisc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class CiscTreeMap<K extends Comparable<K>, V> implements Map<K, V> {

    private CiscEntry<K, V> root;
    private int size;
    private V retValue;


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(root, (K)key);
    }

    private boolean containsKey(CiscEntry<K, V> node, K key){
        if(node == null){
            return false;
        }else{
            int compare = key.compareTo(node.key);
            if(compare == 0){
                return true;
            }else if(compare < 0){
                return containsKey(node.left, key);
            }else{
                return containsKey(node.right, key);
            }
        }
    }

    @Override
    public boolean containsValue(Object value) {
        ArrayList<V> newlist = (ArrayList<V>) values();
        int i = 0;
        boolean tf = false;
        while(i < newlist.size()){
            if(newlist.get(i).equals(value)){
                tf = true;
            }
            i++;
        }
        return tf;
    }

    @Override
    public V get(Object key){
        return get(root, (K)key);
    }
    private V get(CiscEntry<K, V> node, K key) {
        if(node == null){
            return null;
        }else{
            int compare = key.compareTo(node.key);
            if(compare == 0){
                return node.value;
            }else if(compare < 0){
                return get(node.left, key);
            }else{
                return get(node.right, key);
            }

        }
       // return null;
    }
    @Override
    public V put(K key, V value) {
        root = put(root, key, value);
        return retValue;
    }
    private CiscEntry<K, V> put(CiscEntry<K, V> node, K key, V value){
        if(node == null){
            node = new CiscEntry<>(key, value);
            this.size++;
            retValue = null;
        }else if(node.key.compareTo(key) > 0){
            node.left = put(node.left, key, value);
        }else if(node.key.compareTo(key) < 0){
            node.right = put(node.right, key, value);
        }else{
            retValue = node.value;
            node.value = value;
        }
        return node;
    }

    @Override
    public V remove(Object key) {
        retValue = null;
        remove(root, key);
        return retValue;
    }

    private CiscEntry<K, V> remove(CiscEntry<K, V> node, K key){
        if(node == null){
            return null;
        }else if(node.key.compareTo((K) key) > 0){
            node.left = remove(node.left, key);
        }else if(node.key.compareTo((K) key) < 0){
            node.right = remove(node.right, key);
        }else{
            retValue = node.value;
            if(node.right == null){
                size--;
                return node.left;
            }else if(node.left == null){
                size--;
                return node.right;
            }else{
                node.value = (V) getMaxEntry(node.left);
                //node.left = remove(node.left, node.value);
            }
        }
        return node;
    }

    private K getMaxEntry(CiscEntry<K, V> node, K key){
        if(node.right == null){
            return node.key;
        }else{
            return (K) getMaxEntry(node.right);
        }

    }
    public Object getMaxEntry(CiscEntry<K,V> right) {
        return right;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Set<? extends Entry<? extends K, ? extends V>> newset = m.entrySet();
        int j = 0;
        for(Entry<? extends K, ? extends V> entry : newset){
            put(entry.getKey(), entry.getValue());
            j++;
        }
        size = this.size();
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return keySet();
    }

    @Override
    public Collection<V> values() {
            ArrayList<Integer> newlist = new ArrayList<Integer>(size);
            values(root, newlist);
            return (Collection<V>) newlist;
    }

    private void values(CiscEntry<K, V> node, ArrayList<Integer> newlist){
        if(node != null){
            values(node.left, newlist);
            newlist.add((Integer) node.value);
            values(node.right, newlist);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new CiscEntrySet();
    }

    private static class CiscEntry<K, V> implements Map.Entry<K, V>{
        private K key;
        private V value;
        private CiscEntry<K, V> left;
        private CiscEntry<K, V> right;
        public CiscEntry(K key, V value){
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
    private class CiscEntrySet extends AbstractSet<Entry<K, V>>{

        @Override
        public Iterator<Entry<K, V>> iterator(){
            return new CiscEntrySetIterator(root);
        }

        @Override
        public int size(){
            return size;
        }

        private class CiscEntrySetIterator implements Iterator<Map.Entry<K, V>>{

            private Stack<CiscEntry<K, V>> stack;

            public CiscEntrySetIterator(CiscEntry<K, V> node){
                stack = new Stack<CiscEntry<K, V>>();
                while(node != null){
                    stack.push(node);
                    node = node.left;
                }
            }
            @Override
            public boolean hasNext(){
                return !stack.isEmpty();
            }

            @Override
            public Entry<K, V> next(){
                CiscEntry<K, V> front = stack.pop();
                CiscEntry<K, V> result = front;
                if(front.right != null){
                    front = front.right;
                    while(front != null){
                        stack.push(front);
                        front = front.left;
                    }
                }
                return result;
            }
        }
    }
}
