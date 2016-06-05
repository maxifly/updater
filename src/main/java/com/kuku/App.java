package com.kuku;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxim.Pantuhin on 08.10.2014.
 */
public class App {
    public static String OLD = "old";
    public static String NEW = "new";
    public static String EXECUTE = "execute";

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, InterruptedException {
        System.out.println("kuku");
        Map<String, String> arguments = new HashMap<>();
        try {
            int i = 0;

            String key = "";
            for (String s : args) {
                if (i % 2 == 0) {
                    key = s;
                    if (!key.startsWith("--")) {
                        throw new ErrorParameters("Error parameters");
                    }
                } else {
                    System.out.println("Key " + key + ", value: " + s);
                    arguments.put(key.substring(2), s);
                }
                i++;
            }

            if (!arguments.containsKey(OLD)) {
                throw new ErrorParameters("Can not found old key.");
            }
            if (!arguments.containsKey(NEW)) {
                throw new ErrorParameters("Can not found new key.");
            }
            if (!arguments.containsKey(EXECUTE)) {
                throw new ErrorParameters("Can not found execute key.");
            }

            // Проверки пройдены
            //Сохранение старых файлов
            Path tempDir = Files.createTempDirectory("updater_");

            File newSrc = new File(arguments.get(NEW));
            File oldSrc = new File(arguments.get(OLD));
            File oldDest = new File(tempDir.toString(), oldSrc.getName());

            System.out.println("Copy old version to " + oldDest.toString());
            try {
                Files.copy(oldSrc.toPath(), oldDest.toPath());
            }
            catch (NoSuchFileException noSuchFileException) {
                noSuchFileException.printStackTrace();
            }

            //Удаляем старые файлы
            long Timeout = System.currentTimeMillis() + 600000; //TODO В аргументы?

            boolean delOld = false;
            boolean copyNew = false;


            while (System.currentTimeMillis() < Timeout) {
                try {

                    if (!delOld) {
                        System.out.println("Try delete " + oldSrc.toString());
                        try {
                            Files.delete(oldSrc.toPath());
                            System.out.println(oldSrc.toString() + " deleted.");
                        }
                        catch (NoSuchFileException noSuchFileException) {
                            System.out.println("No such file " + noSuchFileException.getFile());
                        }


                        delOld = true;
                    }

                    if (!copyNew) {
                        File newDest = new File(oldSrc.toPath().getParent().toString(), newSrc.getName());
                        System.out.println("Try copy " + newSrc.toString() + " to " + newDest.toString());
                        Files.copy(newSrc.toPath(), newDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println(newSrc.toString() + " copied");
                        copyNew = true;
                        break;
                    }

                    if(!copyNew) {
                        throw new ErrorTimeout("timeout");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread.sleep(1000);


            }


            // Запуск приложения
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(arguments.get(EXECUTE));

            //   MainFrame mf = factory.getMainFrame();

            //   mf.start();

        } catch (ErrorParameters | ErrorTimeout errorParameters) {
            System.out.println(errorParameters.getMessage());
            Thread.sleep(10000);
        } catch (IOException e) {
            e.printStackTrace();
            Thread.sleep(10000);
        }
        System.out.println("tutu");



    }

}
