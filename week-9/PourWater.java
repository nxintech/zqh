package com.nxin.zqh.weekeight;

/**
 * Created by lyf
 * on 2016/6/7.
 */
public class PourWater {
    public static void main(String[] args) {
        pourWaterByMod(3, 5, 4);
        pourWaterByMod(7, 11, 2);
    }

    public static void pourWaterByMod(int x,int y ,int target){
        int maxDivisor=getMaxDivisor(x,y);
        if(target % maxDivisor!=0){
            System.out.println("can't");
            return;
        }
        int xwater=0;
        int ywater=0;
        while(true){
            if(xwater==0){
                xwater=x;
                System.out.printf("A=%s ; B=%s \n",xwater,ywater);
            }else{
                //Y的剩余空间可以倒入X不溢出
                if(xwater<=y-ywater){
                    ywater=ywater+xwater;
                    xwater=0;
                    System.out.printf("A=%s ; B=%s \n", xwater, ywater);
                    if(ywater==target){
                        System.out.println("get the target \n");
                        break;
                    }
                }else{//y剩余空间不满足x倒入
                    xwater=xwater-(y-ywater);
                    ywater=y;
                    System.out.printf("A=%s ; B=%s \n",xwater,ywater);
                    if(xwater==target){
                        System.out.println("get the target \n");
                        break;
                    }
                    //倒掉Y
                    ywater=0;
                    System.out.printf("A=%s ; B=%s \n",xwater,ywater);
                }
            }
        }
    }


    static int getMaxDivisor(int x,int y){
        if(x<y){
            int temp=x;
            x=y;
            y=temp;
        }
        while(x % y!= 0){
            int temp = x % y;
            x = y;
            y = temp;
        }
        return y;
    }

}
