import java.util.Scanner;

public class test {
	
	public static void main(String[] args){
		
		
		Scanner in= new Scanner(System.in);
		
		System.out.println("请输入月份？");
		
		int yuefen=in.nextInt();
		
		int[][] numseven=new int[][]{{1,3,5,7,8,10,12},{4,6,9,11},{2}};
		
		//int[] arr_1={1,3,5,7,8,10,12};
		//int[] arr_2={4,6,9,11};
		//int[] arr_3={2};
		
		//System.out.println(arr_1.length);
		
		String marks = "";
		for(int i=0;i<numseven.length;i++){
		for(int y=0;y<numseven[i].length;y++){
			if(yuefen==numseven[i][y]){
				
				switch(numseven[i].length){
					case 7:
					marks="这个月有31天刀";
					break;
					case 4:
					marks="这个月有30天";
					break;
					case 1:
					marks="这个月有28天";
					break;
				}
				System.out.println(marks);
			}
		}
		}
		
	}
	
}
