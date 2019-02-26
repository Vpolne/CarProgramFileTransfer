package com.marykay.CarProgramFileTransfer;

import javax.swing.*;
import java.awt.*;

public class FileTransfer {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FileTransferForm fileTransferForm = new FileTransferForm();
        fileTransferForm.pack();
        fileTransferForm.setSize(400,400);
        fileTransferForm.setVisible(true);

    }
}
