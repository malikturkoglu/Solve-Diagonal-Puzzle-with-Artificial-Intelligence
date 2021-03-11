import java.util.Arrays;

class Node{
    private int pathCost;
    Node parent;
    int[][] stateInfo = new int[4][4];
    int zeroIndexX;
    int zeroIndexY;
    int depth;
    String key;
    private String pathDiagram;
    public Node(int[][]stateInfo,Node parent,int pathCost, int depth, String pathDiagram){
        this.stateInfo=stateInfo;
        this.parent=parent;
        this.pathCost=pathCost;
        this.depth=depth;
        this.pathDiagram=pathDiagram;
        findZero();
        generateKey(stateInfo);
    }

    public int getPathCost() {
        return pathCost;
    }

    public String getpathDiagram(){
        return this.pathDiagram;
    }


    public String findDirection(int x, int y){            // create path diagram
        if(((zeroIndexX-1)==x) && (zeroIndexY)==y){
            return "Up(1)  ";
        }
        else if(((zeroIndexX+1)==x) && (zeroIndexY)==y){
            return "Down(1)  ";
        }
        else if(((zeroIndexX)==x) && ((zeroIndexY)+1)==y){
            return "Right(1)  ";
        }
        else if(((zeroIndexX)==x) && ((zeroIndexY)-1)==y){
            return "Left(1)  ";
        }
        else if(((zeroIndexX-1)==x) && ((zeroIndexY)-1)==y){
            return "Up-Left(3)  ";
        }
        else if(((zeroIndexX+1)==x) && ((zeroIndexY)-1)==y){
            return "Down-Left(3)  ";
        }
        else if(((zeroIndexX-1)==x) && ((zeroIndexY)+1)==y){
            return "Up-Right(3)  ";
        }
        else if(((zeroIndexX+1)==x) && ((zeroIndexY)+1)==y){
            return "Down-Right(3)  ";
        }

        return "geçersiz adress  ";
    }

    public Node createchild(int x, int y){
        int temp[][] = new int[4][4];
        for(int i=0; i<stateInfo.length; i++)
            for(int j=0; j<stateInfo[i].length; j++)
                temp[i][j]=stateInfo[i][j];                    // burada birebir aynısını oluşturdu child ile


        temp[zeroIndexX][zeroIndexY]= temp[x][y];   // burada yer değiştirme yapıyoruz. mesela 5 ile 0 i yer değiştircez. sıfırın oraya
        temp[x][y]=0;           //oraya 5 i, 5 in oraya da sıfırı koyuyoruz.

//		int costOfMovement= FindingCostPath(x,y)+getPathCost();    // bunda baya kötü çıktı diğerine göre node 24 ve 5200 de çıkmıştı.
        int costOfMovement= FindingCostPath(x,y)+GraphSearch.calculatePathCost(this.pathDiagram);       // parentın ona uzaklığı ve parentinin başlangıçtan uzaklığı ekledik
        int depthOfChild= this.depth+1;
        String childDirection=this.pathDiagram + findDirection(x, y);  // bunu içerisine ver
        Node child = new Node(temp, this, costOfMovement, depthOfChild, childDirection);

        return child;


    }

    public void findZero(){      // zeronun konumunu bulmaya yarıyor.
        for(int i=0; i<=3; i++) {
            for(int j=0; j<=3; j++) {
                if(stateInfo[i][j]==0) {                        // sıfırın yerini arıyoruz
                    this.zeroIndexX = i;
                    this.zeroIndexY = j;
                    break;
                }
            }
        }

    }

    public int FindingCostPath(int x,int y){
        // bu kısımda biz sağ-sol-yukarı aşşa olan sonuçlarda 1 alıcaz.
        // çaprazlama olan sonuçlarda ise 3 alacağız.
        int totalcost= Math.abs(zeroIndexX-x) + Math.abs(zeroIndexY-y);
        if(totalcost==2)
            totalcost=3;

        return totalcost;
    }


    public void FindPotantialChildrenUCS(){          // UCS ye göre child oluşturuyoruz


        if(-1<(zeroIndexX-1)){           // burada yukarı gidebilir mi diye bakıyoruz
            Node childrenUp= this.createchild((zeroIndexX-1), zeroIndexY);
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUp.key) )){
                GraphSearch.frontier.add(childrenUp);
                GraphSearch.hasMapExploredSet.put(childrenUp.key,childrenUp.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUp.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUp.key)>childrenUp.getPathCost()){
                    GraphSearch.frontier.add(childrenUp);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUp.key, childrenUp.getPathCost());   // we update same key with new value.
                }
            }

        }

        if(4>(zeroIndexX+1)){           // burada aşağıya gidebilir mi diye bakıyoruz
            Node childrenDown= this.createchild((zeroIndexX+1), zeroIndexY);
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDown.key) )){
                GraphSearch.frontier.add(childrenDown);
                GraphSearch.hasMapExploredSet.put(childrenDown.key,childrenDown.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDown.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDown.key)>childrenDown.getPathCost()){
                    GraphSearch.frontier.add(childrenDown);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDown.key, childrenDown.getPathCost());   // we update same key with new value.
                }
            }

        }
        if(4>(zeroIndexY+1)){           // burada sağa gidebilir miyiz diye baktık
            Node childrenRight= this.createchild((zeroIndexX), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenRight.key) )){
                GraphSearch.frontier.add(childrenRight);
                GraphSearch.hasMapExploredSet.put(childrenRight.key,childrenRight.getPathCost());
                // 	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenRight.key)>childrenRight.getPathCost()){
                    GraphSearch.frontier.add(childrenRight);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenRight.key, childrenRight.getPathCost());   // we update same key with new value.
                }
            }


        }
        if(-1<(zeroIndexY-1)){           // burada sola gidebilir miyiz diye baktık
            Node childrenLeft= this.createchild((zeroIndexX), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key) )){
                GraphSearch.frontier.add(childrenLeft);
                GraphSearch.hasMapExploredSet.put(childrenLeft.key,childrenLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenLeft.key)>childrenLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenLeft);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenLeft.key, childrenLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (-1<(zeroIndexY-1))){    // yukarı-sol köşesi

            Node childrenUpLeft= this.createchild((zeroIndexX-1), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key) )){
                GraphSearch.frontier.add(childrenUpLeft);
                GraphSearch.hasMapExploredSet.put(childrenUpLeft.key,childrenUpLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpLeft.key)>childrenUpLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenUpLeft);
                    //         System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpLeft.key, childrenUpLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((4>(zeroIndexX+1)) && (-1<(zeroIndexY-1))){     // aşagı-sol köşesi
            Node childrenDownLeft= this.createchild((zeroIndexX+1), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key) )){
                GraphSearch.frontier.add(childrenDownLeft);
                GraphSearch.hasMapExploredSet.put(childrenDownLeft.key,childrenDownLeft.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownLeft.key)>childrenDownLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenDownLeft);
                    //        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownLeft.key, childrenDownLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (4>(zeroIndexY+1))){   // yukarı-sağ köşesi
            Node childrenUpRight= this.createchild((zeroIndexX-1), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key) )){
                GraphSearch.frontier.add(childrenUpRight);
                GraphSearch.hasMapExploredSet.put(childrenUpRight.key,childrenUpRight.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpRight.key)>childrenUpRight.getPathCost()){
                    GraphSearch.frontier.add(childrenUpRight);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpRight.key, childrenUpRight.getPathCost());   // we update same key with new value.
                }
            }

        }

        if((4>(zeroIndexX+1)) && (4>(zeroIndexY+1))){         //aşağı-sağ köşesi

            Node childrenDownRight= this.createchild((zeroIndexX+1), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key) )){
                GraphSearch.frontier.add(childrenDownRight);
                GraphSearch.hasMapExploredSet.put(childrenDownRight.key,childrenDownRight.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownRight.key)>childrenDownRight.getPathCost()){
                    GraphSearch.frontier.add(childrenDownRight);
                    ////        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownRight.key, childrenDownRight.getPathCost());   // we update same key with new value.
                }
            }

        }
    }

    public void FindPotantialChildrenHeuristicH1(){


        if(-1<(zeroIndexX-1)){           // burada yukarı gidebilir mi diye bakıyoruz
            Node childrenUp= this.createchild((zeroIndexX-1), zeroIndexY);
            childrenUp.pathCost= GraphSearch.calculationOfHeuristicH1(childrenUp.stateInfo)+childrenUp.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUp.key) )){
                GraphSearch.frontier.add(childrenUp);
                GraphSearch.hasMapExploredSet.put(childrenUp.key,childrenUp.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUp.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUp.key)>childrenUp.getPathCost()){
                    GraphSearch.frontier.add(childrenUp);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUp.key, childrenUp.getPathCost());   // we update same key with new value.
                }
            }

        }

        if(4>(zeroIndexX+1)){           // burada aşağıya gidebilir mi diye bakıyoruz
            Node childrenDown= this.createchild((zeroIndexX+1), zeroIndexY);
            childrenDown.pathCost= GraphSearch.calculationOfHeuristicH1(childrenDown.stateInfo)+childrenDown.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDown.key) )){
                GraphSearch.frontier.add(childrenDown);
                GraphSearch.hasMapExploredSet.put(childrenDown.key,childrenDown.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDown.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDown.key)>childrenDown.getPathCost()){
                    GraphSearch.frontier.add(childrenDown);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDown.key, childrenDown.getPathCost());   // we update same key with new value.
                }
            }

        }
        if(4>(zeroIndexY+1)){           // burada sağa gidebilir miyiz diye baktık
            Node childrenRight= this.createchild((zeroIndexX), (zeroIndexY+1));
            childrenRight.pathCost= GraphSearch.calculationOfHeuristicH1(childrenRight.stateInfo)+childrenRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenRight.key) )){
                GraphSearch.frontier.add(childrenRight);
                GraphSearch.hasMapExploredSet.put(childrenRight.key,childrenRight.getPathCost());
                // 	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenRight.key)>childrenRight.getPathCost()){
                    GraphSearch.frontier.add(childrenRight);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenRight.key, childrenRight.getPathCost());   // we update same key with new value.
                }
            }


        }
        if(-1<(zeroIndexY-1)){           // burada sola gidebilir miyiz diye baktık
            Node childrenLeft= this.createchild((zeroIndexX), (zeroIndexY-1));
            childrenLeft.pathCost= GraphSearch.calculationOfHeuristicH1(childrenLeft.stateInfo)+childrenLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key) )){
                GraphSearch.frontier.add(childrenLeft);
                GraphSearch.hasMapExploredSet.put(childrenLeft.key,childrenLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenLeft.key)>childrenLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenLeft);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenLeft.key, childrenLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (-1<(zeroIndexY-1))){    // yukarı-sol köşesi

            Node childrenUpLeft= this.createchild((zeroIndexX-1), (zeroIndexY-1));
            childrenUpLeft.pathCost= GraphSearch.calculationOfHeuristicH1(childrenUpLeft.stateInfo)+childrenUpLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key) )){
                GraphSearch.frontier.add(childrenUpLeft);
                GraphSearch.hasMapExploredSet.put(childrenUpLeft.key,childrenUpLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpLeft.key)>childrenUpLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenUpLeft);
                    //         System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpLeft.key, childrenUpLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((4>(zeroIndexX+1)) && (-1<(zeroIndexY-1))){     // aşagı-sol köşesi
            Node childrenDownLeft= this.createchild((zeroIndexX+1), (zeroIndexY-1));
            childrenDownLeft.pathCost= GraphSearch.calculationOfHeuristicH1(childrenDownLeft.stateInfo)+childrenDownLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key) )){
                GraphSearch.frontier.add(childrenDownLeft);
                GraphSearch.hasMapExploredSet.put(childrenDownLeft.key,childrenDownLeft.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownLeft.key)>childrenDownLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenDownLeft);
                    //        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownLeft.key, childrenDownLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (4>(zeroIndexY+1))){   // yukarı-sağ köşesi
            Node childrenUpRight= this.createchild((zeroIndexX-1), (zeroIndexY+1));
            childrenUpRight.pathCost= GraphSearch.calculationOfHeuristicH1(childrenUpRight.stateInfo)+childrenUpRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key) )){
                GraphSearch.frontier.add(childrenUpRight);
                GraphSearch.hasMapExploredSet.put(childrenUpRight.key,childrenUpRight.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpRight.key)>childrenUpRight.getPathCost()){
                    GraphSearch.frontier.add(childrenUpRight);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpRight.key, childrenUpRight.getPathCost());   // we update same key with new value.
                }
            }

        }

        if((4>(zeroIndexX+1)) && (4>(zeroIndexY+1))){         //aşağı-sağ köşesi

            Node childrenDownRight= this.createchild((zeroIndexX+1), (zeroIndexY+1));
            childrenDownRight.pathCost= GraphSearch.calculationOfHeuristicH1(childrenDownRight.stateInfo)+childrenDownRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key) )){
                GraphSearch.frontier.add(childrenDownRight);
                GraphSearch.hasMapExploredSet.put(childrenDownRight.key,childrenDownRight.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownRight.key)>childrenDownRight.getPathCost()){
                    GraphSearch.frontier.add(childrenDownRight);
                    ////        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownRight.key, childrenDownRight.getPathCost());   // we update same key with new value.
                }
            }

        }
    }



    public void FindPotantialChildrenHeuristicH2(){




        if(-1<(zeroIndexX-1)){           // burada yukarı gidebilir mi diye bakıyoruz
            Node childrenUp= this.createchild((zeroIndexX-1), zeroIndexY);
            childrenUp.pathCost= GraphSearch.calculationOfHeuristicH2(childrenUp.stateInfo)+childrenUp.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUp.key) )){
                GraphSearch.frontier.add(childrenUp);
                GraphSearch.hasMapExploredSet.put(childrenUp.key,childrenUp.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUp.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUp.key)>childrenUp.getPathCost()){
                    GraphSearch.frontier.add(childrenUp);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUp.key, childrenUp.getPathCost());   // we update same key with new value.
                }
            }

        }

        if(4>(zeroIndexX+1)){           // burada aşağıya gidebilir mi diye bakıyoruz
            Node childrenDown= this.createchild((zeroIndexX+1), zeroIndexY);
            childrenDown.pathCost= GraphSearch.calculationOfHeuristicH2(childrenDown.stateInfo)+childrenDown.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDown.key) )){
                GraphSearch.frontier.add(childrenDown);
                GraphSearch.hasMapExploredSet.put(childrenDown.key,childrenDown.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDown.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDown.key)>childrenDown.getPathCost()){
                    GraphSearch.frontier.add(childrenDown);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDown.key, childrenDown.getPathCost());   // we update same key with new value.
                }
            }

        }
        if(4>(zeroIndexY+1)){           // burada sağa gidebilir miyiz diye baktık
            Node childrenRight= this.createchild((zeroIndexX), (zeroIndexY+1));
            childrenRight.pathCost= GraphSearch.calculationOfHeuristicH2(childrenRight.stateInfo)+childrenRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenRight.key) )){
                GraphSearch.frontier.add(childrenRight);
                GraphSearch.hasMapExploredSet.put(childrenRight.key,childrenRight.getPathCost());
                // 	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenRight.key)>childrenRight.getPathCost()){
                    GraphSearch.frontier.add(childrenRight);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenRight.key, childrenRight.getPathCost());   // we update same key with new value.
                }
            }


        }
        if(-1<(zeroIndexY-1)){           // burada sola gidebilir miyiz diye baktık
            Node childrenLeft= this.createchild((zeroIndexX), (zeroIndexY-1));
            childrenLeft.pathCost= GraphSearch.calculationOfHeuristicH2(childrenLeft.stateInfo)+childrenLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key) )){
                GraphSearch.frontier.add(childrenLeft);
                GraphSearch.hasMapExploredSet.put(childrenLeft.key,childrenLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenLeft.key)>childrenLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenLeft);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenLeft.key, childrenLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (-1<(zeroIndexY-1))){    // yukarı-sol köşesi

            Node childrenUpLeft= this.createchild((zeroIndexX-1), (zeroIndexY-1));
            childrenUpLeft.pathCost= GraphSearch.calculationOfHeuristicH2(childrenUpLeft.stateInfo)+childrenUpLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key) )){
                GraphSearch.frontier.add(childrenUpLeft);
                GraphSearch.hasMapExploredSet.put(childrenUpLeft.key,childrenUpLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpLeft.key)>childrenUpLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenUpLeft);
                    //         System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpLeft.key, childrenUpLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((4>(zeroIndexX+1)) && (-1<(zeroIndexY-1))){     // aşagı-sol köşesi
            Node childrenDownLeft= this.createchild((zeroIndexX+1), (zeroIndexY-1));
            childrenDownLeft.pathCost= GraphSearch.calculationOfHeuristicH2(childrenDownLeft.stateInfo)+childrenDownLeft.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key) )){
                GraphSearch.frontier.add(childrenDownLeft);
                GraphSearch.hasMapExploredSet.put(childrenDownLeft.key,childrenDownLeft.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownLeft.key)>childrenDownLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenDownLeft);
                    //        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownLeft.key, childrenDownLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (4>(zeroIndexY+1))){   // yukarı-sağ köşesi
            Node childrenUpRight= this.createchild((zeroIndexX-1), (zeroIndexY+1));
            childrenUpRight.pathCost= GraphSearch.calculationOfHeuristicH2(childrenUpRight.stateInfo)+childrenUpRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key) )){
                GraphSearch.frontier.add(childrenUpRight);
                GraphSearch.hasMapExploredSet.put(childrenUpRight.key,childrenUpRight.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenUpRight.key)>childrenUpRight.getPathCost()){
                    GraphSearch.frontier.add(childrenUpRight);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpRight.key, childrenUpRight.getPathCost());   // we update same key with new value.
                }
            }

        }

        if((4>(zeroIndexX+1)) && (4>(zeroIndexY+1))){         //aşağı-sağ köşesi

            Node childrenDownRight= this.createchild((zeroIndexX+1), (zeroIndexY+1));
            childrenDownRight.pathCost= GraphSearch.calculationOfHeuristicH2(childrenDownRight.stateInfo)+childrenDownRight.pathCost;
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key) )){
                GraphSearch.frontier.add(childrenDownRight);
                GraphSearch.hasMapExploredSet.put(childrenDownRight.key,childrenDownRight.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key))){
                if(GraphSearch.hasMapExploredSet.get(childrenDownRight.key)>childrenDownRight.getPathCost()){
                    GraphSearch.frontier.add(childrenDownRight);
                    ////        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownRight.key, childrenDownRight.getPathCost());   // we update same key with new value.
                }
            }

        }

    }


    public void FindPotantialChildrenILS(int upperLimit){


        if(-1<(zeroIndexX-1)){           // burada yukarı gidebilir mi diye bakıyoruz
            Node childrenUp= this.createchild((zeroIndexX-1), zeroIndexY);
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUp.key) ) && childrenUp.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenUp);
                GraphSearch.hasMapExploredSet.put(childrenUp.key,childrenUp.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUp.key)) && childrenUp.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenUp.key)>childrenUp.getPathCost()){
                    GraphSearch.frontier.add(childrenUp);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUp.key, childrenUp.getPathCost());   // we update same key with new value.
                }
            }

        }

        if(4>(zeroIndexX+1)){           // burada aşağıya gidebilir mi diye bakıyoruz
            Node childrenDown= this.createchild((zeroIndexX+1), zeroIndexY);
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDown.key) ) && childrenDown.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenDown);
                GraphSearch.hasMapExploredSet.put(childrenDown.key,childrenDown.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDown.key)) && childrenDown.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenDown.key)>childrenDown.getPathCost()){
                    GraphSearch.frontier.add(childrenDown);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDown.key, childrenDown.getPathCost());   // we update same key with new value.
                }
            }

        }
        if(4>(zeroIndexY+1)){           // burada sağa gidebilir miyiz diye baktık
            Node childrenRight= this.createchild((zeroIndexX), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenRight.key)) && childrenRight.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenRight);
                GraphSearch.hasMapExploredSet.put(childrenRight.key,childrenRight.getPathCost());
                // 	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenRight.key)  && childrenRight.pathCost <= upperLimit)){
                if(GraphSearch.hasMapExploredSet.get(childrenRight.key)>childrenRight.getPathCost()){
                    GraphSearch.frontier.add(childrenRight);
                    //	          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenRight.key, childrenRight.getPathCost());   // we update same key with new value.
                }
            }


        }
        if(-1<(zeroIndexY-1)){           // burada sola gidebilir miyiz diye baktık
            Node childrenLeft= this.createchild((zeroIndexX), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key) ) && childrenLeft.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenLeft);
                GraphSearch.hasMapExploredSet.put(childrenLeft.key,childrenLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenLeft.key)) && childrenLeft.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenLeft.key)>childrenLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenLeft);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenLeft.key, childrenLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (-1<(zeroIndexY-1))){    // yukarı-sol köşesi

            Node childrenUpLeft= this.createchild((zeroIndexX-1), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key) ) && childrenUpLeft.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenUpLeft);
                GraphSearch.hasMapExploredSet.put(childrenUpLeft.key,childrenUpLeft.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpLeft.key)) && childrenUpLeft.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenUpLeft.key)>childrenUpLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenUpLeft);
                    //         System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpLeft.key, childrenUpLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((4>(zeroIndexX+1)) && (-1<(zeroIndexY-1))){     // aşagı-sol köşesi
            Node childrenDownLeft= this.createchild((zeroIndexX+1), (zeroIndexY-1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key) )  && childrenDownLeft.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenDownLeft);
                GraphSearch.hasMapExploredSet.put(childrenDownLeft.key,childrenDownLeft.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownLeft.key))  && childrenDownLeft.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenDownLeft.key)>childrenDownLeft.getPathCost()){
                    GraphSearch.frontier.add(childrenDownLeft);
                    //        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownLeft.key, childrenDownLeft.getPathCost());   // we update same key with new value.
                }
            }


        }

        if((-1<(zeroIndexX-1)) && (4>(zeroIndexY+1))){   // yukarı-sağ köşesi
            Node childrenUpRight= this.createchild((zeroIndexX-1), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key) ) && childrenUpRight.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenUpRight);
                GraphSearch.hasMapExploredSet.put(childrenUpRight.key,childrenUpRight.getPathCost());
                //	          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenUpRight.key))  && childrenUpRight.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenUpRight.key)>childrenUpRight.getPathCost()){
                    GraphSearch.frontier.add(childrenUpRight);
                    //          System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenUpRight.key, childrenUpRight.getPathCost());   // we update same key with new value.
                }
            }

        }

        if((4>(zeroIndexX+1)) && (4>(zeroIndexY+1))){         //aşağı-sağ köşesi

            Node childrenDownRight= this.createchild((zeroIndexX+1), (zeroIndexY+1));
            if(!(GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key) )  && childrenDownRight.pathCost <= upperLimit){
                GraphSearch.frontier.add(childrenDownRight);
                GraphSearch.hasMapExploredSet.put(childrenDownRight.key,childrenDownRight.getPathCost());
                //          System.out.println("up için child oluşturuldu");
            }// burda childlar oluşmadan önce bakılcak
            else if((GraphSearch.hasMapExploredSet.containsKey(childrenDownRight.key)) && childrenDownRight.pathCost <= upperLimit){
                if(GraphSearch.hasMapExploredSet.get(childrenDownRight.key)>childrenDownRight.getPathCost()){
                    GraphSearch.frontier.add(childrenDownRight);
                    ////        System.out.println("up için child oluşturuldu - bu listede vardı ama costu daha küçük olduğu için ekledik");
                    GraphSearch.hasMapExploredSet.replace(childrenDownRight.key, childrenDownRight.getPathCost());   // we update same key with new value.
                }
            }

        }


    }



    public void generateKey(int mat[][]){
        for (int[] row : mat)
            key=Arrays.toString(row)+key;
    }

}
