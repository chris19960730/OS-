package newElevator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame implements Runnable {
     private int floorNum=20;     //20层
     private int elevatorNum=5;    //五部电梯
     
     private ELThread[] elThread;  //创建电梯线程的数组
     
     Container mainView;            //主界面容器
     JPanel sidePane=new JPanel();     //放置电梯外部按钮的容器
     YButton [] upBtn;                 //外部上升按钮
     YButton [] downBtn;			 //外部下降按钮
     int [] upState; 				  //上升按钮状态
     int [] downState;                 //下降按钮状态
     public View(){                              
    	 elThread=new ELThread[5];        
    	 mainView=this.getContentPane();
    	 GridLayout gird=new GridLayout(1,6);
    	 mainView.setLayout(gird);
    	 GridLayout Level=new GridLayout(20,2);
    	 sidePane.setLayout(Level);
    	 upBtn=new YButton[floorNum];
    	 downBtn=new YButton[floorNum];
    	 ActionListener ac=new Actionhandler();
    	 for(int i=upBtn.length-1;i>=0;i--){
    		 upBtn[i]=new YButton();
    		 upBtn[i].setText("UP");
    		 upBtn[i].setBackground(Color.white);
    		 upBtn[i].addActionListener(ac);
    		 downBtn[i]=new YButton();
    		 downBtn[i].setText("DOWN");
    		 downBtn[i].setBackground(Color.white);
    		 downBtn[i].addActionListener(ac);
    		 sidePane.add(upBtn[i]);
    		 sidePane.add(downBtn[i]);
    	 }
    	mainView.add(sidePane);                      //以上，外部按钮初始化
    	
    	upState=new int[floorNum];
    	downState=new int [floorNum];
    	
    	for(int i=0;i<floorNum;i++){               //初始化楼层的目的地为空
    		upState[i]=0;
    		downState[i]=0;                    
    	}
    	for(int i=0;i<5;i++){                   //将五个电梯线程添加进主界面容器
    		ELThread el=new ELThread();
    		mainView.add(el.pane);
    		el.retThread().start();
    		elThread[i]=el;
    	}
    	Thread floorThread=new Thread(this);                  //该线程管理其余线程运行
    	floorThread.start();
     }
     class Actionhandler implements ActionListener{                    //外部按钮监听事件
    	  public void actionPerformed(ActionEvent e){
    		  for(int i=0;i<floorNum;i++){
    			  if(e.getSource()==upBtn[i]){
    				  upBtn[i].setBackground(Color.red);
    				  upState[i]=1;
    			  }
    			  if(e.getSource()==downBtn[i]){	
    				  downBtn[i].setBackground(Color.red);
    				  downState[i]=1;
    			  }
    		  }
    	  }
     }
    public int totalFloor(){
    	return floorNum;
    }
    
    public void run(){                            
    	while(true){
    		try{Thread.sleep(500);
    	}catch(InterruptedException e){	}
    		for(int i=0;i<floorNum;i++){
    			if(upState[i]==1){                                       //外部按钮响应
    				moveUp(i);
    			}
    			if(upState[i]>=5){                                          //外部按钮复原
    				if(i==elThread[upState[i]-5].currentLevel()){
    					upState[i]=0;
    					upBtn[i].setBackground(Color.white);
    				}
    			}
    		}
    		
    		for(int i=0;i<floorNum;i++){
    			if(downState[i]==1){ 
    				moveDown(i);
    			}
    			if(downState[i]>=5){
    				if(i==elThread[downState[i]-5].currentLevel()){
    					downState[i]=0;
    					downBtn[i].setBackground(Color.white);
    				}
    			}
    		}
    		
    	}
    }
    public void moveUp(int level){                                //上升的调度
    	int aimEL=0; 
    	int distance=20;
    	for(int i=0;i<5;i++){
    		if(elThread[i].isStop()||(elThread[i].isUp()&&level>=elThread[i].currentLevel())){
    			int count=Math.abs(level-elThread[i].currentLevel());
    			if(count<distance){                                                 //找到距离最短的符合条件的电梯
    				aimEL=i;
    				distance=Math.abs(level-elThread[i].currentLevel());
    			}
    		}
    	}
    	if(distance!=20){
    		upState[level]=5+aimEL;
    		elThread[aimEL].termination(level);
    	}
    }
    
    public void moveDown(int level){                              //下降的调度
    	int aimEL=0;
    	int distance=20;
    	for(int i=0;i<5;i++){
    		if(elThread[i].isStop()||(elThread[i].isDown()&&level<=elThread[i].currentLevel())){
    			int count=Math.abs(level-elThread[i].currentLevel());
    			if(count<distance){                                              //找到距离最短的符合条件的电梯
    				aimEL=i;
    				distance=Math.abs(level-elThread[i].currentLevel());
    			}
    		}
    	}
    	if(distance!=20){
    		downState[level]=5+aimEL;
    		elThread[aimEL].termination(level);
    	}
    }
}
