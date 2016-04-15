package test.study;

/**
 * 排序类
 * Created by Administrator on 2016/4/14.
 */
public class TestSort {

    public static void main(String[] args) {
        Integer[] array = new Integer[]{10,8,8,29,16,8,23,14,100,1,2,7,5,45,4};
        int len = array.length;
        //简单插入排序
        System.out.println("简单插入排序");
        insertSort(array, 0, len);
        toStr(array);
        System.out.println("===========");
        //归并排序
        int i = 1;
        Boolean flag = true;
        while (flag) {
            i = i * 2;
            if (i >= len) {
                i = len;
                flag = false;
            }
            System.out.println("归并排序");
            mergerSort(array, i);
            toStr(array);

        }


    }

    /**
     * 插入排序
     * @param flag
     * @return
     */
    public static void insertSort(Integer[] flag, int start, int end) {
        int len = end;
        int j;
        int k;
        int stamp;
        for (int i = start;i < len; i++) {
            k = i;
            for (j = i + 1; j < len; j++) {
                if (flag[j] < flag[k]) {
                    k = j;
                }
            }
            //交换值
            if (i != k) {
                stamp = flag[i];
                flag[i] = flag[k];
                flag[k] = stamp;
            }
        }
    }

    /**
     * 归并排序
     * @param flag
     * @param step
     * @return
     */
    public static void mergerSort(Integer[] flag, Integer step) {
        int len = flag.length;
        int start = 0;
        int end = step;
        for (int i = 0;i < len; i++) {
            insertSort(flag, start, end);

            start = i * step;
            end = (i + 1)*step;
            if (end >= len) {
                end = len;
            }
        }

    }

    public static void toStr(Integer[] flag) {
        int len = flag.length;
        for (int i = 0; i < len; i++) {
            System.out.print(flag[i] + ",");
        }
    }
}
