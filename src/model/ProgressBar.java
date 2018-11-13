/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.JProgressBar;

/**
 *
 * @author MyPC
 */
public class ProgressBar extends Thread{
    
    
    JProgressBar progressBar;
    int count;
    int all;

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
    public ProgressBar(JProgressBar progressBar){
        this.progressBar = progressBar;
    }
    
    @Override
    public void run(){
        while(true){
            int sum = 0;
            if(all != 0){
                sum = count*100/all;
            }
            progressBar.setValue(sum);
            if(sum == 100){
                break;
            }
        }
    }
}
