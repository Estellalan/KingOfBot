package com.kob.backend1.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend1.consumer.WebSocketServer;
import com.kob.backend1.mapper.RecordMapper;
import com.kob.backend1.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;

    final int[][] g;

    private final static int[] dx = {-1, 0, 1, 0},dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";//playing表示正在进行中，finished表示结束
    private String loser = ""; //all:平局 A:A输 B:B输

    public  Game(Integer rows, Integer cols, Integer inner_walls_count,Integer idA, Integer idB){
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA(){
        return this.playerA;
    }

    public Player getPlayerB(){
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }
    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for(int i = 0; i < 4;i ++){
            int x = sx + dx[i], y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0){
                if(check_connectivity(x, y, tx, ty)){
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        return  false;
    }
    private boolean draw(){//画地图
        for(int i = 0; i < this.rows; i ++){
            for(int j = 0; j < this.cols; j ++){
                g[i][j] = 0;
            }
        }

        for(int r = 0; r < this.rows; r ++){
            g[r][0] = g[r][this.cols-1] = 1;
        }

        for(int c = 0; c < this.cols; c ++){
            g[0][c] = g[this.rows-1][c] = 1;
        }

        Random random = new Random();
        for(int i = 0; i < this.inner_walls_count/2; i ++){
            for(int j = 1; j < 1000; j ++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);;

                if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;

                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - r - 1][this.cols - c - 1] = 1;
                break;
            }
        }

        return check_connectivity(this.rows -2,1,1,this.cols - 2);

    }

    public void createMap(){
        for(int i = 0;i <= 1000; i ++){
            if(draw()){
                break;
            }
        }
    }

    public boolean nextStep() { //等待两名玩家的下一步操作

        try {
            Thread.sleep(200);//为了保证前端刷新完，前端的蛇是每200ms走一格
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0 ; i < 50; i ++){
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        System.out.println("*******"+nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if(g[cell.x][cell.y] == 1) return false;

        for(int i = 0;i < n - 1; i ++){
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y){
                return  false;
            }
        }

        for(int i = 0; i < n - 1; i ++){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y){
                return false;
            }
        }

        return true;
    }

    private void judge() { //判断两名玩家下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if(!validA || !validB){
            status = "finished";

            if(!validA && !validB){
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private void sendAllMessage(String message){
        if(WebSocketServer.users.get(playerA.getId()) != null)WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove(){ //向两个Client传递移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }

    }

    private String getMapString(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; i ++){
            for(int j = 0; j < cols; j ++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

            WebSocketServer.recordMapper.insert(record);

    }

    private void sendResult(){ //向两个Client发送结果
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        sendAllMessage(resp.toJSONString());
        saveToDatabase();
    }

    /**
     * 当我们开启一个新线程的时,我们线程的入口函数即为run()函数
     */
    @Override
    public void run() {
        //super.run();
        for(int i = 0; i < 1000; i ++){
            System.out.println(status);
            if(nextStep()){//是否获取了两条蛇的下一步操作
                judge();
                //System.out.println("线程开始运行");
                if(status.equals("playing")){
                   //System.out.println("开始move");
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                //System.out.println("没有获取所有操作！！！！");
                status = "finished";
                lock.lock();
                try {
                    //System.out.println(nextStepA+" "+nextStepB);
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    } else if (nextStepA  == null) {
                        loser = "A";
                    } else if(nextStepB == null){
                        loser = "B";
                    } else {
                        //System.out.println("结果有误！！！！！");
                    }
                } finally {
                    lock.unlock();
                }

                sendResult();
                break;
            }
        }
    }

}
