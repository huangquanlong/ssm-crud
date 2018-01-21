package com.gussGame;

/**
 * @Title: GussGame 
* @Description:  执行猜游戏
* @date 2017年4月16日 
 */
public class GussGame {
	Player p1=null;
	Player p2=null;
	Player p3=null;
  public void StartGame(){
		p1=new Player();
		p2=new Player();
		p3=new Player();
		int rightNumber=(int) (Math.random()*1000000);
		System.out.println("==========rightNumber is:"+rightNumber);
	/*while(true){
		int	gussNumber1=p1.gussNumber();
		System.out.println("gussNumber1 is:"+gussNumber1);
		int	gussNumber2=p2.gussNumber();
		System.out.println("gussNumber2 is:"+gussNumber2);
		int	gussNumber3=p3.gussNumber();
		System.out.println("gussNumber3 is:"+gussNumber3);
		if(gussNumber1==rightNumber){
			System.out.println("=======================gussNumber1 is winner:"+gussNumber1);
			break;
		}else if (gussNumber2==rightNumber) {
			System.out.println("=======================gussNumber2 is winner:"+gussNumber2);
			break;
		}else if(gussNumber3==rightNumber){
			System.out.println("=======================gussNumber3 is winner:"+gussNumber3);
			break;
		}
		System.out.println("=================game is continue,please try again。");
	}
	System.out.println("=================game over");*/
	}
}
