
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


public class GraphSearch {
    public static HashMap<String, Integer> hasMapExploredSet = new HashMap<String, Integer>();
    public static int iterativeLengtheningLimit;
    public static int frontierMaxSize=0;

    public static Comparator<Node> frontierPathCost = Comparator.comparing(Node::getPathCost);
    public static PriorityQueue<Node> frontier = new PriorityQueue<Node>(frontierPathCost);


    public static void main(String[] args) {
        System.out.println("merhaba");
        GenerateState generateState = new GenerateState(5);    // generate instance class
//int[][] initialState = generateState.start();          //generate randomly instance

        int[][] initialState={ { 0, 1, 3, 4 },{ 12, 13, 2, 5 },{11, 14, 15, 6 },{ 10, 9, 8, 7 }  };           // A part

//printState(initialState);
        Node firstNode= new Node(initialState,null,0,0,"Direction : ");
        frontier.add(firstNode);

        // buradan istenilen method seçilip başlanabilir
        startUcs();
//startILS(22,firstNode);
//startHeuristicSearchH1();
//startHeuristicSearchH2();
    }
    public static void maxNumberOfNodesStoredMemory(int nodesStoredMemory){
        if(nodesStoredMemory> frontierMaxSize)
            frontierMaxSize=nodesStoredMemory;
    }
    public static void foundGoalNodeInfo(int counter){
        System.out.println("We found solution : ");
        System.out.println("Node cost: "+frontier.peek().getPathCost());
        System.out.println("Node dept: "+frontier.peek().depth);
//		printState(UCS_Frontier.peek().stateInfo);
        System.out.println("The total number of expanded nodes: "+counter);
//	    System.out.println("key: "+UCS_Frontier.peek().key);
//	    System.out.println("key: "+UCS_Frontier.peek().getpathDiagram());
        System.out.println("The maximum number of nodes stored in memory: "+ frontierMaxSize);
//	    System.out.println("Nodes stored in memory: "+ UCS_Frontier.size());
    }

    public static void NodeInfo(int counter){
//		System.out.println("Node cost: "+UCS_Frontier.peek().getPathCost());
//		System.out.println("Node dept: "+UCS_Frontier.peek().depth);
        //	printState(UCS_Frontier.peek().stateInfo);
//			System.out.println("-----------------------------");
    }


    public static void startILS(int upperLimit, Node firstNode){       // childların oluşumu için node de işlem yapılca, limitten daha yüksek child oluşmucak

        int limit = 0;
        int counter = 0;
        hasMapExploredSet.put(frontier.peek().key,frontier.peek().getPathCost());
        while(limit< upperLimit){

            while(!frontier.isEmpty()){

                if(checkGoalState(frontier.peek().stateInfo)){
                    foundGoalNodeInfo(counter);
                    limit=upperLimit;
                    break;
                }
                if((GraphSearch.hasMapExploredSet.containsKey(frontier.peek().key)) &&
                        (frontier.peek().getPathCost() > GraphSearch.hasMapExploredSet.get(frontier.peek().key)) ){
                    frontier.poll();
                    continue;
                }
                counter++;
                frontier.peek().FindPotantialChildrenILS(limit);
                maxNumberOfNodesStoredMemory(frontier.size());
                frontier.poll();

            }
            frontier.add(firstNode);
            limit++;
            hasMapExploredSet.clear();
        }
    }

    public static void startUcs(){
        int counter=0;
        hasMapExploredSet.put(frontier.peek().key,frontier.peek().getPathCost());
        while(!frontier.isEmpty()){

            if(checkGoalState(frontier.peek().stateInfo)){
                foundGoalNodeInfo(counter);
                break;
            }
            if((GraphSearch.hasMapExploredSet.containsKey(frontier.peek().key)) &&
                    (frontier.peek().getPathCost() > GraphSearch.hasMapExploredSet.get(frontier.peek().key)) ){
                frontier.poll();
                continue;
            }
            counter++;
            NodeInfo(counter);
            frontier.peek().FindPotantialChildrenUCS();
            maxNumberOfNodesStoredMemory(frontier.size());
            frontier.poll();
        }
    }

    public static void startHeuristicSearchH1(){
        int counter=0;
        hasMapExploredSet.put(frontier.peek().key,frontier.peek().getPathCost());
        while(!frontier.isEmpty()){
            if(checkGoalState(frontier.peek().stateInfo)){
                foundGoalNodeInfo(counter);
                break;
            }

            counter++;
            //         NodeInfo(counter);
            frontier.peek().FindPotantialChildrenHeuristicH1();
            maxNumberOfNodesStoredMemory(frontier.size());
            frontier.poll();

        }
    }


    public static void startHeuristicSearchH2(){
        int counter=0;
        hasMapExploredSet.put(frontier.peek().key,frontier.peek().getPathCost());   // nedenini hala anlamadım
        while(!frontier.isEmpty()){

            if(checkGoalState(frontier.peek().stateInfo)){
                foundGoalNodeInfo(counter);
                break;
            }
            if((GraphSearch.hasMapExploredSet.containsKey(frontier.peek().key)) &&  // daha önce aynı değere sahip node yapıldıysa yapma bu nodu
                    (frontier.peek().getPathCost() > GraphSearch.hasMapExploredSet.get(frontier.peek().key)) ){
                frontier.poll();
                continue;
            }
            counter++;
            frontier.peek().FindPotantialChildrenHeuristicH2();
            maxNumberOfNodesStoredMemory(frontier.size());
            frontier.poll();
        }
    }

    public static boolean checkGoalState(int[][] nodeState){
        boolean checkResult = true;
        int[][]goalState={ { 1, 2, 3, 4 },{ 12, 13, 14, 5 },{11, 0, 15, 6 },{ 10, 9, 8, 7}  }; ;
        //	checkResult= Arrays.equals(goalState, nodeState);
        int i,j;
        for(i=0;i<4;i++){
            for(j=0;j<4;j++){
                if(goalState[i][j] != nodeState[i][j]){
                    checkResult=false;
                }

            }
        }
        return checkResult;
    }


    public static void printState(int mat[][])
    {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
    }
    public static int calculatePathCost(String pathDiagram){         // this diagram help us count the number of 1 and 3 and we can get total path cost


        int oneNumbersTotal = 0;
        for (Character c: pathDiagram.toCharArray()) {
            if (c.equals('1')) {
                oneNumbersTotal++;
            }
        }
        int threeNumbersTotal = 0;
        for (Character c: pathDiagram.toCharArray()) {
            if (c.equals('3')) {
                threeNumbersTotal++;
            }
        }
        int totalCost= (threeNumbersTotal*3) + oneNumbersTotal;
        return totalCost;
    }

    public static int calculationOfHeuristicH1(int mat[][]){
        int[][]goalState={ { 1, 2, 3, 4 },{ 12, 13, 14, 5 },{11, 0, 15, 6 },{ 10, 9, 8, 7}  }; ;
        int misPlaced=0;
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat.length; j++){

                if(mat[i][j]==0)
                    continue;


                if(!(goalState[i][j] == mat[i][j])){
                    misPlaced++;
                } } }

        return misPlaced;
    }


    public static int calculationOfHeuristicH2(int mat[][]){
        //   0     1     2     3     4     5     6      7     8     9   10     11    12     13   14     15
        int[][] manHattanLocation= {{2,1},{0,0},{0,1},{0,2},{0,3}, {1,3},{2,3},{3,3},{3,2},{3,1},{3,0},{2,0},{1,0},{1,1},{1,2},{2,2}};

        int temp=0;
        int correctPositionX=0;
        int correctPositionY=0;
        int manHattanSum=0;
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat.length; j++){
                temp= mat[i][j];


                if(temp==0)
                    continue;

                correctPositionX= manHattanLocation[temp][0];
                correctPositionY= manHattanLocation[temp][1];
                manHattanSum=Math.abs(correctPositionX-i)+manHattanSum+Math.abs(correctPositionY-j);
            }

        }

        return manHattanSum;
    }
}


