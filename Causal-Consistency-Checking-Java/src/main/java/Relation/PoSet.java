package Relation;

public interface PoSet {
    void addRelation(int a, int b);
    void calculateTransitiveClosure();
    void printRelations();
}
