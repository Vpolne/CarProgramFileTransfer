package com.marykay.CarProgramFileTransfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.*;
import javax.swing.JFileChooser;

public class FileTransferForm extends JFrame {
    private JPanel panel1;
    private JPanel rootPanel;
    private JButton sourceChoise;
    private JButton destinationChoise;
    private JTextField destinationPath;
    private JTextField sourcePath;
    private JTextField fileNameInput;
    private JLabel fileName;
    private JTextArea logText;
    private JScrollPane logScrollPane;
    private JButton transferButton;
    private JButton helpButton;
    private JProgressBar transferProgressBar;
    private JLabel folderName;
    private JTextField folderNameInput;
    private JButton clearLogButton;
    String fileNameInputString;
    String folderNameInputString;

    public FileTransferForm() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel1.setOpaque(true);
        rootPanel.setOpaque(true);
        add(rootPanel);
        setTitle("Перенос файлов НКК (Автопрограмма)");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setLocation(screenWidth / 2 - 150, screenHeight / 2 - 150);
        fileNameInput.setText("ВЫБЕРИ ИМЯ ФАЙЛА");
        folderNameInput.setText("ВЫБЕРИ ИМЯ ПАПКИ");

        // Настройка логов
        Font logFont = new Font("Verdana", Font.PLAIN, 12);
        logText.setFont(logFont);
        // Выбор директории, из которой копировать файлы
        JFileChooser sourceDirectoryChooser = new JFileChooser();

        sourceChoise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceDirectoryChooser.setCurrentDirectory(new java.io.File("."));
                sourceDirectoryChooser.setDialogTitle("Папка, из которой копировать");
                sourceDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                sourceDirectoryChooser.setAcceptAllFileFilterUsed(false);
                if (sourceDirectoryChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File sourcePathChoosen = sourceDirectoryChooser.getSelectedFile();
                    sourcePath.setText(sourcePathChoosen.toString());
                    logText.append("Искать файлы в папке: " + sourceDirectoryChooser.getSelectedFile() + "\n");
                } else {
                    logText.append("Папка, из которой копировать файлы, не выбрана \n");
                }
            }
        });
        // Отображение инструкции по работе с программой
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPanel, "Программа предназначена для переноса файлов в папки НКК.\n" +
                        "В директории, которая указана в Source, проверяются все файлы.\n" +
                        "Если файл назван по номеру консультанта, то файл будет перенесён в подпапку Folder name (если её нет, то она будет создана) папки Destination\\Номер_НКК\n" +
                        "\n" +
                        "Описание кнопок:\n" +
                        "Source - папка, из которой нужно копировать файлы\n" +
                        "Destination - папка, в которой будет идти поиск папок НКК\n" +
                        "File name: имя в файла, в которое будет переименован переносимый файл\n" +
                        "Folder Name: название папки, в которую будет переноситься файл\n" +
                        "Transfer - по нажатию этой кнопки начнётся перенос файлов\n" +
                        "Help - вызов этого меню :)\n" +
                        "ClearLog - очищение текстового поля, в котором описывается прогресс\n\n\nВерсия программы - 1.1\n\n\nПрограмму написал Степан Кутырев - Stepan.Kutyrev@mkcorp.com");
            }
        });

        // Выбор папки, в которую будут переноситься файлы
        JFileChooser destinationDirectoryChooser = new JFileChooser();
        destinationChoise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destinationDirectoryChooser.setCurrentDirectory(new java.io.File("."));
                destinationDirectoryChooser.setDialogTitle("Папка, в которую копировать");
                destinationDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                destinationDirectoryChooser.setAcceptAllFileFilterUsed(false);
                if (destinationDirectoryChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File destinationPathChoosen = destinationDirectoryChooser.getSelectedFile();
                    destinationPath.setText(destinationPathChoosen.toString());
                    logText.append("Искать НКК в папке: " + destinationDirectoryChooser.getSelectedFile() + "\n");
                } else {
                    logText.append("Папка, в которую переносить файлы, не выбрана \n");
                }
            }
        });

        // Логика копирования файлов
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileNameInputString = fileNameInput.getText().trim();
                folderNameInputString = folderNameInput.getText().trim();
                logText.setText("");
                // Блок проверки введённых данных
                if (sourcePath.getText().equals("")) {
                    logText.append("НЕ ВЫБРАНА ПАПКА, ИЗ КОТОРОЙ КОПИРОВАТЬ!\n");
                }
                if (destinationPath.getText().equals("")) {
                    logText.append("НЕ ВЫБРАНА ПАПКА, В КОТОРУЮ КОПИРОВАТЬ!\n");
                }
                if (fileNameInputString.equals("ВЫБЕРИ ИМЯ ФАЙЛА")) {
                    logText.append("ИМЯ ФАЙЛА НЕ ВЫБРАНО!\n");
                }
                if (folderNameInputString.equals("ВЫБЕРИ ИМЯ ПАПКИ")) {
                    logText.append("ИМЯ ПАПКИ НЕ ВЫБРАНО!\n");
                    return;
                }

                logText.append("Имя файла: " + fileNameInputString + "\n");
                logText.append("Имя папки: " + folderNameInputString + "\n");
                logText.append("Source path: " + sourcePath.getText() + "\n");
                logText.append("Destination path: " + destinationPath.getText() + "\n\n");

                File filesList = new File(sourcePath.getText().trim());
                String[] filesName = filesList.list();
                int filesCounter = 0;
                for (String str : filesName) {
                    String[] nameAndExtension = str.split("\\.");
                    if (nameAndExtension.length == 2) {filesCounter++;}
                }
                float progressStep = 100/filesCounter;
                float progressCounter=0;
                for (String str : filesName) {
                    String[] nameAndExtension = str.split("\\.");
                    String destinationFolderTrimmed = nameAndExtension[0].trim();
                    if (nameAndExtension.length == 1) {
                        logText.append(nameAndExtension[0] + " это папка, пропускаем\n\n");
                        continue;
                    }
                    try {
                        logText.append("Пробую файл:" + nameAndExtension[0] + "." + nameAndExtension[1] + "\n");
                        Path source = Paths.get(sourcePath.getText().trim() + "\\" + nameAndExtension[0] + "." + nameAndExtension[1]);
                        Path destinationInputFolder = Paths.get(destinationPath.getText().trim() + "\\" + destinationFolderTrimmed + "\\" + folderNameInput.getText().trim());
                        Path checkIfIbcFolderExist = Paths.get(destinationPath.getText().trim() + "\\" + destinationFolderTrimmed);
                        if (!Files.exists(checkIfIbcFolderExist)) {
                            logText.append("Папка для НКК " + nameAndExtension[0] + " не существует!\n\n");
                            continue;
                        }
                        if (!Files.exists(destinationInputFolder)) {
                            boolean success = new File(destinationPath.getText().trim() + "\\" + destinationFolderTrimmed + "\\" + folderNameInput.getText().trim()).mkdir();
                            if (!success) {
                                logText.append("Не удалось создать папку: " + destinationInputFolder + "\n");
                                continue;
                            } else {
                                logText.append("Создана папка: " + destinationInputFolder + "\n");
                            }
                        }
                        Path destination = Paths.get(destinationPath.getText().trim() + "\\" + destinationFolderTrimmed + "\\" + folderNameInput.getText().trim() + "\\" +
                                fileNameInput.getText().trim() + "." + nameAndExtension[1]);
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        logText.append("Файл " + nameAndExtension[0] + " перемещён в " + destination + "\n\n");
                        progressCounter+=progressStep*100;
                        transferProgressBar.setValue((int)progressCounter);
                    } catch (Exception el) {
                        logText.append("ОШИБКА: " + el + "\n");
                        JOptionPane.showMessageDialog(rootPanel,el,"ОШИБКА",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        clearLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logText.setText("");
            }
        });
    }


}
