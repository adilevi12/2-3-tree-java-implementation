
public class NodeTree {
    NodeTree left,middle,right,parent;
    Key key;
    Value value;
    int size;
    NodeTree(Key key, Value value){
        this.key=key.createCopy();
        this.value=value.createCopy();
    }
    NodeTree(){
    }
}
