package newElevator;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ELThread implements Runnable {
   private int up=1, down=-1,stop=0;                   
   private int floorNum;
   private int direction;    //电梯运行方向
   private int current;      //电梯当前楼层
   private boolean []levelState;
   private int aim;          //电梯目的楼层
   private Thread thread;
   JPanel pane=new JPanel();
   YButton [] levelBtn;       //电梯内部按钮
   YButton [] floorBtn;       //电梯楼层图
   public ELThread(){
	   floorNum=20;
	   direction=stop;
	   current=0;
	   aim=0;
	   levelState=new boolean[floorNum];
	   for(int i=0;i<floorNum;i++){
		   levelState[i]=false;
	   }
	   thread=new Thread(this);
	   pane.setLayout(new GridLayout(floorNum,2));
	   levelBtn=new YButton[floorNum];
	   floorBtn=new YButton[floorNum];
	   
	   ActionListener ac=new Actionhandler();
	   for(int i=floorNum-1;i>=0;i--){
		   levelBtn[i]=new YButton();
		   int name=i+1;
		   levelBtn[i].setText(""+name);
		   levelBtn[i].setBackground(Color.YELLOW);
		   levelBtn[i].addActionListener(ac);
		   floorBtn[i]=new YButton();
		   floorBtn[i].setBackground(Color.blue);
		   floorBtn[i].addActionListener(ac);
		   pane.add(levelBtn[i]);
		   pane.add(floorBtn[i]);
	   }
	   floorBtn[current].setBackground(Color.red);                      //初始化电梯的控件布局
   }
   class Actionhandler implements ActionListener{                       //电梯内按钮的监听事件
	   public void actionPerformed(ActionEvent e){
		   for(int i=0;i<floorNum;i++){
			   if(e.getSource()==levelBtn[i]){
				   levelState[i]=true;
				   levelBtn[i].setBackground(Color.green);
				   if(direction==stop){                                   //电梯是停止状态则直接以点击位置作为目的地
					   aim=i;
				   }
				   if(direction==up){                                     //电梯为上升状态则找到上升的最高楼层作为目的地
					   aim=findUpNext();//找到上升的最远楼层
				   }
				   if(direction==down){                                   //电梯为下降状态则找到下降的最低楼层作为目的地
					  aim=findDownNext(); //找到下降的最远楼层
				   }
			   }
		   }
	   }
   }
   public void run(){
	   	while(true){
	   		try{Thread.sleep(1000);}
	   	catch(InterruptedException e){}
	   		if(direction==up||direction==down){
	   			try{Thread.sleep(1000);
	   			}	catch(InterruptedException e){}
	   			direction=stop;
	   		}
	   		if(aim>current){    
	   			direction=up;
	   			moveUp();
	   			direction=stop;
	   		}
	   		else if(aim<current){
	   			direction=down;
	   			moveDown();
	   			direction=stop;
	   		}
	   	}
   }
   public void moveUp(){                            //电梯上升
	   int nowLevel=current; 
	   for(int i=current+1;i<=aim;i++){ 
		   try{Thread.sleep(500);
		   	floorBtn[i].setBackground(Color.red);
		   	floorBtn[i-1].setBackground(Color.blue);
		   	if(levelState[i]==true){
		   		Thread.sleep(700);
		   		levelState[i]=false;
		   		levelBtn[i].setBackground(Color.YELLOW);
		   }
		   	current=i;
		  
		   }catch(InterruptedException e){}
	   }
	   moveAfterUp();
   }
   public void moveAfterUp(){                 //上升后反方向的补充动作（下降）
	   for(int i=0;i<20;i++){
		   if(levelState[i]==true){
			   aim=i;
			   moveDown();
		   }
	   }
   }
   public void moveAfterDown(){              //下降后反方向的补充动作（上升）
	   for(int i=0;i<20;i++){
		   if(levelState[i]==true){
			   aim=i;
			   moveUp();
		   }	
	   }
   }
   public void moveDown(){                             //电梯下降
	   int nowLevel=current;
	   for(int i=current-1;i>=aim;i--){
		   try{Thread.sleep(500);
		   	floorBtn[i].setBackground(Color.red);
		   	floorBtn[i+1].setBackground(Color.blue);
		  
		   	if(levelState[i]==true){
		   		Thread.sleep(700);
		   		levelState[i]=false;
		   		levelBtn[i].setBackground(Color.YELLOW);
		   	}
		   	current=i;
		   }catch(InterruptedException e){}
		   
	   }
	   moveUp();
	   
   }   
   public int  findUpNext(){                          //找到上升的最高楼层
	   int next=0;
	   for(int i=floorNum-1;i>=0;i--){
		   if(levelState[i]==true){
			   next=i;
			   break;
		   }
	   }
	   return next;
   }
    
   public int findDownNext(){                            //找到下降的最低楼层
	   int next=0;
	   for(int i=0;i<=floorNum-1;i++){
		   if(levelState[i]==true){
			   next=i;
			   break;
		   }
	   }
	   return next;
   }
   public Thread retThread(){             
	   return thread;
   }
   public boolean isUp(){
	   return direction==up;
   }
   public boolean isDown(){
	   return direction==down;
   }
   public boolean isStop(){
	   return direction==stop;
   }public int currentLevel(){
	   return current;
   }
   public void termination(int level){           //跟View.java对应。在其找到相应楼层时，用该函数改变电梯线程的状态和目的地，实现外部控制电梯运行
	   
	   if(direction==stop){
		   aim=level;
		   levelState[level]=true;
		   if(current<level){
			   direction=up;
		   }
		   if(current>level){
			   direction=down;
		   }
	   }
	   if(direction==up&&level>aim){
		   aim=level;
		   levelState[level]=true;
	   }
	   if(direction==down&&level<aim){
		   aim=level;
		   levelState[level]=true;
	   }
	   
   }
}
