public class Test
{
    public static void main(String[] args)
    {
        IndexMinPQ<Integer> minQ = new IndexMinPQ<Integer>(2);
        LinearProbingHashST<String, Integer> hash = new LinearProbingHashST<String, Integer>();
        hash.put("Hello", 0);
        hash.put("Goodbye", 1);
        System.out.println(hash.get("Hello"));
        System.out.println(hash.get("Goodbye"));

        minQ.insert(0, 11);
        minQ.insert(1, 12);
        minQ.insert(2, 13);
        minQ.insert(3, 10);

        System.out.println(minQ.keyOf(2));
        System.out.println(minQ.minIndex());
        System.out.println(minQ.minKey());

        minQ.delete(3);
        System.out.println(minQ.minIndex());
        System.out.println(minQ.minKey());
    }
}