import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BTNodeLeafTests {

    private BPlusTree BPlusTree;

    @BeforeEach
    @DisplayName("Instantiate B+ Tree")
    void setup(){
        BPlusTree = new BPlusTree(null);

    }

    @Nested
    @DisplayName("Checking Constructor is Correct")
    class ConstructorTests{
        @Test
        @DisplayName("Checking keyCounts and keys are not null during BTNodeLeaf Construction")
        void Constructor_NormalConditions_InitializedKeyAndKeyCountFields(){
            BTNodeLeaf leaf = new BTNodeLeaf();
            assertNotNull(leaf.keys, "keys is null after BTLeafNode construction");
            assertNotNull(leaf.keyCounts, "keyCounts is null after BTLeafNode construction");
            assertNull(leaf.nextLeaf, "nextLeaf should be null after BTLeafNode construction");

        }
    }


    @Nested
    @DisplayName("First Node Insertion in B+ Tree")
    class FirstLeafNodeTests{

        @BeforeEach
        @DisplayName("Add in 3 keys to BPlusTree")
        void setup(){
            BPlusTree.insertWord("A");
            BPlusTree.insertWord("C");
            BPlusTree.insertWord("B");
        }

        @Test
        @DisplayName("Inserting \"A\" into B+ Tree Should result \"A\" being the only key within the first node with a key count of 1 at index 0")
        void insert_A_Success() {
            BTNodeLeaf root = (BTNodeLeaf) BPlusTree.root;
            assertEquals("a", root.keys.get(0), "First element within keys is not \"A\"");
            assertEquals(1, root.keyCounts.get(0), "\"A\" should have key count of 1");

        }

        @Test
        @DisplayName("Inserting \"C\" into B+ Tree Should result \"C\" being the only key within the first node with a key count of 1 at index 2")
        void insert_C_Success() {
            BTNodeLeaf root = (BTNodeLeaf) BPlusTree.root;

            assertEquals("c", root.keys.get(2), "First element within keys is not \"C\"");
            assertEquals(1, root.keyCounts.get(2), "\"C\" should have key count of 1");

        }

        @Test
        @DisplayName("Inserting \"B\" into B+ Tree Should result \"B\" being the only key within the first node with a key count of 1 at index 1")
        void insert_B_Success() {
            BTNodeLeaf root = (BTNodeLeaf) BPlusTree.root;

            assertEquals("b", root.keys.get(1), "First element within keys is not \"B\"");
            assertEquals(1, root.keyCounts.get(1), "\"B\" should have key count of 1");

        }

        @Test
        @DisplayName("Checking Adding \"B\"should increase increase key count to 2")
        void insert_DuplicateB_IncreaseBKeyCount(){
            BTNodeLeaf root = (BTNodeLeaf) BPlusTree.root;

            BPlusTree.insertWord("b");
            assertEquals(2, root.keyCounts.get(1), "\"B\" should have key count of 2 after inserting it again");
            assertEquals(3, root.keys.size(), "Root should have 3 keys");

        }
    }

    @Nested
    @DisplayName("Second Node Insertion in B+ Tree")
    class SecondLeafNodeTests{

        @Nested
        @DisplayName("Fourth Key Insertion")
        class FourthKeySplitsInitialNode{

            private BTNodeInternal rootInternal;

            @BeforeEach
            void setup(){
                BPlusTree.insertWord("A");
                BPlusTree.insertWord("C");
                BPlusTree.insertWord("B");
                BPlusTree.insertWord("C");

                BPlusTree.insertWord("B");
                BPlusTree.insertWord("D");

            }

            @Test
            @DisplayName("Checking Root is an instance of BTNodeInternal")
            void insert_FourthKey_MakesRootBTNodeInternal(){
                assertInstanceOf(BTNodeInternal.class, BPlusTree.root);
            }

            @Test
            @DisplayName("Checking Root has exactly Two Children")
            void insert_FourthKey_RootHasTwoChildren(){
                rootInternal = (BTNodeInternal) BPlusTree.root;
                assertEquals(2, rootInternal.children.size());
            }

            @Test
            @DisplayName("Checking First Child Node for Correct Keys and Counts")
            void insert_FourthKey_FirstChildCorrectKeys(){
                rootInternal = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) rootInternal.children.get(0);
                assertEquals(List.of("a","b"), firstChild.keys);
                assertEquals(List.of(1,2), firstChild.keyCounts);

            }

            @Test
            @DisplayName("Checking Second Child Node for Correct Keys and Counts")
            void insert_FourthKey_SecondChildCorrectKeys(){
                rootInternal = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf secondChild = (BTNodeLeaf) rootInternal.children.get(1);
                assertEquals(List.of("c","d"), secondChild.keys);
                assertEquals(List.of(1,1), secondChild.keyCounts );

            }

            @Test
            @DisplayName("Checking that Root has \"C\" Key within Root")
            void insert_FourthKey_RootContainsCKey(){
                assertEquals(List.of("c"), BPlusTree.root.keys);

            }

            @Test
            @DisplayName("Checking Root's Children have Root as their Parent")
            void insert_FourthKey_RootChildrenHaveRootAsParent(){
                rootInternal = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) rootInternal.children.get(0);
                BTNodeLeaf secondChild = (BTNodeLeaf) rootInternal.children.get(1);

                assertEquals(BPlusTree.root, firstChild.parent);
                assertEquals(BPlusTree.root, secondChild.parent);

            }

            @Test
            @DisplayName("Checking First Child's nextLeaf points to Second Child")
            void insert_FourthKey_FirstChildCorrectNextLeaf(){
                rootInternal = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) rootInternal.children.get(0);
                assertEquals(rootInternal.children.get(1), firstChild.nextLeaf);

            }
        }

        @Nested
        @DisplayName("Creating B+ Tree with Four Children")
        class FullLevelOnlyLeafsTests{
            @BeforeEach
            void setup(){
                BPlusTree.insertWord("A");
                BPlusTree.insertWord("C");
                BPlusTree.insertWord("B");
                BPlusTree.insertWord("D");
                BPlusTree.insertWord("E");
                BPlusTree.insertWord("E");
                BPlusTree.insertWord("F");
                BPlusTree.insertWord("B");
                BPlusTree.insertWord("G");
                BPlusTree.insertWord("H");
                BPlusTree.insertWord("H");
                BPlusTree.insertWord("I");
                BPlusTree.insertWord("Blue");
                BPlusTree.insertWord("Doctor");
                BPlusTree.insertWord("flame");
                BPlusTree.insertWord("flame");
            }

            @Test
            @DisplayName("Checking that Root has Four Children")
            void Insert_FourChildren_FourChildrenInKeys(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                assertEquals(4, internalRoot.children.size());

            }

            @Test
            @DisplayName("Checking First Child Node for Correct Keys and Counts")
            void Insert_FirstChild_CorrectKeysAndKeyCounts(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) internalRoot.children.get(0);
                assertEquals(List.of("a","b","blue"), firstChild.keys);
                assertEquals(List.of(1,2,1), firstChild.keyCounts);

            }

            @Test
            @DisplayName("Checking Second Child Node for Correct Keys and Counts")
            void Insert_SecondChild_CorrectKeysAndKeyCounts(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf secondChild = (BTNodeLeaf) internalRoot.children.get(1);
                assertEquals(List.of("c","d","doctor"), secondChild.keys);
                assertEquals(List.of(1,1,1), secondChild.keyCounts);

            }
            @Test
            @DisplayName("Checking Third Child Node for Correct Keys and Counts")
            void Insert_ThirdChild_CorrectKeysAndKeyCounts(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf thirdChild = (BTNodeLeaf) internalRoot.children.get(2);
                assertEquals(List.of("e","f","flame"), thirdChild.keys);
                assertEquals(List.of(2,1,2), thirdChild.keyCounts);

            }

            @Test
            @DisplayName("Checking Fourth Child Node for Correct Keys and Counts")
            void Insert_FourthChild_CorrectKeysAndKeyCounts(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf fourthChild = (BTNodeLeaf) internalRoot.children.get(3);
                assertEquals(List.of("g","h","i"), fourthChild.keys);
                assertEquals(List.of(1,2,1), fourthChild.keyCounts);

            }

            @Test
            @DisplayName("Checking Parent Node for Correct Keys")
            void insert_FourChildren_CorrectKeysAtParent(){
                assertEquals(List.of("c","e","g"), BPlusTree.root.keys);
            }

            @Test
            @DisplayName("Checking nextLeaf references are correct")
            void Insert_FourChildren_CorrectNextLeafReferences(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) internalRoot.children.get(0);
                BTNodeLeaf secondChild = (BTNodeLeaf) internalRoot.children.get(1);
                BTNodeLeaf thirdChild = (BTNodeLeaf) internalRoot.children.get(2);
                BTNodeLeaf fourthChild = (BTNodeLeaf) internalRoot.children.get(3);

                assertEquals(secondChild,firstChild.nextLeaf);
                assertEquals(thirdChild,secondChild.nextLeaf);
                assertEquals(fourthChild,thirdChild.nextLeaf);
                assertNull(fourthChild.nextLeaf);
            }

            @Test
            @DisplayName("Checking Children for Root as Parent Node")
            void Insert_FourChildren_CorrectParent(){
                BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
                BTNodeLeaf firstChild = (BTNodeLeaf) internalRoot.children.get(0);
                BTNodeLeaf secondChild = (BTNodeLeaf) internalRoot.children.get(1);
                BTNodeLeaf thirdChild = (BTNodeLeaf) internalRoot.children.get(2);
                BTNodeLeaf fourthChild = (BTNodeLeaf) internalRoot.children.get(3);

                assertEquals(BPlusTree.root,firstChild.getParent());
                assertEquals(BPlusTree.root,secondChild.getParent());
                assertEquals(BPlusTree.root,thirdChild.getParent());
                assertEquals(BPlusTree.root,fourthChild.getParent());
            }


        }

        @Nested
        @DisplayName("Checking Adding Keys in Unordered Manner")
        class UnorderedKeyTests{

            private BPlusTree unorderedKeyInsertionTree;

            @BeforeEach
            void setup(){
                unorderedKeyInsertionTree = new BPlusTree(null);
                unorderedKeyInsertionTree.insertWord("A");
                unorderedKeyInsertionTree.insertWord("E");
                unorderedKeyInsertionTree.insertWord("B");
                unorderedKeyInsertionTree.insertWord("C");
                unorderedKeyInsertionTree.insertWord("G");
                unorderedKeyInsertionTree.insertWord("E");
                unorderedKeyInsertionTree.insertWord("Blue");
                unorderedKeyInsertionTree.insertWord("Doing");

            }

            @Test
            @DisplayName("Checking Children Have Correct Kyes")
            void CheckStructure(){
                BTNodeInternal rootInternal = (BTNodeInternal) unorderedKeyInsertionTree.root;
                assertEquals(List.of("a","b","blue"), rootInternal.children.get(0).keys);
                assertEquals(List.of("c","doing"), rootInternal.children.get(1).keys);
                assertEquals(List.of("e","g"), rootInternal.children.get(2).keys);
                assertEquals(List.of("c","e"), rootInternal.keys);
            }

        }


    }

}
