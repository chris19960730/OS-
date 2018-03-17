package newElevator;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ELThread implements Runnable {
   private int up=1, down=-1,stop=0;                   
   private int floorNum;
   private int direction;    //�������з���
   private int current;      //���ݵ�ǰ¥��
   private boolean []levelState;
   private int aim;          //����Ŀ��¥��
   private Thread thread;
   JPanel pane=new JPanel();
   YButton [] levelBtn;       //�����ڲ���ť
   YButton [] floorBtn;       //����¥��ͼ
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
	   floorBtn[current].setBackground(Color.red);                      //��ʼ�����ݵĿؼ�����
   }
   class Actionhandler implements ActionListener{                       //�����ڰ�ť�ļ����¼�
	   public void actionPerformed(ActionEvent e){
		   for(int i=0;i<floorNum;i++){
			   if(e.getSource()==levelBtn[i]){
				   levelState[i]=true;
				   levelBtn[i].setBackground(Color.green);
				   if(direction==stop){                                   //������ֹͣ״̬��ֱ���Ե��λ����ΪĿ�ĵ�
					   aim=i;
				   }
				   if(direction==up){                                     //����Ϊ����״̬���ҵ����������¥����ΪĿ�ĵ�
					   aim=findUpNext();//�ҵ���������Զ¥��
				   }
				   if(direction==down){                                   //����Ϊ�½�״̬���ҵ��½������¥����ΪĿ�ĵ�
					  aim=findDownNext(); //�ҵ��½�����Զ¥��
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
   public void moveUp(){                            //��������
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
   public void moveAfterUp(){                 //�����󷴷���Ĳ��䶯�����½���
	   for(int i=0;i<20;i++){
		   if(levelState[i]==true){
			   aim=i;
			   moveDown();
		   }
	   }
   }
   public void moveAfterDown(){              //�½��󷴷���Ĳ��䶯����������
	   for(int i=0;i<20;i++){
		   if(levelState[i]==true){
			   aim=i;
			   moveUp();
		   }	
	   }
   }
   public void moveDown(){                             //�����½�
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
   public int  findUpNext(){                          //�ҵ����������¥��
	   int next=0;
	   for(int i=floorNum-1;i>=0;i--){
		   if(levelState[i]==true){
			   next=i;
			   break;
		   }
	   }
	   return next;
   }
    
   public int findDownNext(){                            //�ҵ��½������¥��
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
   public void termination(int level){           //��View.java��Ӧ�������ҵ���Ӧ¥��ʱ���øú����ı�����̵߳�״̬��Ŀ�ĵأ�ʵ���ⲿ���Ƶ�������
	   
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
