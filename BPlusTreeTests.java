import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BPlusTreeTests {

    private BPlusTree BPlusTree;

    @BeforeEach
    public void Setup(){
        Set<String> ignoreSet = new HashSet<>(List.of("foo","bar"));
        this.BPlusTree = new BPlusTree(ignoreSet);
    }

    @ParameterizedTest
    @ValueSource(strings = {"apples","bananas","grapes"})
    public void InsertWord_NoIgnoreSet_True(String word) {
        boolean canAdd = BPlusTree.insertWord(word);
        assertTrue(canAdd);
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "bar"})
    public void InsertWord_IgnoreSet_False(String word){

        boolean canAdd = BPlusTree.insertWord(word);
        assertFalse(canAdd);

    }

    @Test
    public void InsertWord_FirstWord_InstanceOfBTNodeLeaf(){
        BPlusTree.insertWord("Test");
        assertInstanceOf(BTNodeLeaf.class, BPlusTree.root);

    }


}
