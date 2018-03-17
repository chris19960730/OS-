package newElevator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame implements Runnable {
     private int floorNum=20;     //20��
     private int elevatorNum=5;    //�岿����
     
     private ELThread[] elThread;  //���������̵߳�����
     
     Container mainView;            //����������
     JPanel sidePane=new JPanel();     //���õ����ⲿ��ť������
     YButton [] upBtn;                 //�ⲿ������ť
     YButton [] downBtn;			 //�ⲿ�½���ť
     int [] upState; 				  //������ť״̬
     int [] downState;                 //�½���ť״̬
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
    	mainView.add(sidePane);                      //���ϣ��ⲿ��ť��ʼ��
    	
    	upState=new int[floorNum];
    	downState=new int [floorNum];
    	
    	for(int i=0;i<floorNum;i++){               //��ʼ��¥���Ŀ�ĵ�Ϊ��
    		upState[i]=0;
    		downState[i]=0;                    
    	}
    	for(int i=0;i<5;i++){                   //����������߳���ӽ�����������
    		ELThread el=new ELThread();
    		mainView.add(el.pane);
    		el.retThread().start();
    		elThread[i]=el;
    	}
    	Thread floorThread=new Thread(this);                  //���̹߳��������߳�����
    	floorThread.start();
     }
     class Actionhandler implements ActionListener{                    //�ⲿ��ť�����¼�
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
    			if(upState[i]==1){                                       //�ⲿ��ť��Ӧ
    				moveUp(i);
    			}
    			if(upState[i]>=5){                                          //�ⲿ��ť��ԭ
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
    public void moveUp(int level){                                //�����ĵ���
    	int aimEL=0; 
    	int distance=20;
    	for(int i=0;i<5;i++){
    		if(elThread[i].isStop()||(elThread[i].isUp()&&level>=elThread[i].currentLevel())){
    			int count=Math.abs(level-elThread[i].currentLevel());
    			if(count<distance){                                                 //�ҵ�������̵ķ��������ĵ���
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
    
    public void moveDown(int level){                              //�½��ĵ���
    	int aimEL=0;
    	int distance=20;
    	for(int i=0;i<5;i++){
    		if(elThread[i].isStop()||(elThread[i].isDown()&&level<=elThread[i].currentLevel())){
    			int count=Math.abs(level-elThread[i].currentLevel());
    			if(count<distance){                                              //�ҵ�������̵ķ��������ĵ���
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
