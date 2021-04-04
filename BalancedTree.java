
public class BalancedTree {
    NodeTree root;
    NodeTree Left;
    NodeTree Middle;
    NodeTree max;
    NodeTree min;
    BalancedTree(){
        this.root=new NodeTree();
        this.Left= new NodeTree();
        this.Middle= new NodeTree();
    }

    public void insert(Key newKey, Value newValue) {
        NodeTree z = new NodeTree(newKey, newValue);
        z.size=1;
        if (root.key == null && Left.key==null) {
            Left=z;
            root=new NodeTree(Left.key, Left.value);

        }
        else if (root.middle == null){
            if (z.key.compareTo(Left.key)<0){
                Middle=new NodeTree(Left.key, Left.value);
                Left=z;
            }
            else {
                Middle=z;
            }
            root=new NodeTree(Middle.key, Middle.value);
            root.size=2;
            SetChildren(root,Left,Middle,null);
            max=Middle;
            min=Left;
        }
        else {
            NodeTree y=root;
            if (z.key.compareTo(min.key)<0){
                y=min;
            }
            if (z.key.compareTo(max.key)>0){
                y=max;
            }
            while (y.left != null) {
                if (z.key.compareTo(y.left.key) < 0) {
                    y = y.left;
                }
                else if (z.key.compareTo(y.middle.key) < 0) {
                    y = y.middle;
                }
                else {
                    if (y.right!= null){
                        y = y.right;
                    }
                }
            }

            NodeTree x = y.parent;
            z = insertAndSplit(x, z);
            while (x != root) {
                x = x.parent;
                if (z != null) {
                    z = insertAndSplit(x, z);
                }
                else {
                    updateKey(x);
                    if (x.right==null){
                        x.size=x.left.size+x.middle.size;
                    }
                    else{
                        x.size=x.left.size+x.middle.size+x.right.size;
                    }
                }
            }
            if (z != null) {
                NodeTree w = new NodeTree(z.key,z.value);
                SetChildren(w, x, z, null);
                w.size=w.left.size+w.middle.size;
                root = w;
            }

        }
    }
    public NodeTree insertAndSplit(NodeTree x, NodeTree z){
        NodeTree l=x.left;
        NodeTree m=x.middle;
        NodeTree r=x.right;
        if (r==null){
            if (z.key.compareTo(l.key)<0){
                SetChildren(x,z,l,m);
                if (z.key.compareTo(min.key)<0){
                    min=z;
                }
            }
            else if (z.key.compareTo(m.key)<0){
                SetChildren(x,l,z,m);
            }
            else{
                SetChildren(x,l,m,z);
                if (z.key.compareTo(max.key)>0){
                    max=z;
                }
            }
//            x.size=x.left.size+x.middle.size+x.right.size;
            x.size+=1;
            return null;
        }

        if (z.key.compareTo(l.key)<0){
            SetChildren(x,z,l,null);
            x.size=x.left.size+x.middle.size;
            if (z.key.compareTo(min.key)<0){
                min=z;
            }
            NodeTree y= new NodeTree(r.key,r.value);
            SetChildren(y,m,r,null);
            y.size=y.left.size+y.middle.size;
            return y;
        }
        else if (z.key.compareTo(m.key)<0){
            SetChildren(x,l,z,null);
            x.size=x.left.size+x.middle.size;
            NodeTree y= new NodeTree(r.key,r.value);
            SetChildren(y,m,r,null);
            y.size=y.left.size+y.middle.size;
            return y;
        }
        else if (z.key.compareTo(x.right.key)<0){
            SetChildren(x,l,m,null);
            x.size=x.left.size+x.middle.size;
            NodeTree y= new NodeTree(r.key,r.value);
            SetChildren(y,z,r,null);
            y.size=y.left.size+y.middle.size;
            return y;
        }
        else{
            SetChildren(x,l,m,null);
            x.size=x.left.size+x.middle.size;
            NodeTree y= new NodeTree(z.key,z.value);
            SetChildren(y,r,z,null);
            y.size=y.left.size+y.middle.size;
            if (z.key.compareTo(max.key)>0){
                max=z;
            }
            return y;
        }
    }

    public void SetChildren(NodeTree x, NodeTree left, NodeTree middle, NodeTree right){
        x.left=left;
        x.middle=middle;
        x.right=right;
        left.parent=x;
        if (middle!= null){
            middle.parent=x;
        }
        if (right!=null){
            right.parent=x;
        }
        updateKey(x);
    }

    public void updateKey(NodeTree x){
        x.key=x.left.key;
        if (x.middle!=null){
            x.key=x.middle.key;
        }
        if (x.right!=null){
            x.key=x.right.key;
        }
    }

    public void delete(Key key) {
        NodeTree x=searchKey(root,key);
        if (x!=null){
            if (root.size==2){
                if (x.key.compareTo(root.left.key)==0){
                   root=new NodeTree(root.middle.key, root.middle.value);
                   root.size=1;
                }
                else {
                    root=new NodeTree(root.left.key, root.left.value);
                    root.size=1;
                }
            }
            else if (root.size==1){
                root=null;
            }
            else{
                NodeTree y = x.parent;
                if (x == y.left) {
                    if (x.key==min.key){
                        min=y.middle;
                    }
                    SetChildren(y, y.middle, y.right, null);
                }
                else if (x == y.middle) {
                    SetChildren(y, y.left, y.right, null);
                }
                else {
                    if (x.key==max.key){
                        max=y.middle;
                    }
                    SetChildren(y, y.left, y.middle, null);
                }
                y.size-=1;
                x.parent=null;
                while (y != null) {
                    if (y.middle == null) {
                        if (y != root) {
                            y = BorrowOrMerge(y);
                        }
                        else {
                            root = y.left;
                            y.left.parent = null;
                            y=null;
                        }
                    }
                    else {
                        updateKey(y);
                        y = y.parent;
                        if (y!=null){
                            y.size-=1;
                        }
                    }
                }
            }

        }

    }

    public NodeTree BorrowOrMerge(NodeTree y){
        NodeTree z = y.parent;
        if (y == z.left){
            NodeTree x = z.middle;
            if (x.right != null){
                SetChildren(y, y.left, x.left, null);
                SetChildren(x, x.middle, x.right, null);
                x.size-=1;
                y.size+=1;
                z.size-=1;
            }
            else{
                SetChildren(x, y.left, x.left, x.middle);
                x.size=x.size+y.size;
                y.parent=null;
                SetChildren(z, x, z.right, null);
                z.size-=1;
            }
            return z;
        }
        if (y == z.middle){
            NodeTree x = z.left;
            if (x.right != null){
                SetChildren(y, x.right, y.left, null);
                SetChildren(x, x.left, x.middle, null);
                x.size-=1;
                y.size+=1;
                z.size-=1;
            }
            else{
                SetChildren(x, x.left, x.middle, y.left);
                x.size=x.size+y.size;
                y.parent=null;
                SetChildren(z, x, z.right, null);
                z.size-=1;
            }
            return z;
        }
        NodeTree x=z.middle;
        if (x.right != null){
            SetChildren(y, x.right, y.left, null);
            SetChildren(x, x.left, x.middle, null);
            x.size-=1;
            y.size+=1;
            z.size-=1;

        }
        else {
            SetChildren(x, x.left, x.middle, y.left);
            x.size=x.size+y.size;
            y.parent=null;
            SetChildren(z, z.left, x, null);
            z.size-=1;
        }
        return z;
    }
    public NodeTree searchKey(NodeTree x, Key k){
        if (x.left== null){
            if (x.key.compareTo(k)==0){
                return x;
            }
            else {
                return null;
            }
        }
        if (k.compareTo(x.left.key)<=0){
            return searchKey(x.left,k);
        }
        else if(k.compareTo(x.middle.key)<=0){
            return searchKey(x.middle,k);
        }
        else {
            if (x.right!=null){
                return searchKey(x.right,k);
            }
            else {
                return null;
            }
        }
    }

//    public Value search(Key key){}
//    public int rank(Key key){}
//    public Key select(int index){}
//    public Value sumValuesInInterval(Key key1, Key key2){}
    public static void main(String[] args) {
        MyKey key_one= new MyKey("please",1);
        MyKey key_two= new MyKey("work",2);
        MyKey key_three= new MyKey("work",3);
        MyKey key_four= new MyKey("it",4);
        MyKey key_five= new MyKey("do",5);
        MyKey key_six= new MyKey("it",6);
        MyKey key_seven= new MyKey("a",7);
        MyKey key_eight= new MyKey("yellow",8);
        MyKey key_nine= new MyKey("joe",9);
        MyValue value_one=new MyValue(1);
        MyValue value_two=new MyValue(2);
        MyValue value_three=new MyValue(3);
        MyValue value_four=new MyValue(4);
        MyValue value_five=new MyValue(5);
        MyValue value_six=new MyValue(6);
        MyValue value_seven=new MyValue(7);
        MyValue value_eight=new MyValue(8);
        MyValue value_nine=new MyValue(9);
        BalancedTree Tree= new BalancedTree();
        System.out.println("#####################   inserting   ##############################");
        Tree.insert(key_one,value_one);
        Tree.insert(key_two,value_two);
        Tree.insert(key_three,value_three);
        Tree.insert(key_four,value_four);
        Tree.insert(key_five,value_five);
        Tree.insert(key_six,value_six);
        Tree.insert(key_seven,value_seven);
        Tree.insert(key_eight,value_eight);
        Tree.insert(key_nine,value_nine);
        System.out.println(Tree.root.key.toString());
        System.out.println(Tree.root.left.key.toString());
        System.out.println(Tree.root.middle.key.toString());
        System.out.println(Tree.root.right.key.toString());
        System.out.println(Tree.root.left.left.key.toString());
        System.out.println(Tree.root.left.middle.key.toString());
        System.out.println(Tree.root.left.right.key.toString());
        System.out.println(Tree.root.middle.left.key.toString());
        System.out.println(Tree.root.middle.middle.key.toString());
        System.out.println(Tree.root.middle.right.key.toString());
        System.out.println(Tree.root.right.left.key.toString());
        System.out.println(Tree.root.right.middle.key.toString());
        System.out.println(Tree.root.right.right.key.toString());
        System.out.println("#####################   deleting   ##############################");
        Tree.delete(key_eight);
        Tree.delete(key_two);
        Tree.delete(key_three);
        Tree.delete(key_two);
        Tree.delete(key_six);
        Tree.delete(key_four);
        Tree.delete(key_five);
        Tree.delete(key_one);
        Tree.delete(key_seven);
        Tree.delete(key_nine);
//        Tree.insert(key_six,value_six);
//        Tree.insert(key_seven,value_seven);
//        Tree.insert(key_five,value_five);



//        System.out.println(Tree.root.key.toString());
//        System.out.println(Tree.root.left.key.toString());
//        System.out.println(Tree.root.middle.key.toString());
////        System.out.println(Tree.root.right.key.toString());
//        System.out.println(Tree.root.left.left.key.toString());
//        System.out.println(Tree.root.left.middle.key.toString());
////        System.out.println(Tree.root.left.right.key.toString());
//        System.out.println(Tree.root.middle.left.key.toString());
//        System.out.println(Tree.root.middle.middle.key.toString());
//        System.out.println(Tree.root.middle.right.key.toString());
//        System.out.println(Tree.root.right.left.key.toString());
//        System.out.println(Tree.root.right.middle.key.toString());
//        System.out.println(Tree.root.right.right.key.toString());


    }
}
