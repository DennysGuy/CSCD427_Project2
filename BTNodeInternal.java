import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BTNodeInternal extends BTNode
{
   public ArrayList<BTNode> children;
   
   public BTNodeInternal()
   {
      this.children = new ArrayList<>();
      this.keys = new ArrayList<>();
   }
   
   public void insert(String key, BPlusTree tree)
   {
      /*int index = this.findInsertIndex(key);
      BTNodeLeaf child = (BTNodeLeaf) this.children.get(index);
      child.insert(key, tree);*/
      this.insertLeaf(key, tree.root, tree);

      //next we have to fix the tree
      //this.fixBPlusTree(tree);

   }

   private Boolean insertLeaf(String key, BTNode root, BPlusTree tree) {
      if (root instanceof  BTNodeInternal) {
         int index = findInsertIndex(key, root);
         //need to check if it's full
         return this.insertLeaf(key, ((BTNodeInternal) root).children.get(index), tree);
      } else{
         root.insert(key, tree);
         //fix parent, recursive return to parent to check if parent needs to be pixed
         this.fixBPlusTree(root.parent, tree);
         return true;
      }

   }

   //fix internal node
   private Boolean fixBPlusTree(BTNode root, BPlusTree tree) {
      //if root, do fixing here
      if (root.keys.size() > BTNode.SIZE) {
         if (root.equals(tree.root)) {
            //create a new root node
            this.fixRoot();
            return true;

         } else {
            BTNodeInternal internalNode = (BTNodeInternal) root;
            internalNode.fixInternal();
            return fixBPlusTree(root.parent, tree);
         }
      }
      return true;
   }

   public void fixInternal() {
      int splitKey = this.keys.size()/2;
      //add split key to new root and remove it from the current root
      this.parent.keys.add(this.keys.get(splitKey));
      Collections.sort(this.parent.keys);
      String keyToTrack = this.keys.remove(splitKey);
      splitKey = this.keys.size()/2; //get updated split key for leafs
      //create 2 new internal nodes and split the remaining keys into them
      BTNodeInternal newNode1 = new BTNodeInternal();
      BTNodeInternal newNode2 = new BTNodeInternal();
      this.addKeys(0, splitKey, newNode1);
      this.addKeys(splitKey+1, this.keys.size()-1, newNode2);
      //need to add root's leaf nodes
      this.addLeafs(newNode1);
      this.addLeafs(newNode2);

      newNode1.parent = this.parent;
      newNode2.parent = this.parent;

      int index = this.parent.keys.indexOf(keyToTrack);
      this.parent.children.add(index, newNode1);
      this.parent.children.add(index+1, newNode2);
      //this.parent.children.add(leftNode);
      //this.parent.children.add(rightNode);
      this.parent.children.remove(this);

   }

   public void fixRoot() {
      BTNodeInternal newRoot = new BTNodeInternal();
      //find the key to split on
      int splitKey = this.keys.size()/2;
      //add split key to new root and remove it from the current root
      newRoot.keys.add(this.keys.get(splitKey));
      this.keys.remove(splitKey);
      splitKey = this.keys.size()/2; //get updated split key for leafs
      //create 2 new internal nodes and split the remaining keys into them
      BTNodeInternal leftNode = new BTNodeInternal();
      BTNodeInternal rightNode = new BTNodeInternal();
      this.addKeys(0, splitKey, leftNode);
      this.addKeys(splitKey+1, this.keys.size()-1, rightNode);
      //need to add root's leaf nodes
      this.addLeafs(leftNode);
      this.addLeafs(rightNode);

      leftNode.parent = newRoot;
      rightNode.parent = newRoot;

      newRoot.children.add(leftNode);
      newRoot.children.add(rightNode);

      this.parent = newRoot;
   }

   public Boolean fixLeafNodePointers(BTNode root, BPlusTree tree) {

      if (root instanceof BTNodeInternal) {
         //traverse to the bottom of the BPlusTree
         BTNodeInternal curRoot = (BTNodeInternal) root;
         return fixLeafNodePointers(curRoot.children.get(0), tree);
      } else {
         BTNodeLeaf curLeaf = (BTNodeLeaf) tree.root;
         return true;
      }
   }

   public void printLeavesInSequence()
   {
      
   }
   
   public void printStructureWKeys()
   {
      
   }

   public int findInsertIndex(String word) {
      for (int i = 0; i <= this.keys.size(); i++) {

         if (i == this.keys.size()) {
            return i;
         }

         if (word.compareTo(this.keys.get(i)) <= 0) {
            return i;
         }

      }

      return -1;
   }

   private void addKeys(int start, int end, BTNodeInternal node) {
      for (int i = start; i <= end ; i++) {
        node.keys.add(this.keys.get(i));
      }
   }

   private void addLeafs(BTNodeInternal node) {
      for (int i = 0; i <= node.keys.size(); i++) {
         BTNodeLeaf child = (BTNodeLeaf) this.children.removeFirst();
         child.parent = node;
         node.children.add(child);
      }
   }

   public int findInsertIndex(String word, BTNode root) {
      for (int i = 0; i <= root.keys.size(); i++) {

         if (i == root.keys.size()) {
            return i;
         }

         if (word.compareTo(root.keys.get(i)) <= 0) {
            return i;
         }
      }

      return -1;
   }

   public Boolean rangeSearch(String startWord, String endWord)
   {
      return true;
   }
   
   public Boolean searchWord(String word)
   {
      return true;
   }
}