package skywolf46.commandannotation.util;

public class DataPair<K, V> {
    private K k;
    private V v;

    public DataPair(K k,V v){
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }
}
