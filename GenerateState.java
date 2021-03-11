import java.util.ArrayList;
import java.util.Arrays;

public class GenerateState {
    private int solutionDepth;
    private int zeroIndexX;
    private int zeroIndexY;
    private int[][] currentState = new int[4][4];
    private String currentKey;
    public static ArrayList<String> LookedState = new ArrayList<String>();


    //burada her zamanki gibi yap öncekiler gibi sadece tek child üzerinden gideceksin onu hesapla
// for döngüsü koy depth leveline göre dönsün, random gitmeli bunun için bir şey düşün.
    GenerateState(int solutionDepth){
        this.solutionDepth = solutionDepth;
        int[][] goalState={ { 1, 2, 3, 4 },{ 12, 13, 14, 5 },{11, 0, 15, 6 },{ 10, 9, 8, 7}  };

        int i,j;
        for(i=0; i<currentState.length; i++)
            for(j=0; j<currentState[i].length; j++)
                currentState[i][j]=goalState[i][j];              // burada direk goal hedefi alıyoruz.

        String tempGoalKey=generateKey(currentState);       // direk onun anahtarını başlar başlamaz listenin başına koyduk
        LookedState.add(tempGoalKey);
        findZero();
    }


    public int[][] start(){
        int i;
        for(i=0;i<this.solutionDepth;i++){
            selectPotantialNode(findPotantialNode());
        }
        return this.currentState;
    }

    public void findZero(){      // zeronun konumunu bulmaya yarıyor.
        for(int i=0; i<=3; i++) {
            for(int j=0; j<=3; j++) {
                if(currentState[i][j]==0) {                        // sıfırın yerini arıyoruz
                    this.zeroIndexX = i;
                    this.zeroIndexY = j;
                    break;
                }
            }
        }

    }
    public void generateCurrentKey(int mat[][]){

        for (int[] row : mat)
            this.currentKey=Arrays.toString(row)+currentKey;
    }

    public int[][] checkNextState(int x, int y){
        int temp[][] = new int[4][4];
        for(int i=0; i<currentState.length; i++)
            for(int j=0; j<currentState[i].length; j++)
                temp[i][j]=currentState[i][j];                    // burada birebir aynısını oluşturdu child ile


        temp[zeroIndexX][zeroIndexY]= temp[x][y];   // burada yer değiştirme yapıyoruz. mesela 5 ile 0 i yer değiştircez. sıfırın oraya
        temp[x][y]=0;           //oraya 5 i, 5 in oraya da sıfırı koyuyoruz.
        return temp;
    }

    public void acceptNextState(int x, int y){
        int temp[][] = new int[4][4];
        for(int i=0; i<currentState.length; i++)
            for(int j=0; j<currentState[i].length; j++)
                temp[i][j]=currentState[i][j];                    // burada birebir aynısını oluşturdu child ile


        temp[zeroIndexX][zeroIndexY]= temp[x][y];   // burada yer değiştirme yapıyoruz. mesela 5 ile 0 i yer değiştircez. sıfırın oraya
        temp[x][y]=0;           //oraya 5 i, 5 in oraya da sıfırı koyuyoruz.
        int i,j;
        for(i=0; i<currentState.length; i++)
            for(j=0; j<currentState[i].length; j++)
                currentState[i][j]=temp[i][j];                    // burada current state'i güncelledik bir sonraki adım için

//	System.out.println("accepstate bitinde böyle");
//	printState(currentState);
//	System.out.println("Bitti");
        findZero();	  // sıfırın konumunu güncelledik.
        String puttingKeyOnTheList = generateKey(currentState);
        LookedState.add(puttingKeyOnTheList);

    }



    public String generateKey(int mat[][]){
        String checkKey = null;
        for (int[] row : mat)
            checkKey=Arrays.toString(row)+checkKey;

        return checkKey;
    }
    public int findPotantialNode(){
        int[] holdAvaliableNode = {0,0,0,0,0,0,0,0};



        if(-1<(zeroIndexX-1)){           // burada yukarı gidebilir mi diye bakıyoruz
            int temp[][] = checkNextState((zeroIndexX-1),zeroIndexY);
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[0]=1;
            }

        }

        if(4>(zeroIndexX+1)){           // burada aşağıya gidebilir mi diye bakıyoruz
            int temp[][] = checkNextState((zeroIndexX+1),zeroIndexY);
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[1]=1;
            }

        }
        if(4>(zeroIndexY+1)){           // burada sağa gidebilir miyiz diye baktık

            int temp[][] = checkNextState(zeroIndexX, (zeroIndexY+1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[2]=1;
            }
        }
        if(-1<(zeroIndexY-1)){           // burada sola gidebilir miyiz diye baktık
            int temp[][] = checkNextState(zeroIndexX, (zeroIndexY-1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[3]=1;
            }

        }

        if((-1<(zeroIndexX-1)) && (-1<(zeroIndexY-1))){    // yukarı-sol köşesi
            int temp[][] = checkNextState((zeroIndexX-1), (zeroIndexY-1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[4]=1;
            }
        }

        if((4>(zeroIndexX+1)) && (-1<(zeroIndexY-1))){     // aşagı-sol köşesi
            int temp[][] = checkNextState((zeroIndexX+1), (zeroIndexY-1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[5]=1;
            }
        }

        if((-1<(zeroIndexX-1)) && (4>(zeroIndexY+1))){   // yukarı-sağ köşesi
            int temp[][] = checkNextState((zeroIndexX-1), (zeroIndexY+1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[6]=1;
            }
        }

        if((4>(zeroIndexX+1)) && (4>(zeroIndexY+1))){         //aşağı-sağ köşesi
            int temp[][] = checkNextState((zeroIndexX+1), (zeroIndexY+1));
            String keyChecker= generateKey(temp);
            if(!(LookedState.contains(keyChecker))){
                holdAvaliableNode[7]=1;
            }
        }

        int i=0;
        int rand=0;
        for(i=0;i<100;i++){                     // burada uygun olan gidilebileceği adımlardan birini seçiyoruz.
            rand = (int)(Math.random() * 8);

            if(holdAvaliableNode[rand] == 1){
                //		System.out.println("Hareket diagramı:0-Yukarı: 1-Aşagıya: 2-sağa: 3-sola: ");
                //	System.out.println("Hareket diagramı:4-Yukarı-sol: 5-Aşagı-sol: 6-yukarı-sağa: 3-aşağı-sol: ");
                //		System.out.println("Yapılacak adım: "+rand);

                return rand;

            }

        }

//	System.out.println("generateState de sorun var, for döngüsünün içinden çıktı -for döngüsünde eğer bitmez ise bu yazıya bakar");
        return rand;

    }

    public void selectPotantialNode(int rand){


        if(rand == 0){           // burada yukarı gidebilir mi diye bakıyoruz
            acceptNextState((zeroIndexX-1),zeroIndexY);
        }

        if(rand == 1){           // burada aşağıya gidebilir mi diye bakıyoruz
            acceptNextState((zeroIndexX+1),zeroIndexY);
        }
        if(rand == 2){           // burada sağa gidebilir miyiz diye baktık
            acceptNextState(zeroIndexX, (zeroIndexY+1));

        }
        if(rand == 3){           // burada sola gidebilir miyiz diye baktık
            acceptNextState(zeroIndexX, (zeroIndexY-1));
        }

        if(rand == 4){    // yukarı-sol köşesi
            acceptNextState((zeroIndexX-1), (zeroIndexY-1));
        }

        if(rand == 5){     // aşagı-sol köşesi
            acceptNextState((zeroIndexX+1), (zeroIndexY-1));
        }

        if(rand == 6){   // yukarı-sağ köşesi
            acceptNextState((zeroIndexX-1), (zeroIndexY+1));
        }

        if(rand == 7){         //aşağı-sağ köşesi
            acceptNextState((zeroIndexX+1), (zeroIndexY+1));
        }

    }
    public static void printState(int mat[][])
    {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
    }

}
