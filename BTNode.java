import java.util.ArrayList;

public abstract class BTNode
{
   protected int nodeID;
   protected static final int SIZE = 3;
   protected ArrayList<String> keys;
   protected BTNodeInternal parent;
   
   public BTNodeInternal getParent() { return parent; }
   
   public abstract void insert(String key, BPlusTree tree);
      
   public abstract void printLeavesInSequence();
      
   public abstract void printStructureWKeys();

   public abstract int findInsertIndex(String word);  // One node can link to multiple subtrees. This method helps find which subtree we should follow. This is only used internally within other methods.
   
   public abstract Boolean rangeSearch(String startWord, String endWord);
   
   public abstract Boolean searchWord(String word);
}