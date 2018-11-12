/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author MyPC
 */
public class ServerDirectory implements Serializable{
    private File[] listFiles;

    public ServerDirectory(File[] listFiles) {
        this.listFiles = listFiles;
    }

    public File[] getListFiles() {
        return listFiles;
    }

    public void setListFiles(File[] listFiles) {
        this.listFiles = listFiles;
    }
}
